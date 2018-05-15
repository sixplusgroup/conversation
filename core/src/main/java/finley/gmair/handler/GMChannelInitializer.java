package finley.gmair.handler;

import finley.gmair.message.PacketEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GMChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    GMPacketHandler gmPacketHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
//        pipeline.addLast(new LineBasedFrameDecoder(1024));
//        pipeline.addLast(new PacketEncoder());
        pipeline.addLast(new ByteArrayEncoder());
        pipeline.addLast(gmPacketHandler);
    }
}