package finley.gmair.netty;

import finley.gmair.handler.GMChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;

@Component
@PropertySource(value = "classpath:/netty-server.properties")
public class GMServer {
    @Value("${tcp.port}")
    private int port;

    @Value("${boss.thread.count}")
    private int boss;

    @Value("${worker.thread.count}")
    private int worker;

    @Value("${so.keepalive}")
    private boolean keepAlive;

    @Value("${so.backlog}")
    private int backlog;

    @Bean(name = "boss", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(boss);
    }

    @Bean(name = "worker", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(worker);
    }

    @Bean(name = "address")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(port);
    }

    @Bean(name = "repository")
    public GMRepository repository() {
        return new GMRepository();
    }

    @Bean(name = "initializer")
    public GMChannelInitializer initializer() {
        return new GMChannelInitializer();
    }

    @Autowired
    @Qualifier("initializer")
    private GMChannelInitializer initializer;

    @Bean(name = "bootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup(), workerGroup()).handler(new LoggingHandler(LogLevel.DEBUG)).channel(NioServerSocketChannel.class).childHandler(initializer).option(ChannelOption.SO_BACKLOG, backlog);
        return bootstrap;
    }

    @Autowired
    @Qualifier("bootstrap")
    private ServerBootstrap bootstrap;

    private Channel channel;

    public void start() {
        try {
            channel = bootstrap.bind(port).sync().channel().closeFuture().sync().channel();
        } catch (Exception e) {

        }
    }

    @PreDestroy
    public void stop() throws Exception {
        channel.close();
        channel.parent().close();
    }
}
