package netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import netty.FileTransfer.ExceptionCatchHandler;
import netty.FileTransfer.FileTransferServerInboundHandler;
import tls.SSLContextUtil;

import javax.net.ssl.SSLEngine;
import java.util.Scanner;

public class NettyTestServer {
    private int port;

    public static void main(String[] args) {
        NettyTestServer nettyServer = new NettyTestServer(8888);
        nettyServer.launch();
    }

    public NettyTestServer(int port) {
        this.port = port;
    }

    public void launch() {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .option(ChannelOption.SO_BACKLOG, 100)
                .channel(NioServerSocketChannel.class)
                .handler(loggingHandler)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_RCVBUF, 65535)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        SSLEngine sslEngine = SSLContextUtil.getServerSSLEngine("TLSv1.3");
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline
                                .addFirst("ssl", new SslHandler(sslEngine))
                                .addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(
                                        128 * 1024, 0, 4, 0, 0))
                                .addLast("FileTransferServerInboundHandler", new FileTransferServerInboundHandler())
//                                //.addLast("ProtobufDecoder", new ProtobufDecoder(ProtoDataTypes.ProtoDataType.getDefaultInstance()))
                                .addLast("ExceptionCatchHandler", new ExceptionCatchHandler())
                        ;
                    }
                });


        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            Channel channel = channelFuture.channel();
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                if (scanner.nextLine().equalsIgnoreCase("exit")) {
                    channel.close();
                }
            }).start();
            channel.closeFuture().addListener((ChannelFutureListener) future -> {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
