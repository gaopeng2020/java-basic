package crypto.tls;

import crypto.ValidRsaCertificate;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Objects;

/**
 * @author garden
 * @create 8/11/18
 */
public class SSLClient {
    public static void main(String[] args) {
        String storePath = Objects.requireNonNull(ValidRsaCertificate.class.getClassLoader().getResource("server.keystore")).getFile();
        String alias = "server_rsa";
        String password = "123456";

        try {
//            System.setProperty("javax.net.debug", "all");
//            KeyStore keyStore = KeyStore.getInstance("PKCS12");
//            String password = "aabbcc";
//            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("client/certificate-client.p12");
//            keyStore.load(inputStream, password.toCharArray());

            // KeyManagerFactory ()
            System.setProperty("javax.net.debug", "all");
            KeyStore keyStore = ValidRsaCertificate.getKeyStore(storePath, password);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
            keyManagerFactory.init(keyStore, password.toCharArray());
            X509KeyManager x509KeyManager = null;
            for (KeyManager keyManager : keyManagerFactory.getKeyManagers()) {
                if (keyManager instanceof X509KeyManager) {
                    x509KeyManager = (X509KeyManager) keyManager;
                    break;
                }
            }
            if (x509KeyManager == null) throw new NullPointerException();

            // TrustManagerFactory ()
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            String password2 = "abcdefg";
            InputStream inputStream1 = ClassLoader.getSystemClassLoader().getResourceAsStream("server/certificate-server.p12");
            trustStore.load(inputStream1, password2.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
            trustManagerFactory.init(trustStore);
            X509TrustManager x509TrustManager = null;
            for (TrustManager trustManager : trustManagerFactory.getTrustManagers()) {
                if (trustManager instanceof X509TrustManager) {
                    x509TrustManager = (X509TrustManager) trustManager;
                    break;
                }
            }

            if (x509TrustManager == null) throw new NullPointerException();

            // set up the SSL Context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            //初始化SSLContext,三个参数分别是：可以导入的证书,信任管理器,SecureRandom) 三个参数都可为null，没有证书填写null就行
            sslContext.init(new KeyManager[]{x509KeyManager}, new TrustManager[]{x509TrustManager}, null);

            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLSocket kkSocket = (SSLSocket) socketFactory.createSocket("127.0.0.1", 8333);
            kkSocket.setEnabledProtocols(new String[]{"TLSv1.2"});

            PrintWriter socketOut = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

            String fromServer, fromUser;
            while ((fromServer = socketIn.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    socketOut.println(fromUser);
                }
            }
        } catch (IOException | CertificateException | NoSuchAlgorithmException | UnrecoverableKeyException |
                 NoSuchProviderException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
