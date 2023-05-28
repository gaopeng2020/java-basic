package crypto.signature;

import crypto.certificate.ValidRsaCertificate;
import org.apache.commons.codec.binary.Base64;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

/**
 * @author gaopeng
 */
public class SignatureDemo {
    private static final String SIGNATURE_ALGORITHM = "SHA256withECDSA";
    private static final String DIGEST_ALGORITHM = "SHA-256";

    public static void main(String[] args) throws Exception {
        InputStream storeStream = SignatureDemo.class.getResourceAsStream("/crypto/server.keystore");
        String alias = "server_ecc";
        String ksPassword = "123456";
        KeyStore ks = ValidRsaCertificate.getKeyStore(storeStream, ksPassword);

        //验证证书签名
        System.out.println("证书签名验证是否通过：" + verifyCertificateSignature(ks, alias));

        //数据签名测试（所谓签名就是用摘要算法获取数据的摘要值，然后用私钥加秘密加密摘要值，加密后的数据就是签名数据）
        PrivateKey privateKey = ValidRsaCertificate.getPrivateKey(ks, alias, ksPassword);
        PublicKey publicKey = ValidRsaCertificate.getPublicKey(ks, alias);
        String tbsData = "签名前的数据";
        byte[] toSignData = tbsData.getBytes(StandardCharsets.UTF_8);
        //用SubjectCertificate的私钥签名数据
        byte[] signedData = sign(toSignData, privateKey, SIGNATURE_ALGORITHM, DIGEST_ALGORITHM);
        //  用SubjectCertificate的公钥验签数据
        if (verify(toSignData, signedData, publicKey, SIGNATURE_ALGORITHM, DIGEST_ALGORITHM)) {
            System.out.println("验签通过");
        } else {
            System.out.println("验签错误");
        }
    }

    private static boolean verifyCertificateSignature(KeyStore ks, String alias) {
        try {
            //获取SubjectCertificate的签名数据
            X509Certificate subjectCer = (X509Certificate) ks.getCertificate(alias);
            byte[] signedData = Base64.encodeBase64(subjectCer.getSignature());

            //获取SubjectCertificate的摘要信息，并计算出摘要值
            byte[] tbsData = subjectCer.getTBSCertificate();

            //获取SubjectCertificate的颁发者证书
            Certificate[] certificateChain = ks.getCertificateChain(alias);
            Certificate issueCer = certificateChain[2];

            //验签
            return verify(tbsData, signedData, issueCer.getPublicKey(), SIGNATURE_ALGORITHM, DIGEST_ALGORITHM);
        } catch (KeyStoreException | CertificateEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 签名
     */
    private static byte[] sign(byte[] data, PrivateKey privateKey, String signatureAlgorithm, String digestAlgorithm) throws Exception {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(digestAlgorithm);
            messageDigest.update(data);
            byte[] digestValue = messageDigest.digest();

            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initSign(privateKey);
            signature.update(digestValue);
            byte[] sign = signature.sign();
            return Base64.encodeBase64(sign);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 验签
     */
    private static boolean verify(byte[] tbsData, byte[] signedData, PublicKey publicKey, String signatureAlgorithm, String digestAlgorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
            byte[] digestValue = digest.digest(tbsData);

            Signature signature = Signature.getInstance(signatureAlgorithm);
            signature.initVerify(publicKey);
            signature.update(digestValue);
            return signature.verify(Base64.decodeBase64(signedData));
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
