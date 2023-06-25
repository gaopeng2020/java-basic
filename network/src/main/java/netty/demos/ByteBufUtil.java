package netty.demos;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    public static ByteBuf lengthFieldEncode( byte[] byteArray) {
        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        int length = byteArray.length;
//        System.out.println("send payload length = " + length);
        buf.writeInt(length);
        buf.writeBytes(byteArray);
        return buf;
    }

    public static byte[] lengthFieldDecode(ByteBuf buf) {
        int length = buf.readInt();
        System.out.println("received payload length = " + length);
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return bytes;
    }

    public static String getFileDigest(InputStream fis, String algorithm) throws Exception {
        int len;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while ((len = fis.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        // 获取消息摘要对象
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        // 获取消息摘要
        byte[] digest = messageDigest.digest(os.toByteArray());

        return Base64.getEncoder().encodeToString(digest);
    }
}
