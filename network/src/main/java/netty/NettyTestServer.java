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
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.ERROR);
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class);

        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.SO_RCVBUF, 65535)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("SslHandler", new SslHandler(SSLContextUtil.getServerSSLEngine("TLS")))
                                .addLast(new LengthFieldBasedFrameDecoder(
                                        128 * 1024, 0, 4, 0, 0))
                                // .addLast(loggingHandler)
                                .addLast(new FileTransferServerInboundHandler())
                                //.addLast("ProtobufDecoder", new ProtobufDecoder(ProtoDataTypes.ProtoDataType.getDefaultInstance()))
                                .addLast(new ExceptionCatchHandler())
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
