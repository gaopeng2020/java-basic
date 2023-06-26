package netty.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import netty.FileTransfer.ProtoFilePackage;
import org.graalvm.collections.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

public class ByteBufUtil {
    public static void log(ByteBuf buffer) {
        int length = buffer.readableBytes();
        int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder buf = new StringBuilder(rows * 80 * 2)
                .append("read index:").append(buffer.readerIndex())
                .append(" write index:").append(buffer.writerIndex())
                .append(" capacity:").append(buffer.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(buf, buffer);
        System.out.println(buf);
    }

    public static byte[] stringEncode(String src) {
        return src.getBytes(StandardCharsets.UTF_8);
    }

    public static String stringDecode(byte[] src) {
        return new String(src, StandardCharsets.UTF_8);
    }

    /**
     *
     * @param byteArray byteArray
     * @return ByteBuf
     */
    public static ByteBuf lengthFieldEncode(byte[] byteArray) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        int length = byteArray.length;
        buf.writeInt(length);
        buf.writeByte(0);
        buf.writeBytes(byteArray);
        return buf;
    }

    public static ByteBuf payloadTypeEncode(byte[] byteArray, PayloadTypeEnum payloadTypeEnum) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        int length = byteArray.length + 1;
        buf.writeInt(length);
        switch (payloadTypeEnum) {
            case STRING_ECHO -> buf.writeByte(0);
            case UPLOAD_FILE -> buf.writeByte(1);
            case DOWNLOAD_FILE -> buf.writeByte(2);
            default -> buf.writeByte(255);
        }
        buf.writeBytes(byteArray);
        return buf;
    }

    public static byte[] lengthFieldDecode(ByteBuf buf) {
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return bytes;
    }

    public static Pair<PayloadTypeEnum, byte[]> payloadTypeDecode(ByteBuf src) {
        PayloadTypeEnum payloadTypeEnum;
        int length = src.readInt();
        byte messageType = src.readByte();
        switch (messageType) {
            case 0 -> payloadTypeEnum = PayloadTypeEnum.STRING_ECHO;
            case 1 -> payloadTypeEnum = PayloadTypeEnum.UPLOAD_FILE;
            case 2 -> payloadTypeEnum = PayloadTypeEnum.DOWNLOAD_FILE;
            default -> payloadTypeEnum = PayloadTypeEnum.INVALID;
        }
        byte[] bytes = new byte[length - 1];
        src.readBytes(bytes);

        return Pair.create(payloadTypeEnum, bytes);
    }

    public static String getFileDigest(InputStream fis, String algorithm) throws Exception {
        // 获取消息摘要对象
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        int len;
        byte[] buffer = new byte[4096];
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while ((len = fis.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, len);
//            os.write(buffer, 0, len);
        }
        // 获取消息摘要
        byte[] digest = messageDigest.digest();

        return Base64.getEncoder().encodeToString(digest);
    }

    public static boolean fileMd5Verification(File file, ProtoFilePackage.FilePackage filePackage) {
        boolean flag = false;
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
        return flag;
    }

    public static void sendStringMessage(ChannelHandlerContext ctx, String msg) {
        ByteBuf buf = ByteBufUtil.payloadTypeEncode(msg.getBytes(StandardCharsets.UTF_8), PayloadTypeEnum.STRING_ECHO);
        ctx.writeAndFlush(buf);
    }
}
