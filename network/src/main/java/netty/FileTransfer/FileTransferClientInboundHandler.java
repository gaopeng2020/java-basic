package netty.FileTransfer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import netty.utils.PayloadTypeEnum;
import org.graalvm.collections.Pair;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class FileTransferClientInboundHandler extends ChannelInboundHandlerAdapter {
    private File file;
    private long startTime = 0;
    private FileTransferCommonMethods fileTransferUtil;
    private final static String CACHE_PATH = "ClientCache";
    private final static ExecutorService workerThreadService = Executors.newFixedThreadPool(6);

    public FileTransferClientInboundHandler(File file) {
        if (!file.exists()) {
            log.error("to upload file does not exist!");
            return;
        }
        this.file = file;
        startTime = System.currentTimeMillis();
        fileTransferUtil = new FileTransferCommonMethods();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive detected...");
        super.channelInactive(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        //连接建立后通过键盘发送消息给server
        workerThreadService.submit(()->{
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String next = scanner.nextLine();
                if (next.equalsIgnoreCase("exist")) {
                    ctx.channel().close();
                    break;
                }
                fileTransferUtil.sendStringMessage(ctx, next);
                if (next.equals("1")) {
                    try {
                        fileTransferUtil.readBytesFromFile(ctx, file);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        Pair<PayloadTypeEnum, byte[]> pair = fileTransferUtil.payloadTypeDecode(buf);
        PayloadTypeEnum payloadTypeEnum = pair.getLeft();
        byte[] bytes = pair.getRight();
        switch (payloadTypeEnum) {
            case TEXT -> printReceivedTextMessage(ctx, bytes);
            case FILE -> {
                try {
                    ProtoFilePackage.FilePackage filePackage = ProtoFilePackage.FilePackage.parseFrom(bytes);
                    fileTransferUtil.writeBytes2File(ctx, filePackage,CACHE_PATH);
                    super.channelRead(ctx, filePackage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            default -> log.error("Invalid payload type: {}", buf);
        }
        super.channelRead(ctx, buf);
    }

    private void printReceivedTextMessage(ChannelHandlerContext ctx, byte[] bytes) {
        String message = new String(bytes, StandardCharsets.UTF_8);
        if ("传输完成".equals(message)) {
            long time = System.currentTimeMillis() - startTime;
            String unit = " ms";
            if (time > 1000) {
                time /= 1000;
                unit = " s";
            }
            System.out.println("文件传输完成，用时:" + time + unit);
//            ctx.channel().close();
        } else if ("传输失败".equals(message)) {
            System.out.println("传输失败，请重新传输该文件！");
//            ctx.channel().close();
        } else {
            System.out.println("message = " + message);
        }
    }
}
