package netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufUtil;

import java.nio.charset.StandardCharsets;
import java.util.Random;

@Slf4j
public class ProtoClientHandler extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                        1024, 0, 4, 0, 0))
                .addLast(new LoggingHandler(LogLevel.INFO))
                .addLast("ProtobufEncoder", new ProtobufEncoder())
                //Add InboundHandler
                .addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        sendProtoDataTypeRandomly(ctx);
//                        ctx.channel().close();
                        super.channelActive(ctx);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        byte[] bytes = ByteBufUtil.lengthFieldDecode((ByteBuf) msg);
                        // String decode = new String(Base64.getDecoder().decode(bytes));
                        String decode = new String(bytes, StandardCharsets.UTF_8);
                        log.info("Server echo: {}", decode);
                        super.channelRead(ctx, msg);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        super.exceptionCaught(ctx, cause);
                    }
                })
                //Add OutboundHandler
                .addLast(new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        //为所有出栈的字节数据添加上length field，用于解决TCP粘包与半包问题
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

    private void sendProtoDataTypeRandomly(@NonNull ChannelHandlerContext ctx) {
        int random = new Random().nextInt(4);
        System.out.println("random = " + random);
        ProtoDataTypes.ProtoDataType dataType = null;
        if (random == 0) {
            dataType = ProtoDataTypes.ProtoDataType.newBuilder()
                    .setSelector(ProtoDataTypes.ProtoDataType.DataTypeSelectorEnum.StudentType)
                    .setStudent(ProtoDataTypes.Student.newBuilder().setName("Student").setAge(18).build())
                    .build();

        } else if (random == 1) {
            dataType = ProtoDataTypes.ProtoDataType.newBuilder()
                    .setSelector(ProtoDataTypes.ProtoDataType.DataTypeSelectorEnum.WorkerType)
                    .setWorker(ProtoDataTypes.Worker.newBuilder().setName("Worker").setAge(28).build())
                    .build();

        } else if (random == 2) {
            dataType = ProtoDataTypes.ProtoDataType.newBuilder()
                    .setSelector(ProtoDataTypes.ProtoDataType.DataTypeSelectorEnum.FarmerType)
                    .setFarmer(ProtoDataTypes.Farmer.newBuilder().setName("Farmer").setAge(38).build())
                    .build();

        } else {
            log.error("Not supported random number:" + random);
            return;
        }
        ctx.channel().writeAndFlush(dataType.toByteArray());
    }
}
