package netty.demos;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.log4j.Log4j2;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

@Log4j2
public class HelloClient {
    private static final boolean USE_SYNC = false;

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture channelFuture = bootstrap
                .group(group)
                // 选择客户 Socket 实现类，NioSocketChannel 表示基于 NIO 的客户端实现
                .channel(NioSocketChannel.class)
                // ChannelInitializer 处理器（仅执行一次）
                // 它的作用是待客户端SocketChannel建立连接后，执行initChannel以便添加更多的处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        // 添加入栈handler
                        channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(
                                        1024, 0, 4, 0, 4))
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buffer = (ByteBuf) msg;
                                        String message = buffer.toString(StandardCharsets.UTF_8);
                                        log.info(message);
                                        super.channelRead(ctx, msg);
                                    }
                                    //添加出栈handler实现对出栈的消息添加length field的功能
                                }).addLast(new ChannelOutboundHandlerAdapter() {
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        byte[] bytes = (byte[]) msg;
                                        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
                                        buf.writeInt(bytes.length);
                                        buf.writeBytes(bytes);
                                        super.write(ctx, buf, promise);
                                    }
                                });
                    }
                    // 指定要连接的服务器和端口,但connect方法会交给NioEventLoop执行
                }).connect(new InetSocketAddress("localhost", 8080));

        // 使用同步阻塞方法sync  等待channel建立连接
        AtomicReference<Channel> channel = new AtomicReference<>();
        if (USE_SYNC) {
            channelFuture.sync();
            // 写入消息到channel，然后进入出栈Handler处理
            String msg = "client通过channelFuture.sync()方法同步阻塞建立socket连接";
            channel.set(channelFuture.channel());
            channel.get().writeAndFlush(msg.getBytes(StandardCharsets.UTF_8));
        } else {
            //Netty也为channelFuture提供了异步阻塞的方式:addListener
            channelFuture.addListener((ChannelFutureListener) future ->
            {
                String msg = "client通过channelFuture.addListener()方法异步建立socket连接";
                channel.set(future.channel());
                channel.get().writeAndFlush(msg.getBytes(StandardCharsets.UTF_8));
            });
        }

        //连接建立后通过键盘发送消息给server
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String next = scanner.nextLine();
            if (next.equals("q")) {
                channel.get().close();
                break;
            }
            System.out.println("next = " + next);
            channel.get().writeAndFlush(next.getBytes(StandardCharsets.UTF_8));
        }

        //channel关闭处理
        ChannelFuture closeFuture = channelFuture.channel().closeFuture();
        closeFuture.addListener((ChannelFutureListener) future -> {
            log.info("some actions after channel closed");
            group.shutdownGracefully();
        });
    }
}
