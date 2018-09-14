package finley.gmair.handler;

import com.alibaba.fastjson.JSON;
import finley.gmair.datastructrue.LimitQueue;
import finley.gmair.annotation.PacketConfig;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.machine.MachineV1Status;
import finley.gmair.model.packet.*;
import finley.gmair.netty.GMRepository;
import finley.gmair.netty.V1BoardRepository;
import finley.gmair.pool.CorePool;
import finley.gmair.service.CommunicationService;
import finley.gmair.service.LogService;
import finley.gmair.service.RedisService;
import finley.gmair.util.ByteUtil;
import finley.gmair.util.PacketUtil;
import finley.gmair.util.TimeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;

@Component
@ChannelHandler.Sharable
public class GMPacketHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    @Qualifier("repository")
    private GMRepository repository;

    @Autowired
    private V1BoardRepository v1BoardRepository;

    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private LogService logService;

    @Autowired
    private RedisService redisService;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        //log the active status
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        repository.remove(ctx);
        ctx.channel().close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] request = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(request).release();

        //第一代板子
        if (request[0] == (byte) 0xEF) {
            int FRH_LEN = 1, CTF_LEN = 1, CID_LEN = 1, UID_LEN = 12, LEN_LEN = 1, CRC_LEN = 2, FRT_LEN = 1;
            int FIRST_PACKET_DAT_LEN = ByteUtil.byte2int(new byte[]{request[15]});
            int FIRST_PACKET_LEN = FRH_LEN + CTF_LEN + CID_LEN + UID_LEN + LEN_LEN + FIRST_PACKET_DAT_LEN + CRC_LEN + FRT_LEN;
            byte[] first = new byte[FIRST_PACKET_LEN];
            try {
                System.arraycopy(request, 0, first, 0, FIRST_PACKET_LEN);
                handleRequest(first, ctx);
            } catch (Exception e) {
                System.out.println("error happen when the receive byte = " + ByteBufUtil.hexDump(request));
                System.out.println("first_packet_len = " + first.length);
            }
        }
        //第二代板子
        if (request[0] == (byte) 0xFF) {
            int FRH_LEN = 1, CTF_LEN = 1, CIT_LEN = 1, UID_LEN = 12, TIM_LEN = 8, LEN_LEN = 1, CRC_LEN = 2, FRT_LEN = 1;
            int FIRST_PACKET_DAT_LEN = ByteUtil.byte2int(new byte[]{request[23]});
            int FIRST_PACKET_LEN = FRH_LEN + CTF_LEN + CIT_LEN + UID_LEN + TIM_LEN + LEN_LEN + FIRST_PACKET_DAT_LEN + CRC_LEN + FRT_LEN;
            //byte[] first = new byte[FIRST_PACKET_LEN];
            //System.arraycopy(request, 0, first, 0, FIRST_PACKET_LEN);
            handleRequest(request, ctx);

            //处理粘包
            if (request.length > FIRST_PACKET_LEN) {
                int SECOND_PACKET_DAT_LEN = ByteUtil.byte2int(new byte[]{request[FIRST_PACKET_LEN + 23]});
                int SECOND_PACKET_LEN = FRH_LEN + CTF_LEN + CIT_LEN + UID_LEN + TIM_LEN + LEN_LEN + SECOND_PACKET_DAT_LEN + CRC_LEN + FRT_LEN;
                byte[] second = new byte[SECOND_PACKET_LEN];
                try {
                    System.arraycopy(request, FIRST_PACKET_LEN, second, 0, SECOND_PACKET_LEN);
                    handleRequest(second, ctx);
                }catch (Exception e){

                }
            }
        }

    }

    private void handleRequest(byte[] request, ChannelHandlerContext ctx) {


        //V2板子报文的接收、解析逻辑
        if (request[0] == (byte) 0xFF && request[request.length - 1] == (byte) 0xEE) {
            AbstractPacketV2 packet = PacketUtil.transferV2(request);
            String uid = packet.getUID().trim();
            CorePool.getLogExecutor().execute(new Thread(() -> logService.createMachineComLog(uid, "Send packet", new StringBuffer("Client: ").append(uid).append(" of 2nd version sends a packet to server").toString(), ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress())));
            if (StringUtils.isEmpty(repository.retrieve(uid)) || repository.retrieve(uid) != ctx) {
                repository.push(uid, ctx);
            }

            //the packet is valid, give response to the client and process the packet in a new thread
            HeartBeatPacket response = PacketUtil.generateHeartBeat(uid);
            ctx.writeAndFlush(response.convert2bytearray());

            if (TimeUtil.timestampDiff(System.currentTimeMillis(), packet.getTime()) >= 1000 * 30) {
                return;
            }
            CorePool.getComExecutor().execute(new Thread(() -> {
                //nothing to do with heartbeat packet
                if (packet instanceof HeartBeatPacket) {
                    //actions that need to be implement if this is a no-data packet
                    //System.out.println(new StringBuffer("A heart beat packet ").append(uid).append(" has been received"));
                }
                if (packet instanceof ProbePacket && packet.isValid()) {
                    //System.out.println(new StringBuffer("A probe packet ").append(uid).append(" has been received"));
                    //decode packet
                    byte[] CTF = ((ProbePacket) packet).getCTF();
                    //judge whether the packet is a data packet
                    if (CTF[0] != (byte) 0x03) return;
                    int command = ByteUtil.byte2int(((ProbePacket) packet).getCID());
                    //if it belongs to a normal probe packet
                    if (command == 0x00) {
                        //decode the data byte array, it should be confirmed with Jay Gao
                        byte[] data = ((ProbePacket) packet).getDAT();
                        if (data.length != 13) return;
                        byte[] pm2_5 = new byte[]{data[0], data[1]};
                        byte[] temp = new byte[]{data[2]};
                        byte[] humid = new byte[]{data[3]};
                        byte[] co2 = new byte[]{data[4], data[5]};
                        byte[] volume = new byte[]{data[6], data[7]};
                        byte[] power = new byte[]{data[8]};
                        byte[] mode = new byte[]{data[9]};
                        byte[] heat = new byte[]{data[10]};
                        byte[] light = new byte[]{data[11]};
                        byte[] lock = new byte[]{data[12]};
                        MachineStatus status = new MachineStatus(uid, ByteUtil.byte2int(pm2_5), ByteUtil.byte2int(temp), ByteUtil.byte2int(humid), ByteUtil.byte2int(co2), ByteUtil.byte2int(volume), ByteUtil.byte2int(power), ByteUtil.byte2int(mode), ByteUtil.byte2int(heat), ByteUtil.byte2int(light), ByteUtil.byte2int(lock));
//                        System.out.println(new StringBuffer("Machine status received: " + JSONObject.toJSONString(status)));
                        communicationService.create(status);
                        CorePool.getComExecutor().execute(new Thread(() -> {
                            LimitQueue<MachineStatus> queue = null;
                            if(redisService.exists(uid)==false){
                                queue = new LimitQueue<>(120);
                                queue.offer(status);
                            }else{
                                queue = (LimitQueue<MachineStatus>)redisService.get(uid);
                                queue.offer(status);
                            }
                            redisService.set(uid,queue,(long) 120);
                            //redisService.set(uid, status, (long) 120);
                        }));
                    } else {
                        Field[] fields = PacketInfo.class.getDeclaredFields();
                        for (Field field : fields) {
                            if (field.isAnnotationPresent(PacketConfig.class)) {
                                PacketConfig pf = field.getAnnotation(PacketConfig.class);
                                if (command != pf.command()) {
                                    continue;
                                }
                                String name = pf.name();
                                byte[] data = ((ProbePacket) packet).getDAT();
                                String type = field.getGenericType().getTypeName();
                                MachinePartialStatus status;
                                System.out.println(type);
                                switch (type) {
                                    case "int":
                                        status = new MachinePartialStatus(uid, name, ByteUtil.byte2int(data), packet.getTime());
                                        communicationService.create(status);
//                                        System.out.println(new StringBuffer("Machine partial status received: " + JSONObject.toJSONString(status)));
                                        break;
                                    case "java.lang.String":
                                        status = new MachinePartialStatus(uid, name, new String(data), packet.getTime());
                                        communicationService.create(status);
//                                        System.out.println(new StringBuffer("Machine partial status received: " + JSONObject.toJSONString(status)));
                                        break;
                                    case "int[]":
                                        //format volumes
                                        if (command == 0x10) {
                                            int[] volumes = new int[21];
                                            byte[] temp = new byte[2];
                                            for (int i = 0; i < 42; i += 2) {
                                                temp[0] = data[i];
                                                temp[1] = data[i + 1];
                                                volumes[i / 2] = ByteUtil.byte2int(temp);
                                            }
                                            status = new MachinePartialStatus(uid, name, JSON.toJSONString(volumes), packet.getTime());
                                            communicationService.create(status);
                                            break;
                                        }
                                }
                            }
                        }
                    }
                }
            }));
        }


        //V1板子报文的接收、解析逻辑
        else if (request[0] == (byte) 0xEF && request[request.length - 1] == (byte) 0xEE) {
            /* if match 0xEF, then it should be the 1nd version packet */
            AbstractPacketV1 packet = PacketUtil.transferV1(request);
            String uid = packet.getUID().trim();
            //CorePool.getLogExecutor().execute(new Thread(() -> logService.createMachineComLog(uid, "Send packet", new StringBuffer("Client: ").append(uid).append(" of 1nd version sends a packet to server").toString(), ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress())));
            if (StringUtils.isEmpty(repository.retrieve(uid)) || repository.retrieve(uid) != ctx) {
                repository.push(uid, ctx);
            }

            //judge whether a packet has been received within limited time
            if (!StringUtils.isEmpty(v1BoardRepository.retrieve(uid))) {
                return;
            } else {
                v1BoardRepository.push(uid, uid);
            }

            //the packet is valid, give response to the client and process the packet in a new thread
            HeartbeatPacketV1 response = PacketUtil.generateHeartbeatPacketV1(uid);
            if (request[1] == 0x02)
                ctx.writeAndFlush(response.convert2bytearray());
            CorePool.getComExecutor().execute(new Thread(() ->
            {
                if (packet instanceof HeartbeatPacketV1 && packet.isValid()) {
                    //decode packet
                    byte[] CTF = ((HeartbeatPacketV1) packet).getCTF();
                    //judge whether the packet is a data packet
                    if (CTF[0] != (byte) 0x02)
                        return;

                    //convert packet to MachineV1Status
                    byte[] data = ((HeartbeatPacketV1) packet).getDAT();
                    if (data.length != 32) {
                        System.out.println("data length is not 32");
                        return;
                    }
                    byte[] pm2_5 = new byte[]{data[0], data[1]};
                    byte[] temperature = new byte[]{data[2]};
                    byte[] humidity = new byte[]{data[3]};
                    byte[] hcho = new byte[]{data[4], data[5]};
                    byte[] co2 = new byte[]{data[6], data[7]};
                    byte[] velocity = new byte[]{data[8], data[9]};
                    byte[] power = new byte[]{data[10]};
                    byte[] workMode = new byte[]{data[11]};
                    byte[] uv = new byte[]{data[12]};
                    byte[] heat = new byte[]{data[13]};
                    byte[] light = new byte[]{data[14]};
                    byte[] cycle = new byte[]{data[15]};
                    byte[] voc = new byte[]{data[16]};
                    byte[] signal = new byte[]{data[17], data[18]};
                    String time = TimeUtil.getCurrentTime();
                    MachineV1Status status = new MachineV1Status(uid, ByteUtil.byte2int(pm2_5), ByteUtil.byte2int(temperature), ByteUtil.byte2int(humidity), ByteUtil.byte2int(hcho), ByteUtil.byte2int(co2), ByteUtil.byte2int(velocity), ByteUtil.byte2int(power), ByteUtil.byte2int(workMode), ByteUtil.byte2int(uv), ByteUtil.byte2int(heat), ByteUtil.byte2int(light), ByteUtil.byte2int(cycle), ByteUtil.byte2int(voc), ByteUtil.byte2int(signal), time);
                    finley.gmair.model.machine.v1.MachineStatus machineV1Status = new finley.gmair.model.machine.v1.MachineStatus(uid, status.getPm25(), status.getTemperature(), status.getHumidity(), status.getCo2(), status.getVelocity(), status.getPower(), status.getWorkMode(), status.getHeat(), status.getLight());
                    communicationService.create(status);                //把接收到的全存mongo数据库
                    CorePool.getComExecutor().execute(new Thread(() -> {
                        LimitQueue<finley.gmair.model.machine.v1.MachineStatus> queue = null;
                        if(redisService.exists(uid)==false){
                            queue = new LimitQueue<>(720);
                            queue.offer(machineV1Status);
                        }else{
                            queue = (LimitQueue<finley.gmair.model.machine.v1.MachineStatus>)redisService.get(uid);
                            queue.offer(machineV1Status);
                        }
                        redisService.set(uid,queue,(long) 120);
//                        redisService.set(uid, machineV1Status, (long) 120); //存入缓存
                    }));
                }
            }));
        }

        //未知报文
        else {
            System.out.println("unkown packet:" + ByteUtil.byte2Hex(request));
            System.out.println("head :" + ByteUtil.byte2Hex(new byte[]{request[0]}));
            System.out.println("tail :" + ByteUtil.byte2Hex(new byte[]{request[request.length - 1]}));
            System.out.println(new String(request));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        repository.remove(ctx);
        ctx.channel().close();
    }

    private void outputHex(ByteBuf byteBuf) {
        System.out.println("Hex:" + ByteBufUtil.hexDump(byteBuf));
        String receivce = ByteBufUtil.hexDump(byteBuf);
        System.out.println("FRH:" + receivce.substring(0, 2));
        System.out.println("CTF:" + receivce.substring(2, 4));
        System.out.println("CID:" + receivce.substring(4, 6));
        System.out.println("UID:" + receivce.substring(6, 30));
        System.out.println("LEN:" + receivce.substring(30, 32));
        int length = Integer.valueOf(receivce.substring(30, 32), 16);
        System.out.println("DAT:" + receivce.substring(32, 32 + length * 2));
        System.out.println("CRC:" + receivce.substring(32 + length * 2, 32 + length * 2 + 4));
        System.out.println("FRT:" + receivce.substring(32 + length * 2 + 4, 32 + length * 2 + 6));
    }
}
