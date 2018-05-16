package finley.gmair.handler;

import finley.gmair.model.packet.HeartBeatPacket;
import finley.gmair.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
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
        byte[] CTF = new byte[]{0x02};

        byte[] CID = new byte[]{0x00};

        byte[] UID = new byte[]{0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41};

        long time = System.currentTimeMillis();

        byte[] TIM = ByteUtil.long2byte(time, 8);

        byte[] LEN = new byte[]{0x00};

        HeartBeatPacket packet = new HeartBeatPacket(CTF, CID, UID, TIM, LEN);

        byte[] request = packet.convert2bytearray();

        ctx.writeAndFlush(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }
}