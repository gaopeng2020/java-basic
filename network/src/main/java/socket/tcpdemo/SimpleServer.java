package socket.tcpdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author garden
 * @create 8/12/18
 */
public class SimpleServer {
    public static void main(String[] agrs) {
        try {
            ServerSocket serverSocket = new ServerSocket(8444);
            Socket socket = serverSocket.accept();
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String fromClient,fromUser;
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            fromUser = kkp.processInput(null);
            socketOut.println(fromUser);

            while ((fromClient = socketIn.readLine()) != null) {
                System.out.println("Client: " + fromClient);
                if (fromClient.equals("Bye."))
                    break;

                fromUser = kkp.processInput(fromClient);
                socketOut.println(fromUser);

//                fromUser = stdIn.readLine();
//                if (fromUser != null) {
//                    socketOut.println(fromUser);
//                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
