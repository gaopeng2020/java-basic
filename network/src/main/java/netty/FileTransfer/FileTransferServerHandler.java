package netty.FileTransfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netty.demos.ByteBufUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

@Slf4j
public final class FileTransferServerHandler extends ChannelInitializer<NioSocketChannel> {
    long startTime = 0;

    @Override
    protected void initChannel(NioSocketChannel ch) {
        LoggingHandler loggingHandler = new LoggingHandler(LogLevel.ERROR);
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                        65535, 0, 4, 0, 0))
                .addLast(loggingHandler)
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
                        startTime = System.currentTimeMillis();
//                        System.out.println("startTime = " + startTime);
                        ByteBuf buf = (ByteBuf) msg;
                        byte[] bytes = ByteBufUtil.lengthFieldDecode(buf);
                        ProtoFilePackage.FilePackage filePackage = ProtoFilePackage.FilePackage.parseFrom(bytes);
                        super.channelRead(ctx, filePackage);
                        buf.release();
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        log.error(cause.getMessage());
                        super.exceptionCaught(ctx, cause);
                    }
                })
                .addLast("ProtobufDecoder", new ProtobufDecoder(ProtoFilePackage.FilePackage.getDefaultInstance()))
                .addLast(new SimpleChannelInboundHandler<ProtoFilePackage.FilePackage>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ProtoFilePackage.FilePackage filePackage) {
                        //存储客户端上传的文件到用户的Download目录下
                        File file = fileUploadHandler(filePackage);

                        //文件完整性校验，校验不通过则请求Client重新发送，校验通过通知客户端传输完成
                        boolean hasMoreSegments = filePackage.getHasMoreSegments();
                        if (!hasMoreSegments) {
                            boolean hasVerified = fileMd5Verification(file, filePackage);
                            if (hasVerified) {
                                ctx.channel().writeAndFlush("传输完成".getBytes(StandardCharsets.UTF_8));
                                System.out.println("文件传输完成，用时:" + (System.currentTimeMillis() - startTime));
                            } else {
                                ctx.channel().writeAndFlush("传输失败".getBytes(StandardCharsets.UTF_8));
                            }

                        }
                    }
                }).addLast(new ChannelOutboundHandlerAdapter() {
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
                })
        ;
    }

    private boolean fileMd5Verification(File file, ProtoFilePackage.FilePackage filePackage) {
        if (filePackage.getFileSize() != file.length()) {
            return false;
        }
        try(FileInputStream fis = new FileInputStream(file)) {
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
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    private static File getFieByName(String fileName, long fileSize) {
        File userHome = new File(System.getProperty("user.home"));
        File[] files = userHome.listFiles(pathname -> pathname.isDirectory() && pathname.getName().equals("Downloads"));
        File dir;
        if (files == null) {
            dir = new File(userHome, "Downloads");
        } else {
            dir = files[0];
        }
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

}
