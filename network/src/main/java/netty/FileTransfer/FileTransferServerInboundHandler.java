package netty.FileTransfer;

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
public class FileTransferServerInboundHandler extends ChannelInboundHandlerAdapter {
    private long startTime;
    private final static ExecutorService workerThreadService = Executors.newFixedThreadPool(6);
    private final FileTransferCommonMethods fileTransferUtil;
    private final static String CACHE_PATH = "ServerCache";

    public FileTransferServerInboundHandler() {
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
        fileTransferUtil.sendStringMessage(ctx, functions);
        /*ByteBuf buf = fileTransferUtil.payloadTypeEncode(ByteBufUtil.stringEncode(functions), PayloadTypeEnum.TEXT);
        ctx.channel().writeAndFlush(buf);*/
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
                clientInteractiveHandler(ctx, message);

                super.channelRead(ctx, message);
            }
            case FILE -> {
                //写入文件非常耗时用，最好在自己的线程池中处理该任务，防止netty io阻塞,经测试使用自定义的任务池会导致文件MD校验不通过，暂不使用。
                //   workerThreadService.submit(() -> {
                try {
                    ProtoFilePackage.FilePackage filePackage = ProtoFilePackage.FilePackage.parseFrom(bytes);
                    fileTransferUtil.writeBytes2File(ctx, filePackage, CACHE_PATH);
                    super.channelRead(ctx, filePackage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                //  });
            }
            default -> log.error("不支持的PayloadType:{}", payloadTypeEnum);
        }
        // buf.release(); buf可以在trail handler中自动释放
        super.channelRead(ctx, buf);
    }

    private void clientInteractiveHandler(ChannelHandlerContext ctx, String message) {
        String downloadFileList = fileTransferUtil.getDownloadFiles("ServerCache");
        if (message.equals("2")) {
            message = "[echo] " + "Please response a file name within bellow list: \n" + downloadFileList;
            fileTransferUtil.sendStringMessage(ctx, message);
        } else if (downloadFileList.contains(message)) {
            String finalMessage = message;
            workerThreadService.submit(() -> {
                try {
                    fileTransferUtil.readBytesFromFile(ctx, fileTransferUtil.getFieByName(finalMessage, CACHE_PATH));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            String echoMessage = "[echo] " + message;
            fileTransferUtil.sendStringMessage(ctx, echoMessage);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SocketAddress socketAddress = ctx.channel().remoteAddress();
        System.out.println(socketAddress + " channelInactive...");
        ctx.channel().close();
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelUnregistered...");
        ctx.channel().close();
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelRegistered...");
        super.channelRegistered(ctx);
    }

}
