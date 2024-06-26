package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import netty.FileTransfer.ExceptionCatchHandler;
import netty.FileTransfer.FileTransferClientInboundHandler;
import tls.SSLContextUtil;

import javax.net.ssl.SSLEngine;
import java.io.File;

/**
 * -Djavax.net.debug=ssl,handshake;-Ddeployment.security.TLSv1.2=true
 */
public class NettyTestClient {
    private final String inetHost;
    private final int inetPort;

    public NettyTestClient(String host, int port) {
        this.inetHost = host;
        this.inetPort = port;
    }

    public static void main(String[] args) {
//        String filePath = "F:\\Software\\AutoCAD 2020_x64.7z";
        String filePath = "C:\\Users\\gaopeng\\Downloads\\Browser\\Configuring Watch Dog in AUTOSAR Stack.mp4";
        File file = new File(filePath);
        new NettyTestClient("172.100.9.62", 8087).launch(file);
    }

    private void launch(File file) {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        SSLEngine sslEngine = SSLContextUtil.getClientSSLEngine("TLSv1.3");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline
                                .addFirst("ssl", new SslHandler(sslEngine))
                                .addLast(loggingHandler)
                                .addLast("LengthFieldBasedFrameDecoder",new LengthFieldBasedFrameDecoder(
                                        128 * 1024, 0, 4, 0, 0))
                                .addLast("FileTransferClientInboundHandler",new FileTransferClientInboundHandler(file))
//                                //.addLast("ProtobufEncoder", new ProtobufEncoder())
                                .addLast("ExceptionCatchHandler",new ExceptionCatchHandler())
                        ;
                    }
                });
        try {
            ChannelFuture future = bootstrap.connect(inetHost, inetPort).sync();
            future.channel().closeFuture().addListener((ChannelFutureListener) channelFuture -> group.shutdownGracefully());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
