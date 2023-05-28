package crypto.tls;

import crypto.certificate.ValidEccCertificate;
import crypto.certificate.ValidRsaCertificate;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;

/**
 * @author garden
 */
public class SSLClient {
    private static final String[] TLS_VERSIONS = new String[]{"TLSv1.3"};
    public static void main(String[] args) {
        InputStream storeStream = SSLClient.class.getResourceAsStream("/crypto/server.keystore");
        String password = "123456";

        try {
//            System.setProperty("javax.net.debug", "all");

            // TrustManagerFactory ()
            KeyStore trustStore =ValidRsaCertificate.getKeyStore(storeStream,password);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
            trustManagerFactory.init(trustStore);
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }
            if (x509TrustManager == null) {
                throw new NullPointerException();
            }

            // set up the SSL Context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //初始化SSLContext,三个参数分别是：可以导入的证书,信任管理器,SecureRandom) 三个参数都可为null，没有证书填写null就行
            // client没有证书不需要KeyManagerFactory，设为null
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket kkSocket = (SSLSocket) socketFactory.createSocket("192.168.10.28", 8333);
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
        } catch (IOException | NoSuchAlgorithmException |
                 NoSuchProviderException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
