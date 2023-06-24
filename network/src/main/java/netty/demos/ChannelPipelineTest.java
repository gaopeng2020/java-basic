package netty.demos;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.embedded.EmbeddedChannel;

import java.nio.charset.StandardCharsets;

/**
 * pipeline是结构是一个带有head与tail指针的双向链表，其中的节点为handler
 * 当有入站（Inbound）操作时，会从head开始向后调用handler，直到handler不是处理Inbound操作为止
 * 当有出站（Outbound）操作时，会从tail开始向前调用handler，直到handler不是处理Outbound操作为止
 * <p>
 * 具体结构如下:
 * <img src="https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20210423102354.png" alt="img" style="zoom:50%;" />
 * <p>
 * 调用顺序如下:
 * <img width="340" height="120" src="https://nyimapicture.oss-cn-beijing.aliyuncs.com/img/20210423105200.png" alt="img" style="zoom:40%;" />
 */
public class ChannelPipelineTest {
    public static void main(String[] args) {
        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                ByteBuf buf = (ByteBuf) msg;
                System.out.println("1:" + buf.toString(StandardCharsets.UTF_8));
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter h2 = new ChannelInboundHandlerAdapter() {
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                ByteBuf buf = (ByteBuf) msg;
                System.out.println("2:" + buf.toString(StandardCharsets.UTF_8));
                ByteBufUtil.log(buf);
                super.channelRead(ctx, msg);
            }
        };

        ChannelOutboundHandlerAdapter h3 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                ByteBuf buf = (ByteBuf) msg;
                System.out.println("3:" + buf.toString(StandardCharsets.UTF_8));
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter h4 = new ChannelOutboundHandlerAdapter() {
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                ByteBuf buf = (ByteBuf) msg;
                System.out.println("4:" + buf.toString(StandardCharsets.UTF_8));
                ByteBufUtil.log(buf);
                super.write(ctx, msg, promise);
            }
        };

        // 用于测试Handler的Channel
        EmbeddedChannel channel = new EmbeddedChannel(h1, h2, h3, h4);

        // 执行Inbound操作
        channel.writeInbound(ByteBufAllocator.DEFAULT.buffer().
                writeBytes("writeInbound".getBytes(StandardCharsets.UTF_8)));

        // 执行Outbound操作
        channel.writeOutbound(ByteBufAllocator.DEFAULT.buffer().
                writeBytes("writeOutbound".getBytes(StandardCharsets.UTF_8)));
    }
}
