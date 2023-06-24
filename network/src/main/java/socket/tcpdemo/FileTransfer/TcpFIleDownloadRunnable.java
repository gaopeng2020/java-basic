package socket.tcpdemo.FileTransfer;

import java.io.*;
import java.net.Socket;

public class TcpFIleDownloadRunnable implements Runnable {
    Socket socket;
    File[] files;

    public TcpFIleDownloadRunnable(Socket socket, File[] files) {
        this.socket = socket;
        this.files = files;
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
        downloadFile(this.socket, this.files);
    }

    public static void downloadFile(Socket socket, File[] files) {
        for (File file : files) {
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
                while ((len = is.read(buffer)) != -1)
                    baos.write(buffer, 0, len);
                System.out.println("[Client]: " + baos);

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
}
