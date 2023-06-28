package netty.FileTransfer;

import com.google.protobuf.ByteString;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netty.utils.PayloadTypeEnum;
import org.graalvm.collections.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Slf4j
public class FileTransferCommonMethods {
    /**
     * write Bytes to File
     *
     * @param ctx         ChannelHandlerContext
     * @param filePackage ProtoFilePackage.FilePackage
     */
    public void writeBytes2File(ChannelHandlerContext ctx, ProtoFilePackage.FilePackage filePackage, String dirName) {
        //存储客户端上传的文件存储到用户的Download目录下
        long phyFileLen = filePackage.getFileSize();
        File file = getFieByName(filePackage.getFileName(), dirName);

        //如果文件大小相同且MD5值也一样，则无需传输了
        if (phyFileLen == file.length() && fileMd5Verification(file, filePackage)) {
            sendStringMessage(ctx, "传输完成");
            return;
        }

        byte[] bytes = filePackage.getContents().toByteArray();
        try (FileOutputStream fos = new FileOutputStream(file, true);
             BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            bos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //report process of uploaded file
        reportFileUploadProcess(ctx, file, filePackage);

        //没有后续分片或者因意外中断再次传输后导致文件的大小大于实际大小时，执行文件完整性校验，并将结果发送给客户端
        boolean hasMoreSegments = filePackage.getHasMoreSegments();
        if (!hasMoreSegments || file.length() > phyFileLen) {
            String message;
            if (fileMd5Verification(file, filePackage)) {
                message = "传输完成";
            } else {
                if (file.exists()) {
                    file.delete();
                }
                message = "传输失败";
            }
            sendStringMessage(ctx, message);
        }
    }

    /**
     * read Byte From File
     *
     * @param ctx  ChannelHandlerContext
     * @param file File
     * @throws Exception
     */
    public void readBytesFromFile(ChannelHandlerContext ctx, File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        String md5 = getFileDigest(fis, "MD5");
        //动态设置缓冲区大小
        int base = 32;
        if (file.length() > 1024 * 1024 * 1024) {
            base = 127;
        } else if (file.length() > 500 * 1024 * 1024) {
            base = 96;
        } else if (file.length() > 100 * 1024 * 1024) {
            base = 64;
        }

        //文件只读，不存在不创建，抛出异常
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        //开始位置初始为0
        randomAccessFile.seek(0);

        byte[] bytes = new byte[base * 1024];
        int len;
        while ((len = randomAccessFile.read(bytes)) != -1) {
            boolean hasMoteSegments = len == bytes.length;
            ProtoFilePackage.FilePackage build = ProtoFilePackage.FilePackage.newBuilder()
                    .setFileName(file.getName())
                    .setFileSize(file.length())
                    .setContents(ByteString.copyFrom(bytes, 0, len))
                    .setMd5(md5)
                    .setHasMoreSegments(hasMoteSegments)
                    .build();
            byte[] byteArray = build.toByteArray();
            ByteBuf buf = payloadTypeEncode(byteArray, PayloadTypeEnum.FILE);
            ctx.writeAndFlush(buf);
//            super.channelActive(ctx);
        }
        System.out.println("sender md5 = " + md5);
        System.out.println("sender file.length = " + file.length());
        fis.close();
    }

    /**
     * report process of uploaded file
     *
     * @param ctx         ChannelHandlerContext
     * @param file        file
     * @param filePackage filePackage
     */
    public void reportFileUploadProcess(@NonNull ChannelHandlerContext ctx, @NonNull File file, @NonNull ProtoFilePackage.FilePackage filePackage) {
        long filePhySize = filePackage.getFileSize();
        int devisor = 1;
        String uint = " Byte";
        int speed = 100;
        if (filePhySize > 500 * 1024 * 1024) {
            devisor = 1024 * 1024;
            uint = " MB";
            speed = 400;
        } else if (filePhySize > 500 * 1024) {
            devisor = 1024;
            uint = " KB";
            speed = 200;
        }
        long receivedLen = file.length();
        if (receivedLen % speed == 0 || receivedLen == filePhySize) {
            float percent = (float) receivedLen / filePhySize * 100;
            long send = receivedLen / devisor;
            long left = (filePhySize - receivedLen) / devisor;
            String message = "[" + String.format("%.2f", percent) + "%]" + " 已发送:" + send + uint + ",剩余:" + left + uint;
            sendStringMessage(ctx, message);
        }
    }

    /**
     * get Fie By Name
     *
     * @param fileName fileName
     * @param dirName
     * @return File
     */
    public File getFieByName(String fileName, String dirName) {
        File dir = getDownloadDir(dirName);
        File[] fileNames = dir.listFiles(name -> name.getName().equals(fileName));
        if (fileNames == null || fileNames.length == 0) {
            return new File(dir, fileName);
        } else {
            return fileNames[0];
        }
    }

    /**
     * get Files under Download director of user home
     *
     * @return string: files name
     */
    public String getDownloadFiles(String dirName) {
        File dir = getDownloadDir(dirName);
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
    public File getDownloadDir(String dirName) {
        File userHome = new File(System.getProperty("user.home"));
        File[] downloadFiles = userHome.listFiles(file -> file.isDirectory() && file.getName().equals("Downloads"));
        File downloadDir;
        if (downloadFiles == null || downloadFiles.length == 0) {
            downloadDir = new File(userHome, "Downloads");
            downloadDir.mkdir();
        } else {
            downloadDir = downloadFiles[0];
        }
        File[] files = downloadDir.listFiles(file -> file.isDirectory() && file.getName().equals(dirName));
        File dir;
        if (files == null || files.length == 0) {
            dir = new File(downloadDir, dirName);
            dir.mkdir();
        } else {
            dir = files[0];
        }
        return dir;
    }

    public ByteBuf payloadTypeEncode(byte[] byteArray, PayloadTypeEnum payloadTypeEnum) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        int length = byteArray.length + 1;
        buf.writeInt(length);
        switch (payloadTypeEnum) {
            case TEXT -> buf.writeByte(0);
            case FILE -> buf.writeByte(1);

            default -> buf.writeByte(255);
        }
        buf.writeBytes(byteArray);
        return buf;
    }

    public Pair<PayloadTypeEnum, byte[]> payloadTypeDecode(ByteBuf src) {
        PayloadTypeEnum payloadTypeEnum;
        int length = src.readInt();
        byte messageType = src.readByte();
        switch (messageType) {
            case 0 -> payloadTypeEnum = PayloadTypeEnum.TEXT;
            case 1 -> payloadTypeEnum = PayloadTypeEnum.FILE;

            default -> payloadTypeEnum = PayloadTypeEnum.INVALID;
        }
        byte[] bytes = new byte[length - 1];
        src.readBytes(bytes);

        return Pair.create(payloadTypeEnum, bytes);
    }

    public String getFileDigest(InputStream fis, String algorithm) throws Exception {
        // 获取消息摘要对象
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        int len;
        byte[] buffer = new byte[8192];
        while ((len = fis.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, len);
        }
        // 获取消息摘要
        byte[] digest = messageDigest.digest();

        return Base64.getEncoder().encodeToString(digest);
    }

    public boolean fileMd5Verification(File file, ProtoFilePackage.FilePackage filePackage) {
        boolean flag = false;
        if (filePackage.getFileSize() != file.length()) {
            log.warn("接收的文件大小与实际大小不一致，接收的文件大小为：{}，文件实际大小为：{}", file.length(), filePackage.getFileSize());
            return false;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            String md5 = getFileDigest(fis, "MD5");
            System.out.println("Calculated md5 of Received File = " + md5);
            if (filePackage.getMd5().equals(md5)) {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return flag;
    }

    public void sendStringMessage(ChannelHandlerContext ctx, String msg) {
        ByteBuf buf = payloadTypeEncode(msg.getBytes(StandardCharsets.UTF_8), PayloadTypeEnum.TEXT);
        ctx.writeAndFlush(buf);
//        ctx.channel().writeAndFlush(buf);
    }
}
