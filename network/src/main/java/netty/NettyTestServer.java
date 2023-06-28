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

public class NettyTestServer {
    private int port;

    public static void main(String[] args) {
        NettyTestServer nettyServer = new NettyTestServer(8088);
        nettyServer.launch();
    }

    public NettyTestServer(int port) {
        this.port = port;
    }

    public void launch() {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.DEBUG);
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup(16);
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(loggingHandler);

        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_RCVBUF, 65535)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline
                                .addFirst("SslHandler", new SslHandler(SSLContextUtil.getServerSSLEngine("TLSv1.2"),false))
//                                .addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(
//                                        128 * 1024, 0, 4, 0, 0))
                                .addLast("FileTransferServerInboundHandler", new FileTransferServerInboundHandler())
                                //.addLast("ProtobufDecoder", new ProtobufDecoder(ProtoDataTypes.ProtoDataType.getDefaultInstance()))
                                .addLast("ExceptionCatchHandler", new ExceptionCatchHandler())
                        ;
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future -> {
                bossGroup.shutdownGracefully();
                workGroup.shutdownGracefully();
            });
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
