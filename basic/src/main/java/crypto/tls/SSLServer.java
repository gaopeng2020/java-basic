package crypto.tls;

import crypto.ValidRsaCertificate;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author garden
 */
public class SSLServer {
    private static final String[] TLS_VERSIONS = new String[]{"TLSv1.3"};
   private static final String[] CIPHER_SUITES = new String[]{
           "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
           "TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256",
           "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256",
           "TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256",
           "TLS_RSA_WITH_AES_256_GCM_SHA384","TLS_RSA_WITH_AES_128_CBC_SHA",
           "TLS_AES_256_GCM_SHA384","TLS_AES_128_GCM_SHA256" //TLSv1.3
   };

    public static void main(String[] args) {
        String storePath = Objects.requireNonNull(SSLServer.class.getClassLoader().getResource("server.keystore")).getFile();
        String password = "123456";
        try {
            // Get the keystore
//            System.setProperty("javax.net.debug", "all");
            KeyStore keyStore = ValidRsaCertificate.getKeyStore(storePath, password);

            // KeyManagerFactory ()
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
            keyManagerFactory.init(keyStore, password.toCharArray());
            X509KeyManager x509KeyManager = null;
            for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
                if (keyManager instanceof X509KeyManager) {
                    x509KeyManager = (X509KeyManager) keyManager;
                    break;
                }
            }
            if (x509KeyManager == null) {
                throw new NullPointerException();
            }

            // set up the SSL Context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //由于客户端没有证书，因此服务端不需要TrustManagerFactory，设为null
            sslContext.init(new KeyManager[]{x509KeyManager}, null, null);

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
        } catch (IOException | NoSuchAlgorithmException | UnrecoverableKeyException |
                 NoSuchProviderException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }
}
