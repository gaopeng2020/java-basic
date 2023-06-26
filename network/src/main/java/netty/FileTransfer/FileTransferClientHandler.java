package netty.FileTransfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import netty.utils.PayloadTypeEnum;
import org.graalvm.collections.Pair;

import java.io.File;
import java.nio.charset.StandardCharsets;

@Slf4j
public class FileTransferClientHandler extends ChannelInboundHandlerAdapter {
    private File file;
    private long startTime = 0;
    private FileTransferCommonMethods fileTransferUtil;

    public FileTransferClientHandler(File file) {
        if (!file.exists()) {
            log.error("to upload file does not exist!");
            return;
        }
        this.file = file;
        startTime = System.currentTimeMillis();
        fileTransferUtil = new FileTransferCommonMethods();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        fileTransferUtil.readBytesFromFile(ctx, file);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        Pair<PayloadTypeEnum, byte[]> pair = fileTransferUtil.payloadTypeDecode(buf);
        PayloadTypeEnum payloadTypeEnum = pair.getLeft();
        byte[] bytes = pair.getRight();
        switch (payloadTypeEnum) {
            case TEXT -> {
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
                    ctx.channel().close();
                } else {
                    System.out.println("message = " + message);
                }
            }
            case FILE -> {

            }
            default -> log.error("Invalid payload type: {}", buf);
        }
        super.channelRead(ctx, buf);
    }
}
