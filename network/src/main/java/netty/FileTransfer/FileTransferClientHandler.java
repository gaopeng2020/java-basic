package netty.FileTransfer;

import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import netty.demos.ByteBufUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

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
        FileInputStream fis = new FileInputStream(file);
        String md5 = ByteBufUtil.getFileDigest(fis, "MD5");

        //文件只读，不存在不创建，抛出异常
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.file, "r");
        //开始位置初始为0
        randomAccessFile.seek(start);

        byte[] bytes = new byte[63 * 1024];
        int len;
        while ((len = randomAccessFile.read(bytes)) != -1) {
//        while ((len = fis.read(bytes)) != -1) {
            boolean hasMoteSegments = len == bytes.length;
            ProtoFilePackage.FilePackage build = ProtoFilePackage.FilePackage.newBuilder()
                    .setFileName(file.getName())
                    .setFileSize(file.length())
                    .setContents(ByteString.copyFrom(bytes, 0, len))
                    .setMd5(md5)
                    .setHasMoreSegments(hasMoteSegments)
                    .build();
            byte[] byteArray = build.toByteArray();
            ctx.channel().writeAndFlush(byteArray);
            super.channelActive(ctx);
        }
        System.out.println("sender md5 = " + md5);
        System.out.println("sender file.length = " + file.length());
        fis.close();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //add length field for payload
        System.out.println("____write____");
        if (msg instanceof byte[]) {
            ByteBuf buf = ByteBufUtil.lengthFieldEncode((byte[]) msg);
            super.write(ctx, buf, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }
}
