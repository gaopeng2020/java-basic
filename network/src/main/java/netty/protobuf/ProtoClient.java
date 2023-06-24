package netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ProtoClient {
    public static void main(String[] args) {
        launch("localhost", 8080);
    }

    private static void launch(String inetHost, int inetPort) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ProtoClientHandler());
        try {
            bootstrap.connect(inetHost, inetPort).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
