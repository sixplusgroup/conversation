package finley.gmair.handler;

import finley.gmair.model.packet.AbstractPacketV2;
import finley.gmair.model.packet.HeartBeatPacket;
import finley.gmair.model.packet.ProbePacket;
import finley.gmair.util.ByteUtil;
import finley.gmair.util.PacketUtil;
import finley.gmair.util.TimeUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class GMClientV2Handler extends ChannelInboundHandlerAdapter {
    public GMClientV2Handler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] request = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(request);
        if (request[0] == (byte) 0xFF && request[request.length - 1] == (byte) 0xEE) {
            /* if match 0xFF, then it should be the 2nd version packet */
            AbstractPacketV2 packet = PacketUtil.transferV2(request);
            String uid = packet.getUID().trim();
            //check the timestamp of the packet, if longer that 0.5 minute, abort it
            if (TimeUtil.timestampDiff(System.currentTimeMillis(), packet.getTime()) >= 1000 * 30) {
                return;
            }
            //nothing to do with heartbeat packet
            if (packet instanceof HeartBeatPacket) {
                //actions that need to be implement if this is a no-data packet
                System.out.println(new StringBuffer("A heart beat packet ").append(uid).append(" has been received"));
            }
            //
        } else if (request[0] == (byte) 0xEF && request[request.length - 1] == (byte) 0xEE) {
            /* if match 0xFF, then it should be the 1st version packet */
            return;
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] CTF = new byte[]{0x03};

        byte[] CID = new byte[]{0x00};

        //byte[] UID = ByteUtil.string2byte(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12), 12);

        byte[] UID = ByteUtil.string2byte("zxczxc", 12);

        byte[] data = new byte[]{0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01};

        while (true) {

            long time = System.currentTimeMillis();
            byte[] TIM = ByteUtil.long2byte(time, 8);

            byte[] LEN = new byte[]{0x0C};

            ProbePacket packet = new ProbePacket(CTF, CID, UID, TIM, LEN, data);

            byte[] request = packet.convert2bytearray();

            ctx.writeAndFlush(request);

            System.out.println(new StringBuffer("A heartbeat packet from client: ").append(new String(UID).trim()).append(" has been sent."));

            Thread.sleep(10000);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}
