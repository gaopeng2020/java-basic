package socket.tcpdemo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class TcpFileTransportServer {
    static ServerSocket serverSocket;
    static Socket socket;
    public TcpFileTransportServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            socket = serverSocket.accept();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void downloadFile (Socket socket, String file){
        try (FileInputStream fis = new FileInputStream(file)) {
            OutputStream os = socket.getOutputStream();
            PrintWriter bos = new PrintWriter(os);
            byte[] buffer = new byte[65536];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            bos.println("");
            socket.shutdownOutput(); //取消socket输出阻塞

            //receive result of uploading
            InputStream is = socket.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len=is.read(buffer))!=-1)
                baos.write(buffer,0,len);
            System.out.println("[Client]: " + baos);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(Socket socket, String filePath){
        try(FileOutputStream fos = new FileOutputStream(filePath)) {
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[65535];
            int len;
            while ((len=is.read(buffer))!=-1)
                fos.write(buffer,0,len);

            //notify client result of Uploading
            socket.getOutputStream().write("Upload File Successfully".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String downloadPath = Objects.requireNonNull(TcpFileTransportServer.class.getClassLoader()
                .getResource("SocketTest/lx-music.7z")).getFile();
        String uploadPath = "C:\\Users\\lenovo\\Documents\\gitlab\\java\\basic\\src\\main\\resources\\SocketTest\\upload.zip";
        try {
            serverSocket = new ServerSocket(8899);
            socket = serverSocket.accept();
            uploadFile(socket,uploadPath);
            downloadFile(socket,downloadPath);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
