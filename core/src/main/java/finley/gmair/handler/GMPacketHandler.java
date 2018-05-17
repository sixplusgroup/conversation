package finley.gmair.handler;

import finley.gmair.model.packet.AbstractPacketV2;
import finley.gmair.model.packet.HeartBeatPacket;
import finley.gmair.model.packet.ProbePacket;
import finley.gmair.netty.GMRepository;
import finley.gmair.util.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@ChannelHandler.Sharable
public class GMPacketHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    @Qualifier("repository")
    private GMRepository repository;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
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
        //judge the header, if match 0xFF, then it should be the 2nd version packet
        if (request[0] == (byte) 0xFF && request[request.length - 1] == (byte) 0xEE) {
            AbstractPacketV2 packet = PacketUtil.transferV2(request);
            String uid = packet.getUID().trim();
            if (StringUtils.isEmpty(repository.retrieve(uid)) || repository.retrieve(uid) != ctx.channel()) {
                repository.push(uid, ctx);
            }
            if (packet instanceof HeartBeatPacket) {
                //give a heart beat packet as response
                HeartBeatPacket response = PacketUtil.generateHeartBeat(uid);
                ctx.writeAndFlush(response.convert2bytearray());
            }
            if (packet instanceof ProbePacket) {
                HeartBeatPacket response = PacketUtil.generateHeartBeat(uid);
                ctx.writeAndFlush(response.convert2bytearray());
            }
        } else {
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
