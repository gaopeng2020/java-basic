package crypto.certificate;

import crypto.keyagreement.DhKeyAgreementDemo;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author gaopeng
 */
public class ValidEccCertificate {
    public static void main(String[] args) throws Exception {
        InputStream storeStream = ValidEccCertificate.class.getResourceAsStream("/crypto/server.keystore");
        String alias = "server_ecc";
        String ksPassword = "123456";
        KeyStore ks = ValidRsaCertificate.getKeyStore(storeStream, ksPassword);
        System.out.println("证书验证是否通过：" + ValidRsaCertificate.verifyCertificate(ks.getCertificateChain(alias)));

        PrivateKey serverPrivateKey = ValidRsaCertificate.getPrivateKey(ks, alias, ksPassword);
        PublicKey serverPublicKey = ValidRsaCertificate.getPublicKey(ks, alias);

        //密钥协商
        //1. 客户端根据服务端的公钥生成自己的密钥对
        KeyPair clientKeyPair = DhKeyAgreementDemo.generateKeyPair(serverPublicKey.getEncoded());

        // 2.服务端根据自己的私钥和客户端的公钥生成对称密钥
        SecretKey secretKeyServer = DhKeyAgreementDemo.generateSecretKeyBySHA256(clientKeyPair.getPublic(), serverPrivateKey, "ECDH");

        // 3.客户端根据自己的私钥和服务端的公钥生成对称密钥
        SecretKey secretKeyClient = DhKeyAgreementDemo.generateSecretKeyBySHA256(serverPublicKey, clientKeyPair.getPrivate(), "ECDH");
        if (secretKeyServer.equals(secretKeyClient)) {
            System.out.println("Client/Server会话密钥协商成功,密钥为:" + Base64.encodeBase64String(secretKeyClient.getEncoded()));
        }

        //加密解密测试
        String message = "DH密钥协商算法简介测试...";
        byte[] data = message.getBytes(StandardCharsets.UTF_8);
        byte[] initIV = DhKeyAgreementDemo.initIV(16);
        //客户端加密数据
        byte[] encryptData = DhKeyAgreementDemo.encryption(secretKeyClient, initIV, data);
        //服务端解密数据
        byte[] plaintext = DhKeyAgreementDemo.decryption(secretKeyServer, initIV, encryptData);

        System.out.println("加密前数据：" + message);
        System.out.println("加密后的数据：" + Base64.encodeBase64String(encryptData));
        System.out.println("解密后的数据：" + new String(plaintext, StandardCharsets.UTF_8));

    }

}
