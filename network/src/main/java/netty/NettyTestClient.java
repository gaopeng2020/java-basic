package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import netty.FileTransfer.ExceptionCatchHandler;
import netty.FileTransfer.FileTransferClientInboundHandler;
import tls.SSLContextUtil;

import java.io.File;

public class NettyTestClient {
    private final String inetHost;
    private final int inetPort;

    public NettyTestClient(String host, int port) {
        this.inetHost = host;
        this.inetPort = port;
    }

    public static void main(String[] args) {
        String filePath = "F:\\Software\\AutoCAD 2020_x64.7z";
//        String filePath = "C:\\Users\\gaopeng\\Downloads\\Browser\\Configuring Watch Dog in AUTOSAR Stack.mp4";
        File file = new File(filePath);
        new NettyTestClient("192.168.10.28", 8088).launch(file);
    }

    private void launch(File file) {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.INFO);
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("SslHandler", new SslHandler(SSLContextUtil.getClientSSLEngine("TLS")))
                                .addLast(new LengthFieldBasedFrameDecoder(
                                        128 * 1024, 0, 4, 0, 0))
//                                .addLast(loggingHandler)
                                .addLast(new FileTransferClientInboundHandler(file))
                                //.addLast("ProtobufEncoder", new ProtobufEncoder())
                                .addLast(new ExceptionCatchHandler())
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
