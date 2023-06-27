package tls;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author garden
 */
public class SSLTestClient {
    private static final String[] TLS_VERSIONS = new String[]{"TLSv1.3"};

    public static void main(String[] args) {
        try {
            // System.setProperty("javax.net.debug", "all");

            // set up the SSL Context
            SSLContext sslContext = SSLContextUtil.getClientSSLContext(TLS_VERSIONS[0]);

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket kkSocket = (SSLSocket) socketFactory.createSocket("localhost", 8333);
            kkSocket.setEnabledProtocols(TLS_VERSIONS);

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
