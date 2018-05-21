package finley.gmair.handler;

import finley.gmair.model.packet.ProbePacket;
import finley.gmair.util.ByteUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class GMClientV2Handler extends ChannelInboundHandlerAdapter {
    public GMClientV2Handler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte[] CTF = new byte[]{0x03};

        byte[] CID = new byte[]{0x00};

        byte[] UID = new byte[]{0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41};

        long time = System.currentTimeMillis();

        byte[] TIM = ByteUtil.long2byte(time, 8);

        byte[] LEN = new byte[]{0x01};

        byte[] DAT = new byte[]{0x01};

        ProbePacket packet = new ProbePacket(CTF, CID, UID, TIM, LEN, DAT);

        byte[] request = packet.convert2bytearray();

        ctx.writeAndFlush(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}
