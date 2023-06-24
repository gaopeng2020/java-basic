package netty.demos;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;

@Log4j2
//@Slf4j
public class HelloServer {
    public static void main(String[] args) {
        //当worker group处理一个耗时较长的任务时会阻塞其他客户端与server的通信，此时可以将耗时较长的任务交给其他的group处理
        EventLoopGroup myGroup = new DefaultEventLoopGroup();
        // 1、启动器，负责装配netty组件，启动服务器
        new ServerBootstrap()
                // 2、创建 NioEventLoopGroup，可以简单理解为 线程池 + Selector,第一个为boss group只负责accept事件，worker group负责socketchannel读写事件
                .group(new NioEventLoopGroup(), new NioEventLoopGroup())
                // 3、选择服务器的 ServerSocketChannel 实现
                .channel(NioServerSocketChannel.class)
                // 4、child 负责处理读写，该方法决定了 child 执行哪些操作
                // ChannelInitializer 处理器（仅执行一次）
                // 它的作用是待客户端SocketChannel建立连接后，执行initChannel以便添加更多的处理器
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        // 5、SocketChannel的处理器，使用StringDecoder解码，ByteBuf=>String
                        String header = "[ Received:]";
                        nioSocketChannel.pipeline()
                                //添加一个Handler解决粘包与半包问题，需要客户端发送数据时填充一个LengthField
                                .addLast(new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 4))
                                //添加一个Handler记录日志
                                .addLast(new LoggingHandler(LogLevel.DEBUG))
                                .addLast("worker group", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("worker group thread name = " + Thread.currentThread().getName());
                                        ByteBuf buffer = (ByteBuf) msg;
                                        String message = buffer.toString(StandardCharsets.UTF_8);
                                        log.info(message);
                                        super.channelRead(ctx, message);
                                    }
                                }).addLast(myGroup, "my group", new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("My group thread name = " + Thread.currentThread().getName());
                                        Thread.sleep(1000);
                                        log.info(msg.toString());
                                        String echo = "[Server已收到消息]: " + msg;
                                        ctx.channel().writeAndFlush(echo.getBytes(StandardCharsets.UTF_8));
                                        // 最后一个入栈Handler可以不用继续往下通知
                                        super.channelRead(ctx, msg);
                                    }
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

                    // 7、ServerSocketChannel绑定8080端口
                }).bind(8080);
    }
}
