package netty.nettySsl;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;
import tls.SSLContextUtil;

import javax.net.ssl.SSLEngine;
import java.io.IOException;

@Slf4j
public class NettySslServer {
    private static final int PORT = 5566;

    public void bind() throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws IOException {
                        SSLEngine sslEngine = SSLContextUtil.getServerSSLEngine("TLSv1.2");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addFirst("ssl", new SslHandler(sslEngine));
                        pipeline.addLast(new LineBasedFrameDecoder(1024));
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast("processMsg", new SslDemoServerSideHandler());
                    }
                });

        // 绑定端口，同步等待成功
        b.bind(PORT).sync();

        System.out.println("Netty server start on  : " + PORT);
    }

    public static void main(String[] args) throws Exception {
        new NettySslServer().bind();
    }
}
