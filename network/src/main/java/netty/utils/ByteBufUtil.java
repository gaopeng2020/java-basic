package netty.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.charset.StandardCharsets;

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

    public static byte[] lengthFieldDecode(ByteBuf buf) {
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return bytes;
    }

}
