package netty;

import io.netty.bootstrap.Bootstrap;
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
//        File file = new File("D:/ProgramFiles/DeveloperKits/protoc-23.3-win64.zip");
        File file = new File("D:/ProgramFiles/DeveloperKits/Qt/network.xml");
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
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(2048, 0, 4, 0, 0))
                                .addLast(loggingHandler)
                                .addLast("ProtobufEncoder", new ProtobufEncoder())
                                .addLast(new FileTransferClientHandler(file))
                        ;
                    }
                });
        try {
            bootstrap.connect(inetHost, inetPort).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
