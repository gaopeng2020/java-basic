package netty.protobufdemo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import netty.utils.ByteBufUtil;

import java.nio.charset.StandardCharsets;

@Slf4j
public class ProtoServerHandler extends ChannelInitializer<NioSocketChannel> {
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //Add ProtobufDecoder handler
        pipeline.addLast(new LengthFieldBasedFrameDecoder(
                        1024, 0, 4, 0, 0))
                //Add Inbound Handler
                .addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        initMessages(ctx);
                        super.channelActive(ctx);
                    }

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        //先解析出ProtoDataTypes.ProtoDataType，然后交给ProtobufDecoder解析出对象
                        byte[] bytes = ByteBufUtil.lengthFieldDecode((ByteBuf) msg);
                        ProtoDataTypes.ProtoDataType dataType = ProtoDataTypes.ProtoDataType.parseFrom(bytes);
                        super.channelRead(ctx, dataType);
                    }

                    @Override
                    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                        super.exceptionCaught(ctx, cause);
                    }
                })
                //进入ProtobufDecoder解析出对象
                .addLast("ProtobufDecoder", new ProtobufDecoder(ProtoDataTypes.ProtoDataType.getDefaultInstance()))
                //添加一个Handler将对象打印出来
                .addLast(new SimpleChannelInboundHandler<ProtoDataTypes.ProtoDataType>() {
                    @Override
                    protected void channelRead0(ChannelHandlerContext ctx, ProtoDataTypes.ProtoDataType msg) throws Exception {
                        resolveProtoDataTypes(msg);
                    }
                })
                //Add Out bound Handler
                .addLast(new ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                        if (msg instanceof byte[]) {
                            ByteBuf buf = ByteBufUtil.lengthFieldEncode((byte[]) msg);
                            super.write(ctx, buf, promise);
                        } else {
                            super.write(ctx, msg, promise);
                        }
                    }
                });
    }

    private void initMessages(ChannelHandlerContext ctx) {
        String msg = "Server支持字符串以及ProtoDataTypes.ProtoDataType的解析";
        // byte[] encode = Base64.getEncoder().encode(msg.getBytes());
        byte[] encode = msg.getBytes(StandardCharsets.UTF_8);
        ctx.channel().writeAndFlush(encode);
    }

    private void resolveProtoDataTypes(@NonNull ProtoDataTypes.ProtoDataType protoDataType) {
        ProtoDataTypes.ProtoDataType.DataTypeSelectorEnum selector = protoDataType.getSelector();
        switch (selector) {
            case StudentType -> {
                ProtoDataTypes.Student student = protoDataType.getStudent();
                log.info("student name: {}, student age: {}", student.getName(), student.getAge());
            }
            case WorkerType -> {
                ProtoDataTypes.Worker worker = protoDataType.getWorker();
                log.info("worker name: {}, worker age: {}", worker.getName(), worker.getAge());
            }
            case FarmerType -> {
                ProtoDataTypes.Farmer farmer = protoDataType.getFarmer();
                log.info("farmer name: {}, farmer age: {}", farmer.getName(), farmer.getAge());
            }
            default -> log.error("DataType not support");
        }
    }
}
