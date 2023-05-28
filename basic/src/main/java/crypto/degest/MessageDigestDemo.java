package crypto.degest;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MessageDigestDemo {
    public static void main(String[] args) throws Exception {
        String input = "消息摘要（Message Digest）又称为数字摘要(Digital Digest)，使用数字摘要生成的值是不可以篡改的，为了保证文件或者值的安全";
        // 获取字符串数字摘要对象
        String md5 = getDigest(input, "MD5");
        System.out.println("MD5:"+md5);

        String sha1 = getDigest(input, "SHA-1");
        System.out.println("SHA-1:"+sha1);

        String sha256 = getDigest(input, "SHA-256");
        System.out.println("SHA-256:"+sha256);

        String sha512 = getDigest(input, "SHA-512");
        System.out.println("SHA-512:"+sha512);

        messageAuthenticationCodeTest();

        //获取文件字摘要对象
        String path = "basic/src/main/resources/log4j2.xml";
        String fileSha1 = getDigestFile(path, "MD5");
        System.out.println("MD5:"+fileSha1);

        path = "C:\\Users\\gaopeng\\Desktop\\INCOSE-OMGSysML-Tutorial-Final-090901.pdf";
        String fileSha512 = getDigestFile(path, "SHA-256");
        System.out.println("SHA-256:"+fileSha512);
    }
    private static String getDigest(String input, String algorithm) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        // 消息数字摘要
        byte[] digest = messageDigest.digest(input.getBytes());
        System.out.println("密文的字节长度:" + digest.length);
        return toHex(digest);
    }

    private static void messageAuthenticationCodeTest() throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] input = "Message Authentication Code Test".getBytes();
//        HmacMD5、HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384、HmacSHA512
        KeyGenerator kg = KeyGenerator.getInstance("HmacSHA256");
        SecretKey secretKey = kg.generateKey();
        System.err.println("secretKey: "+Base64.getEncoder().encodeToString(secretKey.getEncoded()));

        Mac senderMac = Mac.getInstance(kg.getAlgorithm());
        senderMac.init(secretKey);
        senderMac.update(input);
        byte[] bytes1 = senderMac.doFinal();
        System.err.println("SenderMac: "+Base64.getEncoder().encodeToString(bytes1));

        Mac receiverMac = Mac.getInstance(kg.getAlgorithm());
        receiverMac.init(secretKey);
        byte[] bytes2 = receiverMac.doFinal(input);
        System.err.println("ReceiverMac: "+Base64.getEncoder().encodeToString(bytes2));
    }

    private static String getDigestFile(String filePath, String algorithm) throws Exception{
        FileInputStream fis = new FileInputStream(filePath);
        int len;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        while ( (len =  fis.read(buffer))!=-1){
            os.write(buffer,0,len);
        }
        // 获取消息摘要对象
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        // 获取消息摘要
        byte[] digest = messageDigest.digest(os.toByteArray());
        System.out.println("密文的字节长度："+digest.length);
        return toHex(digest);
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
