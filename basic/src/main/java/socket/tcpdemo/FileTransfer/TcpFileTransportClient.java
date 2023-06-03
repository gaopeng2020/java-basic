package socket.tcpdemo.FileTransfer;

import java.io.*;
import java.net.Socket;

public class TcpFileTransportClient {
    static Socket socket;

    public static void connect2Server(String ipAddress, int port) {
        try {
            socket = new Socket(ipAddress, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void disConnect2Server() {
        if (socket != null) {
            try {
                if (!socket.isOutputShutdown()) {
                    socket.shutdownOutput();
                }
                if (!socket.isInputShutdown()) {
                    socket.shutdownInput();
                }
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void uploadFile(String path) {
        File file = new File(path);
        try (FileInputStream fis = new FileInputStream(file)) {
            OutputStream os = socket.getOutputStream();

            //transfer file name
            DataOutputStream out = new DataOutputStream(os);
            System.out.println("file.getName() = " + file.getName());
            out.writeUTF(file.getName());

            //transfer file
            byte[] buffer = new byte[65536];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            socket.shutdownOutput();//取消socket输出阻塞

            //receive result of uploading
            InputStream is = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1)
                baos.write(buffer, 0, len);
            System.out.println("[Server]: " + baos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void downloadFile(String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[65535];
            int len;
            while ((len = is.read(buffer)) != -1)
                fos.write(buffer, 0, len);

            //notify client result of Uploading
            socket.getOutputStream().write("Upload File Successfully".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //uploadFile 和 downloadFile方法每次只能运行一个
        connect2Server("127.0.0.1", 8085);
        String uploadPath = "C:\\Users\\lenovo\\Desktop\\Test\\晶能-招标材料.zip";
        uploadFile(uploadPath);

//        String downloadPath = "C:\\Users\\lenovo\\Desktop\\Test\\lx-music.7z";
//        downloadFile(downloadPath);
        disConnect2Server();
    }

}
