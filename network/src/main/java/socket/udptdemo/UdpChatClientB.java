package socket.udptdemo;

public class UdpChatClientB {
    public static void main(String[] args) {
        //启动Udp端口号准备接收消息
        new Thread(new UdpReceiveMessage(8888)).start();
        //启动Udp端口号准备发送消息
        new Thread(new UdpSendMessage("127.0.0.1",9999)).start();
    }
}
