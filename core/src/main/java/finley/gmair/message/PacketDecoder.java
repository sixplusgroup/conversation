package finley.gmair.message;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

@ChannelHandler.Sharable
public class PacketDecoder extends MessageToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) throws Exception {

    }
}
