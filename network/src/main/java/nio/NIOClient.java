package nio;

import socket.tcpdemo.FileTransfer.FileTransferStruct;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Scanner;

public class NIOClient {
    public static void main(String[] args) throws Exception {
        //获取通道，并绑定远程服务器Socket
        SocketChannel channel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
        //切换到非阻塞模式
        channel.configureBlocking(false);
        while (!channel.finishConnect()) {
            System.out.println("客户端正在连接中，请耐心等待");
        }

        //创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate(65535);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            //写入数据到Buffer
            buffer.put((new Date() +"----->"+ next).getBytes());
            //将写模式转变为读模式
            buffer.flip();
            //将Buffer写入到channel
            channel.write(buffer);
            buffer.clear();

            //read message received
            channel.read(buffer);
            String msg = new String(buffer.array(), 0, buffer.position());
            System.out.println("[Server] : " + msg);
        }

//        sendFiles(channel, buffer);

        //关闭channel
        channel.close();
    }

    private static void sendFiles(SocketChannel channel, ByteBuffer writeBuffer) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        //read file contents
        File file = new File("C:\\Users\\lenovo\\Desktop\\Test\\java-dtls-master.zip");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] allBytes = fileInputStream.readAllBytes();

        //构造FileTransferStruct对象
        FileTransferStruct fileTransferStruct = new FileTransferStruct();
        fileTransferStruct.filename = file.getName();
        fileTransferStruct.fileSize = file.length();
        System.out.println("file.length() = " + file.length());
        fileTransferStruct.fileContent = allBytes;
        //将对象写入oos
        oos.writeObject(fileTransferStruct);
        oos.writeObject(null);
        oos.flush();

        //将序列化对象发送出去
        byte[] bytes = baos.toByteArray();
        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        channel.write(wrap);

//        int limit = writeBuffer.limit();
//        System.out.println("limit = " + limit);
//        int count = bytes.length / limit;
//        System.out.println("count = " + count);
//        for (int i = 0; i <= count; i++) {
//            System.out.println("i = " + i);
//            writeBuffer.put(bytes,0,limit);
//            writeBuffer.flip();
//            channel.write(writeBuffer);
//            writeBuffer.clear();
//        }

    }
}
