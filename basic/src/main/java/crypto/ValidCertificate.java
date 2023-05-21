package crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

public class ValidCertificate {
    public static void main(String[] args) throws Exception {
        String storePath = ClassLoader.getSystemResource("keys/bo.keystore").getPath();
        System.out.println(storePath);

        PrivateKey privKey = getPrivateKey(storePath, "www.bo.org", "123456", "123456");
        PublicKey pubKey = getPublicKey(storePath, "www.bo.org", "123456");

        /*String strData = "加密前的数据";
        System.out.println("加密前数据：" + strData);

        String strEnc = encrypt(strData, pubKey);
        System.out.println("加密后的数据：" + strEnc);

        String strDec = decrypt(strEnc, privKey);
        System.out.println("解密后的数据：" + strDec);*/

        String strSignData = "签名前的数据";
        byte[] byteSignData = strSignData.getBytes(StandardCharsets.UTF_8);
        String signData = Arrays.toString(sign(byteSignData, privKey));
        System.out.println("签名后的摘要信息：" + signData);
        if (verify(byteSignData, signData, pubKey)) {
            System.out.println("验签通过");
        } else {
            System.out.println("验签错误");
        }
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
    private static PrivateKey getPrivateKey(String keyStorePath, String alias, String storePass, String keyPass) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, storePass);
        PrivateKey privKey = (PrivateKey) ks.getKey(alias, keyPass.toCharArray());
        //返回私钥字符串
        String privateKey = Base64.encodeBase64String(privKey.getEncoded());
        System.out.println("私钥字符串：" + privateKey);
        return privKey;
    }

    /**
     * 通过路径获取公钥
     */
    private static PublicKey getPublicKey(String keyStorePath, String alias, String storePass) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, storePass);
        Certificate certificate = ks.getCertificate(alias);
        PublicKey pubKey = certificate.getPublicKey();
        BigInteger serialNumber = ((X509Certificate)certificate).getSerialNumber();
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
    private static String encrypt(String data, Key pubKey) {
        String encData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
//            encData = Base64.decodeBase64(encrypted).replace("\r", "").replace("\n", "");
            encData = Arrays.toString(Base64.decodeBase64(encrypted));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encData;
    }

    /**
     * 用私钥解密
     */
    private static String decrypt(String data, Key privKey) {
        String decData = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(data));
            decData = new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decData;
    }

    /**
     * 签名
     */
    private static byte[] sign(byte[] data, PrivateKey privKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(data);
        byte[] sha256 = messageDigest.digest();
        //Signature signature = Signature.getInstance("SHA256withDSA");
        Signature signature = Signature.getInstance("NONEwithRSA");
        signature.initSign(privKey);
        signature.update(sha256);
        byte[] sign = signature.sign();
        return Base64.encodeBase64(sign);
    }

    /**
     * 验签
     */
    private static boolean verify(byte[] data, String sign, PublicKey pubKey) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.update(data);
        byte[] sha256 = digest.digest();
        //Signature signature = Signature.getInstance("SHA256withDSA");
        Signature signature = Signature.getInstance("NONEwithRSA");
        signature.initVerify(pubKey);
        signature.update(sha256);
        boolean pass = signature.verify(Base64.decodeBase64(sign));
        if (!pass) {
            return false;
        } else {
            return true;
        }
    }
}
