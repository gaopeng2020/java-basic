package crypto.tls;

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
            Socket clientSocket = serverSocket.accept();
            PrintWriter socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            KnockKnockProtocol kkp = new KnockKnockProtocol();
            String outputLine = kkp.processInput(null);
            socketOut.println(outputLine);

            String inputLine;
            while ((inputLine = socketIn.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                socketOut.println(outputLine);
                if (inputLine.equals("Bye."))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
