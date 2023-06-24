package socket.udptdemo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpReceiveMessage implements Runnable {
    DatagramSocket socket;

    public UdpReceiveMessage(int port) {
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
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
            byte[] buffer = new byte[1404];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
                //将数据包转换成字符串
                byte[] data = packet.getData();
                String address = packet.getAddress().getHostAddress();
                String receivedStr = new String(data,0, packet.getLength());
                System.out.println("[Received from]: " + address + ", [Received Message]:" + receivedStr);
                //收到类似bye或者88的对话时结束运行
                String endSign = receivedStr.toLowerCase();
                if (endSign.contains("bye") || endSign.contains("88"))
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
