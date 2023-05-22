package crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author gaopeng
 */
public class ValidCertificate {
    public static void main(String[] args) throws Exception {
//        String storePath = ClassLoader.getSystemResource("src/main/resources/server.jks").getPath();
        String storePath = "C:\\Users\\Public\\Documents\\gitlab\\java\\basic\\src\\main\\resources\\server.jks";
        System.out.println(storePath);

        String password = "123456";
        KeyStore ks = getKeyStore(storePath, password);
        boolean verify = certificateVerify(ks.getCertificate("server_rsa"));
        System.out.println("证书验证是否通过："+verify);

        PrivateKey privKey = getPrivateKey(ks, "server_rsa", password, password);
        PublicKey pubKey = getPublicKey(ks, "server_rsa", password);

        String strData = "加密前的数据";
        System.out.println("加密前数据：" + strData);

        byte[] encryptedData = encrypt(strData, pubKey);
        System.out.println("加密后的数据：" + Base64.encodeBase64String(encryptedData));

        byte[] decryptedData = decrypt(encryptedData, privKey);
        assert decryptedData != null;
        System.out.println("解密后的数据：" + new String(decryptedData));

        String strSignData = "签名前的数据";
        byte[] toSignData = strSignData.getBytes(StandardCharsets.UTF_8);
        byte[] signedData = sign(toSignData, privKey);
//        System.out.println("签名后的摘要信息：" + Arrays.toString(signedData));
        if (verify(toSignData, signedData, pubKey)) {
            System.out.println("验签通过");
        } else {
            System.out.println("验签错误");
        }
    }

    private static boolean certificateVerify(Certificate certificate) {
        boolean flag=false;
        PublicKey publicKey = certificate.getPublicKey();
        try {
            certificate.verify(publicKey);
            flag = true;
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException |
                 SignatureException e) {
            throw new RuntimeException(e);
        }
        return flag;
    }

    /**
     * 通过路径获取keystore
     */
    public static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
        FileInputStream is = new FileInputStream(keyStorePath);
        //这里 目前一般是PKCS12, 之前默认是jks
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }


    /**
     * 通过路径获取私钥
     */
    private static PrivateKey getPrivateKey(KeyStore keyStore, String alias, String storePass, String keyPass) throws Exception {
        PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());
        //返回私钥字符串
        String privateKey = Base64.encodeBase64String(privKey.getEncoded());
        System.out.println("私钥字符串：" + privateKey);
        return privKey;
    }

    /**
     * 通过路径获取公钥
     */
    private static PublicKey getPublicKey(KeyStore keyStore, String alias, String storePass) throws Exception {
        Certificate certificate = keyStore.getCertificate(alias);
        PublicKey pubKey = certificate.getPublicKey();
        BigInteger serialNumber = ((X509Certificate) certificate).getSerialNumber();
        String keyId = serialNumber.toString();
        System.out.println("证书ID:" + keyId);
        //返回公钥字符串
        String strPubKey = Base64.encodeBase64String(pubKey.getEncoded());
        System.out.println("公钥字符串：" + strPubKey);
        return pubKey;
    }

    /**
     * 从str中获取公私钥
     */
    private static PublicKey getPublicKeyFromStr(String pubKey) throws Exception {
        byte[] pk = Base64.decodeBase64(pubKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(pk);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    private static PrivateKey getPrivateKeyFromStr(String strPrivateKey) throws Exception {
        byte[] pk = Base64.decodeBase64(strPrivateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pk);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * 用公钥加密
     */
    private static byte[] encrypt(String data, Key pubKey) {
        try {
            Cipher cipher = Cipher.getInstance(pubKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 用私钥解密
     */
    private static byte[] decrypt(byte[] encryptedData, Key privKey) {
        try {
            Cipher cipher = Cipher.getInstance(privKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 签名
     */
    private static byte[] sign(byte[] data, PrivateKey privKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(data);
        byte[] digest = messageDigest.digest();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privKey);
        signature.update(digest);
        byte[] sign = signature.sign();
        return Base64.encodeBase64(sign);
    }

    /**
     * 验签
     */
    private static boolean verify(byte[] data, byte[] sign, PublicKey pubKey) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(data);
        byte[] digested = digest.digest();
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(pubKey);
        signature.update(digested);
        return signature.verify(Base64.decodeBase64(sign));
    }
}
