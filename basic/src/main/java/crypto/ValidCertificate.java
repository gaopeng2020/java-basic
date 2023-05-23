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
import java.security.cert.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * @author gaopeng
 */
public class ValidCertificate {
    public static void main(String[] args) throws Exception {
        String storePath = Objects.requireNonNull(ValidCertificate.class.getClassLoader().getResource("server.keystore")).getFile();

        String alias = "server_rsa";
        String password = "123456";
        KeyStore ks = getKeyStore(storePath, password);

        System.out.println("证书验证是否通过：" + verifyCertificate(ks.getCertificateChain(alias)));
        System.out.println("签名验证是否通过 = " + verifySignature(ks, alias));

        PrivateKey privateKey = getPrivateKey(ks, alias, password);
        PublicKey publicKey = getPublicKey(ks, alias);

        String strData = "加密前的数据";
        System.out.println("加密前数据：" + strData);
        byte[] encryptedData = encrypt(strData, publicKey);
        System.out.println("加密后的数据：" + Base64.encodeBase64String(encryptedData));

        byte[] decryptedData = decrypt(encryptedData, privateKey);
        assert decryptedData != null;
        System.out.println("解密后的数据：" + new String(decryptedData));

        String strSignData = "签名前的数据";
        byte[] toSignData = strSignData.getBytes(StandardCharsets.UTF_8);
        byte[] signedData = sign(toSignData, privateKey);
        if (verify(toSignData, signedData, publicKey)) {
            System.out.println("验签通过");
        } else {
            System.out.println("验签错误");
        }

    }

    private static boolean verifySignature(KeyStore ks, String alias) {
        try {
            X509Certificate subjectCer = (X509Certificate) ks.getCertificate(alias);
            Certificate[] certificateChain = ks.getCertificateChain(alias);
            Certificate issueCer = certificateChain[1];
            byte[] tbsData = subjectCer.getTBSCertificate();
            byte[] signedData = subjectCer.getSignature();

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] digested = digest.digest(tbsData);

            String algorithm = subjectCer.getPublicKey().getAlgorithm();
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, issueCer.getPublicKey());
            byte[] decryptSignedData = cipher.doFinal(signedData);

            System.out.println("digested = " + Base64.encodeBase64String(digested));
            System.out.println("decryptSignedData = " + Base64.encodeBase64String(decryptSignedData));
            if (Base64.encodeBase64String(digested).equals(Base64.encodeBase64String(decryptSignedData))) {
                return true;
            }
        } catch (KeyStoreException | CertificateEncodingException | NoSuchAlgorithmException |
                 InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return false;
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
    private static PrivateKey getPrivateKey(KeyStore keyStore, String alias, String keyPass) throws Exception {
        PrivateKey privKey = (PrivateKey) keyStore.getKey(alias, keyPass.toCharArray());
        //返回私钥字符串
        String privateKey = Base64.encodeBase64String(privKey.getEncoded());
        System.out.println("私钥字符串：" + privateKey);
        return privKey;
    }

    /**
     * 通过路径获取公钥
     */
    private static PublicKey getPublicKey(KeyStore keyStore, String alias) throws Exception {
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
    private static byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(data);
        byte[] digestValue = messageDigest.digest();

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(digestValue);
        byte[] sign = signature.sign();
        return Base64.encodeBase64(sign);
    }

    /**
     * 验签
     */
    private static boolean verify(byte[] plainData, byte[] signedData, PublicKey pubKey) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] digestValue = digest.digest(plainData);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(pubKey);
        signature.update(digestValue);
        return signature.verify(Base64.decodeBase64(signedData));
    }

    /**
     *
     * @param certificateChain certificateChain
     * @return true if passed
     */
    public static boolean verifyCertificate(Certificate[] certificateChain) {
        //获取证书链长度
        int length = certificateChain.length;

        //验证证书链里每个证书是否在有效期里
        Date date = new Date();
        X509Certificate[] x509Certificates = new X509Certificate[length];
        for (int i = 0; i < length; i++) {
            Certificate certificate = certificateChain[i];
            if (certificate instanceof X509Certificate) {
                x509Certificates[i] = (X509Certificate) certificate;
                try {
                    x509Certificates[i].checkValidity(date);
                } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        //声明list，存储证书链中证书主体信息
        ArrayList<BigInteger> list = new ArrayList<>();

        //沿证书链自上而下(用户证书->中间商CA证书->CA根证书)，验证证书的颁布者是下一个证书的持有者
        for (int i = 0; i < length - 1; i++) {
            X509Certificate currentCertificate = x509Certificates[i];
            X509Certificate nextCertificate = x509Certificates[i + 1];
            //获取当前证书颁布者标识
            Principal issuerPrincipal = currentCertificate.getIssuerX500Principal();
            //获取下一个证书的使用者标识
            Principal subjectPrincipal = nextCertificate.getSubjectX500Principal();
            //保存证书的序列号
            list.add(currentCertificate.getSerialNumber());

            //验证证书的颁布者是上一个证书的持有者
            if (issuerPrincipal.equals(subjectPrincipal)) {
                try {
                    //获取下个证书的公钥
                    PublicKey publickey = nextCertificate.getPublicKey();
                    //验证是否已使用与指定公钥相应的私钥签署了此证书
                    currentCertificate.verify(publickey);
                } catch (CertificateException | NoSuchAlgorithmException | SignatureException |
                         InvalidKeyException | NoSuchProviderException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return false;
            }
        }

/*        //验证根证书是否在撤销列表中
        try {
            if (!X509crl.getIssuerX500Principal().equals(x509CertificateRoot.getSubjectX500Principal())) {
                return false;
            }
            X509crl.verify(x509CertificateRoot.getPublicKey());
        } catch (Exception e) {
            return false;
        }
        //在当前时间下，验证证书链中每个证书是否存在撤销列表中
        try {
            //获取CRL中所有的项
            Set<? extends X509CRLEntry> setEntries = X509crl.getRevokedCertificates();
            if (!setEntries.isEmpty()) {
                for (X509CRLEntry X509crlentry : setEntries) {
                    if (list.contains(X509crlentry.getSerialNumber())) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        //证明证书链中的最后一个证书的所有者正是现在通信对象
        Principal principalSubject = x509Certificates[length - 1].getSubjectX500Principal();
        if (!stringTarget.equals(principalSubject.getName())) {
            return false;
        }*/

        return true;
    }
}
