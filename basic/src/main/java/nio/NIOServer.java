package nio;

import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import socket.tcpdemo.FileTransfer.FileTransferStruct;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NIOServer {
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws Exception {
        //开启服务端socket通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //设置成非阻塞
        serverSocketChannel.configureBlocking(false);
        //绑定端口号
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));

        //开启selector,并注册accept事件
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //创建Buffer
        ByteBuffer readBuffer = ByteBuffer.allocate(65536);
        ByteBuffer writeBuffer = ByteBuffer.allocate(65536);

        //循环等待输入
        while (selector.select() > 0) {
            //遍历selectionKeys
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                //从iterator中获取一个key,并从Iterator中移除掉当前的selectionKey
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                //获取socket连接状态
                if (selectionKey.isAcceptable()) {
                    try {
                        SocketChannel channel = serverSocketChannel.accept();
                        System.out.println("channel = " + channel);
                        channel.configureBlocking(false);
                        //将socketChannel注册到selector
                        channel.register(selector, SelectionKey.OP_READ);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (selectionKey.isReadable()) {
                    receiveStringMessage(selectionKey, readBuffer);
//                    receiveFiles(selectionKey, readBuffer);

                } else if (selectionKey.isWritable()) {
                    String message = "Please input continuously,or close socket!";
                    sendStringMessage(selectionKey, writeBuffer, message);
                    //sendFiles(selectionKey, writeBuffer);
                }
            }
        }
        serverSocketChannel.close();
    }

    private static void sendFiles(SelectionKey selectionKey, ByteBuffer writeBuffer) throws IOException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        byte[] bytes = baos.toByteArray();

        //read file contents
        File file = new File("C:\\Users\\lenovo\\Desktop\\Test\\晶能-招标材料.zip");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] allBytes = fileInputStream.readAllBytes();

        //构造FileTransferStruct对象
        FileTransferStruct fileTransferStruct = new FileTransferStruct();
        fileTransferStruct.filename = file.getName();
        fileTransferStruct.fileSize = file.length();
        fileTransferStruct.fileContent = allBytes;

        oos.writeObject(fileTransferStruct);
        oos.flush();
        writeBuffer.put(bytes);
        writeBuffer.flip();

        channel.write(writeBuffer);
    }

    private static void receiveFiles(SelectionKey selectionKey, ByteBuffer readBuffer) throws IOException, ClassNotFoundException {
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        //get Directory path
        File dir = getSavedDirPath();
        readBuffer.clear();

        //从socket中读取数据到ByteArrayBuilder
        int len;
        ByteArrayBuilder builder = new ByteArrayBuilder();
        while ((len = channel.read(readBuffer)) > 0) {
            readBuffer.flip();
            System.out.println("readBuffer.limit() = " + readBuffer.limit());
            System.out.println("readBuffer.position() = " + readBuffer.position());
            System.out.println("len = " + len);

            byte[] bytes = new byte[readBuffer.limit()];
            readBuffer.get(bytes, 0, len);
            builder.write(bytes);
            readBuffer.clear();
        }
        //将数据封装成ObjectInputStream
        System.out.println("readBuffer.limit() = " + readBuffer.limit());
        ByteArrayInputStream bais = new ByteArrayInputStream(builder.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        FileTransferStruct obj = (FileTransferStruct) ois.readObject();
        String filename = obj.filename;
        byte[] content = obj.fileContent;
        String md5Digest = obj.md5Digest;
        long fileSize = obj.fileSize;

        //write to file
        FileOutputStream fos = new FileOutputStream(dir + "/" + filename);
        fos.write(content);
        fos.flush();

        //verify file


        fos.close();
        ois.close();
        bais.close();
    }

    private static String receiveStringMessage(SelectionKey selectionKey, ByteBuffer readBuffer) throws IOException {
        readBuffer.clear();
        StringBuilder stringBuffer = new StringBuilder();
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        try {
            while (channel.read(readBuffer) > 0) {
                String message = new String(readBuffer.array(), 0, readBuffer.position());
                stringBuffer.append(message);
                readBuffer.clear();
            }
        } catch (IOException e) {
            selectionKey.cancel();
            channel.close();
            e.printStackTrace();
        }
        selectionKey.interestOps(SelectionKey.OP_WRITE);
        System.out.println("[" + channel.getRemoteAddress() + "]" + stringBuffer);
        return stringBuffer.toString();
    }

    private static void sendStringMessage(SelectionKey selectionKey, ByteBuffer writeBuffer, String message) throws IOException {
        writeBuffer.rewind();
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        writeBuffer.put(message.getBytes());
        try {
            channel.write(writeBuffer);
        } catch (IOException e) {
            selectionKey.cancel();
            channel.close();
            e.printStackTrace();
        }
        selectionKey.interestOps(SelectionKey.OP_READ);
    }

    private static boolean isFileName(ByteBuffer byteBuffer, String fileNameFlag) {
        int len = 10;
        if (byteBuffer.position() < len || byteBuffer.position() > 512) {
            return false;
        }
        String decode = new String(CHARSET.decode(ByteBuffer.wrap(byteBuffer.array(), 0, len)).array());

        return decode.equals(fileNameFlag);
    }

    private static File getSavedDirPath() {
        File file = new File(System.getProperty("user.home"));
        File[] files = file.listFiles(pathname -> pathname.isDirectory() && pathname.getName().equals("Downloads"));
        if (files == null) {
            return new File(file, "Downloads");
        }
        return files[0];
    }
}
