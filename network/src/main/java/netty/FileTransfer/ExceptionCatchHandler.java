package netty.FileTransfer;

import io.netty.channel.*;
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
                log.error(channelFuture.cause().getMessage());
            }
        }));
        super.disconnect(ctx, promise);
    }
}
