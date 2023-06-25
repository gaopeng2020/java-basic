package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import netty.FileTransfer.FileTransferClientHandler;

import java.io.File;

public class NettyClient {
    private final String inetHost;
    private final int inetPort;

    public NettyClient(String host, int port) {
        this.inetHost = host;
        this.inetPort = port;
    }

    public static void main(String[] args) {
        String filePath = "F:\\Software\\CATIA_P3_V5R21\\Setup_Win32\\64位破解文件\\JS0GROUP.dll";
        File file = new File(filePath);
        new NettyClient("localhost", 8088).launch(file);
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
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(128*1024, 0, 4, 0, 0))
//                                .addLast(loggingHandler)
                                .addLast("ProtobufEncoder", new ProtobufEncoder())
                                .addLast(new FileTransferClientHandler(file))
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