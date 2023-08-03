package crypto.degest;

import crypto.certificate.ValidRsaCertificate;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * 摘要算法：MD5,SHA-1、SHA-224、SHA-256、SHA-512、SHA3-224、SHA3-384、SHA3-512
 * 消息认证算法：HmacMD5、HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384、HmacSHA512
 */
public class MessageDigestDemo {
    public static void main(String[] args) throws Exception {
        String input = "消息摘要（Message Digest）又称为数字摘要(Digital Digest)，使用数字摘要生成的值是不可以篡改的，为了保证文件或者值的安全";
        // 获取字符串数字摘要对象
        String md5 = getStringDigest(input, "MD5");
        System.out.println("MD5:" + md5);

        String sha1 = getStringDigest(input, "SHA-1");
        System.out.println("SHA-1:" + sha1);

        String sha256 = getStringDigest(input, "SHA-256");
        System.out.println("SHA-256:" + sha256);

        String sha512 = getStringDigest(input, "SHA-512");
        System.out.println("SHA-512:" + sha512);

        //HmacMD5、HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384、HmacSHA512
        byte[] input2 = "Message Authentication Code Test".getBytes();
        macTest(input2, "HmacSHA256");

        //获取文件字摘要对象
        InputStream is = MessageDigestDemo.class.getResourceAsStream("/log4j2.xml");
        String fileSha1 = getFileDigest(is, "MD5");
        System.out.println("MD5:" + fileSha1);

        is = MessageDigestDemo.class.getResourceAsStream("/crypto/server.keystore");
        String fileSha512 = getFileDigest(is, "SHA-256");
        System.out.println("SHA-256:" + fileSha512);
    }

    private static String getStringDigest(String input, String algorithm) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        // 消息数字摘要
        byte[] digest = messageDigest.digest(input.getBytes());
        System.out.println(algorithm+"算法消息摘要字节长度：" + digest.length);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(digest);
    }

    private static void macTest(byte[] input, String algorithm) throws Exception {
        //MAC正因为使用了密钥所以能提供消息认证的功能，且密钥不传输，收发双方提前协商好密钥。
        InputStream is = MessageDigestDemo.class.getResourceAsStream("/crypto/server.keystore");
        SecretKey secretKey = ValidRsaCertificate.getSecretKey(is, "AES256", "123456");

        /*KeyGenerator Algorithms:可通过KeyGenerator生成密钥，也可以是使用keystore中存储的密钥
         *AES,ARCFOUR,Blowfish,ChaCha20,DES(密钥长度56bit),DESede(密钥长度112或168bit),
         * HmacMD5(长度可以任意指定（8的整数倍）),HmacSHA1,
         * HmacSHA224,HmacSHA256,HmacSHA384,HmacSHA512,
         * HmacSHA3-224,HmacSHA3-256,HmacSHA3-384,HmacSHA3-512,RC2
         */
        String alg = "HmacMD5";
        KeyGenerator kg = KeyGenerator.getInstance(alg);
        kg.init(224);
        SecretKey key = kg.generateKey();

        Mac senderMac = Mac.getInstance(algorithm);
        senderMac.init(secretKey);
        senderMac.update(input);
        byte[] bytes1 = senderMac.doFinal();

        Mac receiverMac = Mac.getInstance(algorithm);
        receiverMac.init(secretKey);
        byte[] bytes2 = receiverMac.doFinal(input);

        String receivedMac = Base64.getEncoder().encodeToString(bytes1);
        String calculatedMac = Base64.getEncoder().encodeToString(bytes2);

        System.out.println("ReceivedMac: " + receivedMac);
        if (receivedMac.equals(calculatedMac)) {
            System.out.println("消息认证通过！");
        } else {
            System.err.println("消息认证未通过,计算的MAC= " + calculatedMac);
        }
    }

    private static String getFileDigest(InputStream fis, String algorithm) throws Exception {
        int len;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while ((len = fis.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        // 获取消息摘要对象
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        // 获取消息摘要
        byte[] digest = messageDigest.digest(os.toByteArray());
        System.out.println(algorithm+"算法消息摘要字节长度：" + digest.length);
        return org.apache.commons.codec.binary.Base64.encodeBase64String(digest);
    }

    private static String toHex(byte[] digest) {
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            // 转成 16进制
            String s = Integer.toHexString(b & 0xff);
            if (s.length() == 1) {
                // 如果生成的字符只有一个，前面补0
                s = "0" + s;
            }
            sb.append(s);
        }
        System.out.println("16进制数据的长度：" + sb.toString().getBytes().length);
        return sb.toString();
    }
}
