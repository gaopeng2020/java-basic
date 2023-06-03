package socket.tcpdemo.FileTransfer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;

public class TcpFileTransferServer {
    static int port = 8085;
    static String function;

    public static void main(String[] args) {
        parseArguments(args);
        //create threadPool
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                3,
                16,
                60,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );

        //create ServerSocket and bind tcp port
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //get Directory path
        File dir = getSavedDirPath();
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                switch (function) {
                    case "Upload", "u", "U" -> {
                        String filePath = dir.getPath() +"\\"+ UUID.randomUUID().toString().replace("-", "");
                        threadPool.submit(new TcpFIleUploadRunnable(socket, dir.getPath()));
                    }
                    case "Download", "D", "d" -> {
                        File[] files = Objects.requireNonNull(dir.listFiles(pathname -> !pathname.isDirectory()));
                        threadPool.submit(new TcpFIleDownloadRunnable(socket, files));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static File getSavedDirPath() {
        File file = new File(System.getProperty("user.home"));
        File[] files = file.listFiles(pathname -> pathname.isDirectory() && pathname.getName().equals("Downloads"));
        if (files == null) {
            return new File(file, "Downloads");
        }
        return files[0];
    }

    /**
     * @brief parse input Arguments.
     */
    private static void parseArguments(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (!(args[i].equals("-h") || args[i].equals("-p") || args[i].equals("-f"))) {
                printHelpMessage();
                System.exit(1);
            } else if (args[i].equals("-h")) {
                printHelpMessage();
                System.exit(0);
            } else if (args[i].equals("-p") && (i + 1) < args.length) {
                port = Integer.valueOf(args[i + 1]);
                i++;
            } else if (args[i].equals("-f") && (i + 1) < args.length) {
                if (args[i + 1].equalsIgnoreCase("Upload") ||
                        args[i + 1].equals("Download")) {
                    function = args[i + 1];
                    i++;
                } else {
                    System.out.println("Invalid function. Only 'Upload' and 'Download' is supported!\n" +
                            "Options are: [Upload], [Download]");
                    System.exit(1);
                }
            }
        }
        // Check if port was set
        if (port == 8085) {
            System.out.println("Server will sse default 8085 TCP Port!");
        }
        // Check if algorithm was set
        if (!("Upload".equalsIgnoreCase(function) || "Download".equalsIgnoreCase(function))) {
            System.out.println("Invalid function. Only 'Upload' and 'Download' is supported!\n" +
                    "Options are: [Upload], [Download]");
            printHelpMessage();
            System.exit(1);
        }
    }

    /**
     * @brief Prints the help message on the console.
     */
    static private void printHelpMessage() {
        System.out.println(
                "Usage: TcpFileTransfer [-p <TCP port>] -f <function>\n" +
                        "-h                       Print this message and exit.\n" +
                        "-p <port>                [optional] Specify the TCP port.\n" +
                        "-f [Upload | Download]   Specify the function of TcpFileTransferServer.\n"
        );
    }
}
