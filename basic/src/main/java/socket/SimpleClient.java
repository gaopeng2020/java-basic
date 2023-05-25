package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author garden
 * @create 8/12/18
 */
public class SimpleClient {
    public static void main(String[] args) {
        try {
            Socket kkSocket = new Socket("127.0.0.1", 8444);
            PrintWriter socketOut = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer, fromUser;

            while ((fromServer = socketIn.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye.")) {
                    break;
                }

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    socketOut.println(fromUser);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
