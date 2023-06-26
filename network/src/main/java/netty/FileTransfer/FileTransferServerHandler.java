package netty.FileTransfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufUtil;
import netty.utils.PayloadTypeEnum;
import org.graalvm.collections.Pair;

import java.io.*;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

@Slf4j
public final class FileTransferServerHandler extends ChannelInitializer<NioSocketChannel> {
    private final long startTime;

    public FileTransferServerHandler() {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void initChannel(NioSocketChannel ch) {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.ERROR);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                        128 * 1024, 0, 4, 0, 0))
//                .addLast(loggingHandler)
                .addLast(new ChannelInboundHandlerAdapter() {
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
//                        byte[] bytes = ByteBufUtil.lengthFieldDecode(buf);

                        Pair<PayloadTypeEnum, byte[]> pair = ByteBufUtil.payloadTypeDecode(buf);
                        PayloadTypeEnum payloadTypeEnum = pair.getLeft();
                        byte[] bytes = pair.getRight();
                        switch (payloadTypeEnum) {
                            case STRING_ECHO -> {
                                String message = new String(bytes, StandardCharsets.UTF_8);
                                ctx.channel().writeAndFlush(("[echo]" + message).getBytes(StandardCharsets.UTF_8));
                                super.channelRead(ctx, message);
                            }
                            case UPLOAD_FILE -> {
                                ProtoFilePackage.FilePackage filePackage = ProtoFilePackage.FilePackage.parseFrom(bytes);
                                super.channelRead(ctx, filePackage);
                            }
                            case DOWNLOAD_FILE -> {

                            }
                        }

                        buf.release();
                    }

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        SocketAddress socketAddress = ctx.channel().remoteAddress();
                        System.out.println(socketAddress + "断开了连接！");

                        ctx.channel().close();
                        super.channelInactive(ctx);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        log.error(cause.getMessage());
                        super.exceptionCaught(ctx, cause);
                    }
                })
//                .addLast("ProtobufDecoder", new ProtobufDecoder(ProtoFilePackage.FilePackage.getDefaultInstance()))
                .addLast(new SimpleChannelInboundHandler<ProtoFilePackage.FilePackage>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ProtoFilePackage.FilePackage filePackage) {
                        //存储客户端上传的文件到用户的Download目录下
                        File file = fileUploadHandler(filePackage);

                        fileUploadServerEcho(ctx, file, filePackage);

                        //文件完整性校验，校验不通过则请求Client重新发送，校验通过通知客户端传输完成
                        boolean hasMoreSegments = filePackage.getHasMoreSegments();
                        if (!hasMoreSegments) {
                            boolean hasVerified = fileMd5Verification(file, filePackage);
                            String message;
                            if (hasVerified) {
                                message = "传输完成";
                                System.out.println("文件传输完成，用时:" + (System.currentTimeMillis() - startTime) / 1000);
                            } else {
                                if (file.exists()) {
                                    file.delete();
                                }
                                message = "传输失败";
                            }
                            ByteBuf buf = ByteBufUtil.payloadTypeEncode(message.getBytes(StandardCharsets.UTF_8), PayloadTypeEnum.STRING_ECHO);
                            ctx.channel().writeAndFlush(buf);
                        }
                    }
                })
                .addLast(new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        //add length field for payload.
//                        if (msg instanceof byte[]) {
//                            ByteBuf buf = ByteBufUtil.lengthFieldEncode((byte[]) msg);
//                            super.write(ctx, buf, promise);
//                        } else {
//                        }
                            super.write(ctx, msg, promise);
                    }
                })
        ;
    }

    private void fileUploadServerEcho(@NonNull ChannelHandlerContext ctx, @NonNull File file, @NonNull ProtoFilePackage.FilePackage filePackage) {
        long fileSize = filePackage.getFileSize();
        int devisor = 1;
        String uint = " Byte";
        if (fileSize > 10 * 1024 * 1024) {
            devisor = 1024 * 1024;
            uint = " MB";
        } else if (fileSize > 10 * 1024) {
            devisor = 1024;
            uint = " KB";
        }
        long recLen = file.length();
        if (recLen % 100 == 0) {
            float percent = (float) recLen / fileSize * 100;
            long send = recLen / devisor + recLen % devisor;
            long left = (fileSize - recLen) / devisor + (fileSize - recLen) % devisor;
            String message = "[" + String.format("%.2f", percent) + "%]" + " 已发送:" + send + uint + ",剩余:" + left + uint;
            ByteBuf buf = ByteBufUtil.payloadTypeEncode(message.getBytes(StandardCharsets.UTF_8), PayloadTypeEnum.STRING_ECHO);
            ctx.writeAndFlush(buf);
        }
    }

    private boolean fileMd5Verification(File file, ProtoFilePackage.FilePackage filePackage) {
        if (filePackage.getFileSize() != file.length()) {
            return false;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            String md5 = ByteBufUtil.getFileDigest(fis, "MD5");
            System.out.println("Calculated md5 of Received File = " + md5);
            if (filePackage.getMd5().equals(md5)) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private File fileUploadHandler(@NonNull ProtoFilePackage.FilePackage msg) {
        byte[] bytes = msg.getContents().toByteArray();
        File file = getFieByName(msg.getFileName(), msg.getFileSize());
        try (FileOutputStream fos = new FileOutputStream(file, true);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private static File getFieByName(String fileName, long fileSize) {
        File dir = getDownloadDir();
        File[] fileNames = dir.listFiles(name -> name.getName().equals(fileName));
        if (fileNames == null || fileNames.length == 0) {
            return new File(dir, fileName);
        } else {
            File existFile = fileNames[0];
            if (existFile.length() >= fileSize) {
                boolean delete = existFile.delete();
                System.out.println("是否删除了已有文件：" + delete);
                return new File(dir, fileName);
            } else {
                return existFile;
            }
        }
    }

    private static String getDownloadFiles() {
        File dir = getDownloadDir();
        File[] files = dir.listFiles(File::isFile);
        if (files == null) {
            return "There is no file in download director of usr home";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (File file : files) {
            stringBuilder.append(file.getName())
                    .append("\n");
        }
        return stringBuilder.toString();
    }

    private static File getDownloadDir() {
        File userHome = new File(System.getProperty("user.home"));
        File[] files = userHome.listFiles(pathname -> pathname.isDirectory() && pathname.getName().equals("Downloads"));
        File dir;
        if (files == null) {
            dir = new File(userHome, "Downloads");
        } else {
            dir = files[0];
        }
        return dir;
    }

}
