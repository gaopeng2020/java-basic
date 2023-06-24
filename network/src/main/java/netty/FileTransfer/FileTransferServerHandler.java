package netty.FileTransfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netty.demos.ByteBufUtil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

@Slf4j
public final class FileTransferServerHandler extends ChannelDuplexHandler {
    private final ChannelPipeline pipeline;

    public FileTransferServerHandler(ChannelPipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String functions = """
                Response 0 to start string(UTF-8 Charset) echo;
                Response 1 to start file upload function.
                Response 2 to start file download function.
                more functions are in developing...
                """;
        byte[] bytes = functions.getBytes(StandardCharsets.UTF_8);
        ctx.channel().writeAndFlush(bytes);
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = ByteBufUtil.lengthFieldDecode(buf);
        ProtoFilePackage.FilePackage filePackage = ProtoFilePackage.FilePackage.parseFrom(bytes);

        pipeline.addLast("ProtobufDecoder", new ProtobufDecoder(ProtoFilePackage.FilePackage.getDefaultInstance()));
        super.channelRead(ctx, filePackage);
        pipeline.addLast(new SimpleChannelInboundHandler<ProtoFilePackage.FilePackage>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, ProtoFilePackage.FilePackage msg) {
                fileUploadHandler(msg);
            }
        });
    }
private int start=0;
    private void fileUploadHandler(@NonNull ProtoFilePackage.FilePackage msg) {
        File file = getFieByName(msg.getFileName());
        log.info("==============File Path = {}",file.getAbsolutePath());
        byte[] bytes = msg.getContents().toByteArray();
        int startPos = msg.getCurrentEndPos();
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            //移动至start位置开始写入文件
            randomAccessFile.seek(start);
            randomAccessFile.write(bytes);
            start+=bytes.length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //add length field for payload.
        if (msg instanceof byte[]) {
            ByteBuf buf = ByteBufUtil.lengthFieldEncode((byte[]) msg);
            super.write(ctx, buf, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage());
        super.exceptionCaught(ctx, cause);
    }

    private static File getFieByName(String fileName) {
        File userHome = new File(System.getProperty("user.home"));
        File[] files = userHome.listFiles(pathname -> pathname.isDirectory() && pathname.getName().equals("Downloads"));
        File dir;
        if (files == null) {
            dir = new File(userHome, "Downloads");
        } else {
            dir = files[0];
        }

        File[] fileNames = dir.listFiles(name -> name.getName().equals(fileName));
        if (fileNames == null || fileNames.length ==0) {
            return new File(dir, fileName);
        } else {
            return fileNames[0];
        }
    }
}
