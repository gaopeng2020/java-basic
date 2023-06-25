package netty.FileTransfer;

import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import netty.demos.ByteBufUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileTransferClientHandler extends ChannelDuplexHandler {
    private File file;
    private long startTime = 0;

    public FileTransferClientHandler(File file) {
        if (!file.exists()) {
            log.error("to upload file does not exist!");
            return;
        }
        this.file = file;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(fis);
        String md5 = ByteBufUtil.getFileDigest(fis, "MD5");
        //动态设置缓冲区大小
        int base = 32;
        if (file.length() > 1000 * 1024 * 1024) {
            base = 127;
        } else if (file.length() > 500 * 1024 * 1024) {
            base = 96;
        } else if (file.length() > 100 * 1024 * 1024) {
            base = 64;
        }

        //文件只读，不存在不创建，抛出异常
        RandomAccessFile randomAccessFile = new RandomAccessFile(this.file, "r");
        //开始位置初始为0
        randomAccessFile.seek(0);

        byte[] bytes = new byte[base * 1024];
        int len;
        while ((len = randomAccessFile.read(bytes)) != -1) {
//        while ((len = bis.read(bytes)) != -1) {
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
        if (msg instanceof byte[]) {
            ByteBuf buf = ByteBufUtil.lengthFieldEncode((byte[]) msg);
            super.write(ctx, buf, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte[] bytes = ByteBufUtil.lengthFieldDecode((ByteBuf) msg);
        String message = new String(bytes, StandardCharsets.UTF_8);
        if ("传输完成".equals(message)) {
            long time = System.currentTimeMillis() - startTime;
            String unit = " ms";
            if (time > 1000) {
                time /= 1000;
                unit = " s";
            }
            System.out.println("文件传输完成，用时:" + time + unit);
            ctx.channel().close();
        } else if ("传输失败".equals(message)) {
            System.out.println("传输失败，请重新传输该文件！");
        } else {
            System.out.println("message = " + message);
        }
        super.channelRead(ctx, msg);
    }
}
