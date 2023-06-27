package tls;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author garden
 */
public class SSLTestServer {
    private static final String[] TLS_VERSIONS = new String[]{"TLSv1.3"};
    private static final String[] CIPHER_SUITES = new String[]{
            "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
            "TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256",
            "TLS_RSA_WITH_AES_256_GCM_SHA384", "TLS_RSA_WITH_AES_128_CBC_SHA",
            "TLS_AES_256_GCM_SHA384", "TLS_AES_128_GCM_SHA256" //TLSv1.3
    };

    public static void main(String[] args) {
        try {
            // System.setProperty("javax.net.debug", "all");

            //获取SSLContext
            SSLContext sslContext = SSLContextUtil.getServerSSLContext(TLS_VERSIONS[0]);

            SSLServerSocketFactory serverSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket serverSocket = (SSLServerSocket) serverSocketFactory.createServerSocket(8333);
            serverSocket.setNeedClientAuth(false);
            serverSocket.setEnabledProtocols(TLS_VERSIONS);
            serverSocket.setEnabledCipherSuites(CIPHER_SUITES);
            Arrays.stream(serverSocket.getEnabledCipherSuites()).sequential().forEach(System.out::println);
            SSLSocket socket = (SSLSocket) serverSocket.accept();

            // InputStream and OutputStream Stuff
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //get input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;

            // Initiate conversation with client
            KnockKnockProtocol kkp = new KnockKnockProtocol();
            outputLine = kkp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = kkp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Bye.")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
