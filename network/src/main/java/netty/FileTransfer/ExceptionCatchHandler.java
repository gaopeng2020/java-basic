package netty.FileTransfer;

import io.netty.channel.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchHandler extends ChannelDuplexHandler {
    //catch inbound exception
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    //catch inbound exception
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ctx.write(msg, promise.addListener((ChannelFutureListener) channelFuture -> {
            if (!channelFuture.isSuccess()) {
                log.error(channelFuture.cause().getMessage());
            }
        }));
        super.write(ctx, msg, promise);
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.write(promise.addListener((ChannelFutureListener) channelFuture -> {
            if (!channelFuture.isSuccess()) {
                System.out.println(ctx.channel().remoteAddress()+"---disconnect...");
                log.error(channelFuture.cause().getMessage());
            }
        }));
        super.disconnect(ctx, promise);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        ctx.write(promise.addListener((ChannelFutureListener) channelFuture -> {
            if (!channelFuture.isSuccess()) {
                System.out.println(ctx.channel().remoteAddress()+ "---close...");
                log.error(channelFuture.cause().getMessage());
            }
        }));
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                log.info("READER_IDLE");
                // 超时关闭channel
                ctx.close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                log.info("WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                log.info("ALL_IDLE");
                // 发送心跳
                ctx.channel().write("ping\n");
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
