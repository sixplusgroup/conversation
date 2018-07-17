package finley.gmair.client;

import finley.gmair.handler.GMClientV2Handler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

public class GMClientV2 {
    public void connect(int port, String host) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
                public void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline().addLast(new ByteArrayEncoder());
                    channel.pipeline().addLast(new GMClientV2Handler());
                }
            });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }



    public static void main(String[] args) {
        int port = 8888;
        new Thread(() -> {
            try {
                //服务器host: 118.31.78.254     my host: 192.168.2.59
                new GMClientV2().connect(port, "118.31.78.254");
                System.out.println("A new client has been created");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
