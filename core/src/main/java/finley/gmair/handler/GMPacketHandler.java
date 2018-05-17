package finley.gmair.handler;

import finley.gmair.annotation.PacketConfig;
import finley.gmair.model.machine.MachineStatus;
import finley.gmair.model.packet.*;
import finley.gmair.netty.GMRepository;
import finley.gmair.pool.CorePool;
import finley.gmair.service.CommunicationService;
import finley.gmair.service.LogService;
import finley.gmair.util.*;
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
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] request = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(request);
        //judge the header
        if (request[0] == (byte) 0xFF && request[request.length - 1] == (byte) 0xEE) {
            /* if match 0xFF, then it should be the 2nd version packet */
            AbstractPacketV2 packet = PacketUtil.transferV2(request);
            String uid = packet.getUID().trim();
            new Thread(() -> logService.createMachineComLog(uid, "Send packet", new StringBuffer("Client: ").append(uid).append(" of 2nd version sends a packet to server").toString(), ((InetSocketAddress) ctx.channel().remoteAddress()).getAddress().getHostAddress())).start();
            if (StringUtils.isEmpty(repository.retrieve(uid)) || repository.retrieve(uid) != ctx.channel()) {
                repository.push(uid, ctx);
            }
            //check the timestamp of the packet, if longer that 0.5 minute, abort it
            if (TimeUtil.timestampDiff(System.currentTimeMillis(), packet.getTime()) >= 1000 * 30) {
                return;
            }
            //the packet is valid, give response to the client and process the packet in a new thread
            HeartBeatPacket response = PacketUtil.generateHeartBeat(uid);
            ctx.writeAndFlush(response.convert2bytearray());
            new Thread(() -> {
                //nothing to do with heartbeat packet
                if (packet instanceof HeartBeatPacket) {
                    //give a heart beat packet as response
                }
                //
                if (packet instanceof ProbePacket && packet.isValid()) {
                    //decode packet
                    byte[] CTF = ((ProbePacket) packet).getCTF();
                    //judge whether the packet is a data packet
                    if (CTF != new byte[]{0x03}) return;
                    int command = ByteUtil.byte2int(((ProbePacket) packet).getCID());
                    //if it belongs to a normal probe packet
                    if (command == 0x00) {
                        //decode the data byte array
                    } else {
                        Field[] fields = PacketInfo.class.getDeclaredFields();
                        for (Field field : fields) {
                            if (field.isAnnotationPresent(PacketConfig.class)) {
                                PacketConfig pf = field.getAnnotation(PacketConfig.class);
                                if (command != pf.command()) {
                                    continue;
                                }
                                String vt = field.getGenericType().getTypeName();
                                byte[] data = ((ProbePacket) packet).getDAT();
                                switch (vt) {
                                    case "int":
                                        ByteUtil.byte2int(data);
                                        break;
                                    case "long":
                                        ByteUtil.byte2int(data);
                                        break;
                                    case "String":
                                        new String(data);
                                }

                            }
                        }
                    }
                    byte[] data = ((ProbePacket) packet).getDAT();
                    CorePool.getLogExecutor().execute(new Thread(() -> communicationService.create(new MachineStatus(IDGenerator.generate(""), 1, 26, 20, 12, 100, 100, 0, 1))));
                }
            }).start();
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
        ctx.close();
    }
}
