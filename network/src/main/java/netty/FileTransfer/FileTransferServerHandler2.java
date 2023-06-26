package netty.FileTransfer;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufUtil;
import netty.utils.PayloadTypeEnum;
import org.graalvm.collections.Pair;

import java.io.*;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class FileTransferServerHandler2 extends ChannelInboundHandlerAdapter {
    private long startTime;
    private final static ExecutorService workerThreadService = Executors.newFixedThreadPool(6);


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String functions = """
                You can directly start test server string echo function;
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
        Pair<PayloadTypeEnum, byte[]> pair = ByteBufUtil.payloadTypeDecode(buf);
        PayloadTypeEnum payloadTypeEnum = pair.getLeft();
        byte[] bytes = pair.getRight();

        switch (payloadTypeEnum) {
            case STRING_ECHO -> {
                String message = ByteBufUtil.stringDecode(bytes);
                String downloadFileList = getDownloadFiles();
                if (message.equals("2")) {
                    message = "Please response file name of bellow: \n" + downloadFileList;
                } else if (downloadFileList.contains(message)) {
                    String finalMessage = message;
                    workerThreadService.submit(() -> readByteFromFile(ctx, finalMessage));
                }

                String echoMessage = "[echo] " + message;
                ByteBuf byteBuf = ByteBufUtil.payloadTypeEncode(ByteBufUtil.stringEncode(echoMessage), PayloadTypeEnum.STRING_ECHO);
                ctx.writeAndFlush(byteBuf);
                super.channelRead(ctx, message);
            }
            case UPLOAD_FILE ->
                //写入文件非常耗时用，最好在自己的线程池中处理该任务，防止netty io阻塞。
                    workerThreadService.submit(() -> {
                        long receivedFileLength = 0;
                        try {
                            ProtoFilePackage.FilePackage filePackage = ProtoFilePackage.FilePackage.parseFrom(bytes);
                            writeBytes2File(ctx, filePackage, receivedFileLength);
                        } catch (InvalidProtocolBufferException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        // buf.release(); buf可以在trail handler中自动释放
        super.channelRead(ctx, buf);
    }

    private void readByteFromFile(ChannelHandlerContext ctx, String fileName) {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.println(socketAddress + "channelInactive...");
        ctx.channel().close();
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered...");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered...");
        super.channelRegistered(ctx);
    }

    private void writeBytes2File(ChannelHandlerContext ctx, ProtoFilePackage.FilePackage filePackage, long buffedFileLen) {
        //存储客户端上传的文件存储到用户的Download目录下
        long phyFileLen = filePackage.getFileSize();
        File file = getFieByName(filePackage.getFileName(), phyFileLen);

        //如果文件大小相同且MD5值也一样，则无需传输了
        if (phyFileLen == file.length() && ByteBufUtil.fileMd5Verification(file, filePackage)) {
            ByteBufUtil.sendStringMessage(ctx, "传输完成");
            return;
        }
        //如果已写入的文件长度大于记录的长度则说明该文件不合法，没有必要在传输了
        if (buffedFileLen != file.length()) {
            if (file.exists()) {
                file.delete();
            }
            ByteBufUtil.sendStringMessage(ctx, "传输失败");
            return;
        }
        byte[] bytes = filePackage.getContents().toByteArray();

        buffedFileLen += bytes.length;
        try (FileOutputStream fos = new FileOutputStream(file, true);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //report process of uploaded file
        reportFileUploadProcess(ctx, file, filePackage);

        //没有后续分片之后，执行文件完整性校验，并将结果发送给客户端
        boolean hasMoreSegments = filePackage.getHasMoreSegments();
        if (!hasMoreSegments) {
            String message;
            if (ByteBufUtil.fileMd5Verification(file, filePackage)) {
                message = "传输完成";
            } else {
                if (file.exists()) {
                    file.delete();
                }
                message = "传输失败";
            }
            ByteBufUtil.sendStringMessage(ctx, message);
        }
    }

    /**
     * report process of uploaded file
     *
     * @param ctx         ChannelHandlerContext
     * @param file        file
     * @param filePackage filePackage
     */
    private void reportFileUploadProcess(@NonNull ChannelHandlerContext ctx, @NonNull File file, @NonNull ProtoFilePackage.FilePackage filePackage) {
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
            ByteBuf buf = ByteBufUtil.payloadTypeEncode(ByteBufUtil.stringEncode(message), PayloadTypeEnum.STRING_ECHO);
            ctx.writeAndFlush(buf);
        }
    }


    /**
     * get Fie By Name
     *
     * @param fileName fileName
     * @param fileSize fileSize
     * @return File
     */
    private File getFieByName(String fileName, long fileSize) {
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

    /**
     * get Files under Download director of user home
     *
     * @return string: files name
     */
    private String getDownloadFiles() {
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

    /**
     * get Download director of user's home
     *
     * @return Download director
     */
    private File getDownloadDir() {
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
