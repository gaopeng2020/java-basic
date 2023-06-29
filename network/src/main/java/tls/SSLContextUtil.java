package tls;

import lombok.Setter;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

public class SSLContextUtil {
    @Setter
    private static String password = "123456";
    @Setter
    private static String keystorePath = "/server.keystore";

    public static SSLEngine getServerSSLEngine(String tlsVersion) {
        SSLContext sslContext = getServerSSLContext(tlsVersion);
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(false);
        sslEngine.setNeedClientAuth(false);
        // Arrays.stream(sslEngine.getEnabledCipherSuites()).sequential().forEach(System.out::println);
        return sslEngine;
    }

    public static SSLEngine getClientSSLEngine(String tlsVersion) {
        SSLContext sslContext = getClientSSLContext(tlsVersion);
        SSLEngine sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
        return sslEngine;
    }

    public static SSLContext getServerSSLContext(String tlsVersion) {
        try {
            SSLContext sslContext = SSLContext.getInstance(tlsVersion);
            //SecureRandom 伪随机数生成器
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            //由于服务器不需要对客户端验证，因此服务端不需要TrustManagerFactory，TrustManager设为null
            sslContext.init(getX509KeyManagers(), null, null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public static SSLContext getClientSSLContext(String tlsVersion) {
        try {
            SSLContext sslContext = SSLContext.getInstance(tlsVersion);
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            //初始化SSLContext,三个参数分别是：可以导入的证书,信任管理器,SecureRandom) 三个参数都可为null
            // client没有证书不需要KeyManagerFactory，设为null
            sslContext.init(null, getX509TrustManagers(), null);
            return sslContext;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }


    public static KeyManager[] getX509KeyManagers() {
        try (InputStream storeStream = SSLTestServer.class.getResourceAsStream(keystorePath)) {
            //这里 目前一般是PKCS12, 之前默认是jks
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(storeStream, password.toCharArray());

            // KeyManagerFactory ()
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
            keyManagerFactory.init(keyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 UnrecoverableKeyException | NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    public static TrustManager[] getX509TrustManagers() {
        try (InputStream storeStream = SSLTestServer.class.getResourceAsStream(keystorePath)) {
            //这里 目前一般是PKCS12, 之前默认是jks
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(storeStream, password.toCharArray());

            // KeyManagerFactory ()
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("PKIX", "SunJSSE");
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();

        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException |
                 NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }
}
