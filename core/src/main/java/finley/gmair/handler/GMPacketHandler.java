package finley.gmair.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.annotation.PacketConfig;
import finley.gmair.model.machine.MachinePartialStatus;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.packet.AbstractPacketV2;
import finley.gmair.model.packet.HeartBeatPacket;
import finley.gmair.model.packet.PacketInfo;
import finley.gmair.model.packet.ProbePacket;
import finley.gmair.netty.GMRepository;
import finley.gmair.pool.CorePool;
import finley.gmair.service.CommunicationService;
import finley.gmair.service.LogService;
import finley.gmair.util.ByteUtil;
import finley.gmair.util.PacketUtil;
import finley.gmair.util.TimeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.sql.Timestamp;

@Component
@ChannelHandler.Sharable
public class GMPacketHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    @Qualifier("repository")
    private GMRepository repository;

    @Autowired
    private CommunicationService communicationService;

    @Autowired
    private LogService logService;

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
        byteBuf.readBytes(request);
        byteBuf.release();
        //judge the header
        if (request[0] == (byte) 0xFF && request[request.length - 1] == (byte) 0xEE) {
            /* if match 0xFF, then it should be the 2nd version packet */
            AbstractPacketV2 packet = PacketUtil.transferV2(request);
            String uid = packet.getUID().trim();
            //System.out.println(uid + "send packet!");
            CorePool.getLogExecutor().execute(new Thread(() -> logService.createMachineComLog(uid, "Send packet", new StringBuffer("Client: ").append(uid).append(" of 2nd version sends a packet to server").toString(), ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress())));
            if (StringUtils.isEmpty(repository.retrieve(uid)) || repository.retrieve(uid) != ctx) {
                repository.push(uid, ctx);
            }

            //System.out.println("current time: "+new Timestamp(System.currentTimeMillis()) + "... packet time:" + new Timestamp(packet.getTime()));
            //check the timestamp of the packet, if longer that 0.5 minute, abort it

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
                    System.out.println(new StringBuffer("A heart beat packet ").append(uid).append(" has been received"));
                }
                //
                if (packet instanceof ProbePacket && packet.isValid()) {
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
                                            for (int i = 0; i < 21; i += 2) {
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
        } else if (request[0] == (byte) 0xEF && request[request.length - 1] == (byte) 0xEE) {
            /* if match 0xFF, then it should be the 1st version packet */

            return;
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
}
