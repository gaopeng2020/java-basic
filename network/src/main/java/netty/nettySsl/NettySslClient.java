package netty.nettySsl;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import lombok.extern.slf4j.Slf4j;
import netty.FileTransfer.FileTransferClientInboundHandler;
import tls.SSLContextUtil;

import javax.net.ssl.SSLEngine;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Slf4j
public class NettySslClient {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(String host, int port) {

        // 配置客户端NIO线程组
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            SSLEngine sslEngine = SSLContextUtil.getClientSSLEngine("TLSv1.2");
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addFirst("ssl", new SslHandler(sslEngine));
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new LineBasedFrameDecoder(1024));
                            pipeline.addLast(new StringDecoder());
                            // ipeline.addLast("processMsg", new SslDemoClientSideHandler());
                            pipeline.addLast("processMsg", new FileTransferClientInboundHandler());
                        }
                    });

            // 发起异步连接操作
            ChannelFuture future = b.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception ex) {
            log.info("connection exception", ex);
        }
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String destIp = "192.168.10.28";
        int port = 5566;
        new NettySslClient().connect(destIp, port);
    }

}