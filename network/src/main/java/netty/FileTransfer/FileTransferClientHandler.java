package netty.FileTransfer;

import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import netty.demos.ByteBufUtil;

import java.io.File;
import java.io.RandomAccessFile;

@Slf4j
public class FileTransferClientHandler extends ChannelDuplexHandler {
    private File file;
    private int start = 0;

    public FileTransferClientHandler(File file) {
        if (!file.exists()) {
            log.error("to upload file does not exist!");
            return;
        }
        this.file = file;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.file, "r");//文件只读，不存在不创建，抛出异常
        randomAccessFile.seek(start);//开始位置初始为0
        byte[] bytes = new byte[1024];
        while (randomAccessFile.read(bytes) != -1) {
            ProtoFilePackage.FilePackage build = ProtoFilePackage.FilePackage.newBuilder()
                    .setFileName(file.getName())
                    .setContents(ByteString.copyFrom(bytes))
                    .build();
            ctx.channel().writeAndFlush(build);
            super.channelActive(ctx);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println("666=========================================================================999");
        //add length field for payload.
        if (msg instanceof byte[]) {
            ByteBuf buf = ByteBufUtil.lengthFieldEncode((byte[]) msg);
            super.write(ctx, buf, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }
}
