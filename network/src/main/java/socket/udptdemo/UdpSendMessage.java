package socket.udptdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class UdpSendMessage implements Runnable {
    DatagramSocket socket;
    BufferedReader reader;
    InetSocketAddress socketAddress;

    public UdpSendMessage(String receiverIp, int receiverPort) {
        //UDP数据发送端口
        try {
            socket = new DatagramSocket(0);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        //构建接收方socketAddress
        socketAddress = new InetSocketAddress(receiverIp, receiverPort);
        //从键盘接受输入
        reader = new BufferedReader(new InputStreamReader(System.in));

        //发送初始消息
        byte[] bytes = "你好!".getBytes();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, socketAddress);
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (true) {
            try {
                byte[] inputData = reader.readLine().getBytes(StandardCharsets.UTF_8);
                DatagramPacket packet = new DatagramPacket(inputData, inputData.length, socketAddress);
                socket.send(packet);
                String endSign = new String(inputData).toLowerCase();
                if (endSign.contains("bye") || endSign.contains("8"))
                    break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (socket != null) {
            socket.close();
        }
    }
}
