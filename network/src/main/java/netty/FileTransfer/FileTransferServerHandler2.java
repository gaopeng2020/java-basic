package netty.FileTransfer;

import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufUtil;
import netty.utils.PayloadTypeEnum;
import org.graalvm.collections.Pair;

import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class FileTransferServerHandler2 extends ChannelInboundHandlerAdapter {
    private long startTime;
    private final static ExecutorService workerThreadService = Executors.newFixedThreadPool(6);
    private final FileTransferCommonMethods fileTransferUtil;
    public FileTransferServerHandler2() {
        fileTransferUtil = new FileTransferCommonMethods();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String functions = """
                You can directly start test server string echo function;
                Response 1 to start file upload function.
                Response 2 to start file download function.
                more functions are in developing...
                """;
        fileTransferUtil.sendStringMessage(ctx,functions);
        super.channelActive(ctx);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        Pair<PayloadTypeEnum, byte[]> pair = fileTransferUtil.payloadTypeDecode(buf);
        PayloadTypeEnum payloadTypeEnum = pair.getLeft();
        byte[] bytes = pair.getRight();

        switch (payloadTypeEnum) {
            case TEXT -> {
                String message = ByteBufUtil.stringDecode(bytes);
                String downloadFileList = fileTransferUtil.getDownloadFiles();
                if (message.equals("2")) {
                    message = "Please response file name of bellow: \n" + downloadFileList;
                } else if (downloadFileList.contains(message)) {
                    String finalMessage = message;
                    workerThreadService.submit(() -> {
                        try {
                            fileTransferUtil.readBytesFromFile(ctx, fileTransferUtil.getFieByName(finalMessage));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                String echoMessage = "[echo] " + message;
                ByteBuf byteBuf = fileTransferUtil.payloadTypeEncode(ByteBufUtil.stringEncode(echoMessage), PayloadTypeEnum.TEXT);
                ctx.writeAndFlush(byteBuf);
                super.channelRead(ctx, message);
            }
            case FILE ->
                //写入文件非常耗时用，最好在自己的线程池中处理该任务，防止netty io阻塞。
                    workerThreadService.submit(() -> {
                        try {
                            ProtoFilePackage.FilePackage filePackage = ProtoFilePackage.FilePackage.parseFrom(bytes);
                            fileTransferUtil.writeBytes2File(ctx, filePackage);
                        } catch (InvalidProtocolBufferException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        // buf.release(); buf可以在trail handler中自动释放
        super.channelRead(ctx, buf);
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

}
