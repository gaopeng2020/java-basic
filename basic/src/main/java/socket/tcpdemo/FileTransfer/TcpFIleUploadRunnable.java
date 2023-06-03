package socket.tcpdemo.FileTransfer;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class TcpFIleUploadRunnable implements Runnable {
    Socket socket;
    String dirPath;

    public TcpFIleUploadRunnable(Socket socket, String dirPath) {
        this.socket = socket;
        this.dirPath = dirPath;
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
        uploadFile(socket,dirPath);
    }

    public static void uploadFile(Socket socket,String dirPath) {
        try (InputStream is = socket.getInputStream();
             DataInputStream dis = new DataInputStream(is)) {
            //get file name
            String file = dirPath + "\\" + dis.readUTF();
            System.out.println("file = " + file);

            //get file content from socket
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[65535];
            int len;
            while ((len = is.read(buffer)) != -1)
                fos.write(buffer, 0, len);

            //notify client result of Uploading
            socket.getOutputStream().write("Upload File Successfully".getBytes());

            //close FileOutputStream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
