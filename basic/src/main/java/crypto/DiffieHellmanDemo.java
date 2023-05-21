package crypto;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author gaopeng
 */
public class DiffieHellmanDemo {
    private static final String DH_KEY = "EC"; //DiffieHellman , EC
    private static final int KEY_SIZE = 256;
    private static final String AES_KEY = "AES";
    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";
    private static final SecureRandom random = new SecureRandom();

    public static void main(String[] args) throws Exception {
        // 1.Alice生成密钥对
        KeyPair keyPairAlice = generateKeyPair(KEY_SIZE);
        // 2.Alice将自己的公钥发送给Bobby，实际上公钥对象中封装了大质数P、生成元G和Alice的公钥Y
        PublicKey publicKeyAlice = keyPairAlice.getPublic();

        // 3.Bobby根据Alice的公钥对象生成自己的密钥对
        KeyPair keyPairBob = generateKeyPair(publicKeyAlice.getEncoded());
        // 4.Bobby将自己的公钥回送给Alice
        PublicKey publicKeyBob = keyPairBob.getPublic();

        // 5.Alice根据自己的私钥和Bobby的公钥生成对称密钥
        SecretKey secretKeyAlice = generateSecretKeyBySHA256(publicKeyBob, keyPairAlice.getPrivate(), "ECDH");
        System.out.println("Bobby生成的对称密钥：" + Base64.encodeBase64String(secretKeyAlice.getEncoded()));

        // 6.Bobby根据自己的私钥和Alice的公钥生成对称密钥
        SecretKey secretKeyBob = generateSecretKeyBySHA256(publicKeyAlice, keyPairBob.getPrivate(),"ECDH");
        System.out.println("Alice生成的对称密钥：" + Base64.encodeBase64String(secretKeyBob.getEncoded()));

        String message = "Diffie-Hellman密钥协商算法简介测试";
        byte[] data = message.getBytes(StandardCharsets.UTF_8);

        // 初始化分组密码分组模式的初始化向量，本例AES CBC模式，分组长度128bit，所以IV也是128bit
        byte[] iv = initIV(16);
        // 加密
        byte[] ciphertext = encryption(secretKeyAlice, iv, data);
        System.out.println("加密后的密文：" + Base64.encodeBase64String(ciphertext));

        // 解密
        byte[] plaintext = decryption(secretKeyBob, iv, ciphertext);
        System.out.println("解密后的明文：" + Base64.encodeBase64String(plaintext));

        // 将解密后的字节数组还原成UTF8编码的字符
        System.out.println("解密得到的明文数组还原成UTF8编码：" + new String(plaintext, StandardCharsets.UTF_8));
    }


    /***
     * Alice生成密钥对
     * KeyPairGenerator.getInstance的入参查看KeyPairGenerator的文档
     * @return Alice的密钥对
     */
    public static KeyPair generateKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(DH_KEY);
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Bobby收到Alice的公钥数组：
     * 1.先将公钥数组解析成公钥对象
     * KeyFactory.getInstance的入参查看KeyFactory的文档
     * 2.根据Alice的公钥对象生成自己的密钥对
     *
     * @param publicKeyArray publicKeyArray
     * @return KeyPair
     * @throws Exception Exception
     */
    public static KeyPair generateKeyPair(byte[] publicKeyArray) throws Exception {
        // Bobby将Alice发过来的公钥数组解析成公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyArray);
        KeyFactory keyFactory = KeyFactory.getInstance(DH_KEY);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // Bobby根据Alice的公钥生成自己的密钥对
        AlgorithmParameterSpec parameterSpec = switch (DH_KEY) {
            case "EC" -> ((ECPublicKey) publicKey).getParams();
            case "DiffieHellman", "DH" -> ((DHPublicKey) publicKey).getParams();
            default -> null;
        };
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(DH_KEY);
        keyPairGenerator.initialize(parameterSpec);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Alice和Bobby通过自己的私钥和对方的公钥计算生成相同的对称密钥
     * 注意：当前推荐的用法
     *
     * @param publicKey  对方的公钥
     * @param privateKey 自己的私钥
     * @return 生成的对称密钥
     */
    public static SecretKey generateSecretKeyBySHA256(PublicKey publicKey, PrivateKey privateKey,String algorithm) throws Exception {
        //KeyAgreement Algorithms : DiffieHellman、ECDH、ECMQV、XDH、X25519、X448
        KeyAgreement keyAgreement = KeyAgreement.getInstance(algorithm);
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);
        // 当前推荐的做法
        byte[] keyArray = keyAgreement.generateSecret();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digest = messageDigest.digest(keyArray);
        return new SecretKeySpec(digest, AES_KEY);
    }

    /**
     * 生成分组密码分组模式的初始化向量
     * 本例中，AES的分组128bit，16字节
     *
     * @param length length
     * @return byte[]
     */
    public static byte[] initIV(int length) {
        return random.generateSeed(length);
    }

    /**
     * 加密
     *
     * @param secretKey 密钥协商阶段协商的对称密钥
     * @param iv        分组密码分组模式的初始化向量，加密和解密需要使用相同的iv
     * @param data      需要加密的明文字节数组
     * @return 密文
     */
    public static byte[] encryption(SecretKey secretKey, byte[] iv, byte[] data) throws Exception {
        // 密钥和算法有关系，和加解密模式没有关系，所以上面分成了AES_KEY和CIPHER_MODE
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        // 分组密码分组模式的初始化向量参数，本例中是CBC模式
        IvParameterSpec ivp = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivp);
        return cipher.doFinal(data);
    }

    /**
     * 解密
     *
     * @param secretKey 密钥协商阶段协商的对称密钥
     * @param iv        分组密码分组模式的初始化向量，加密和解密需要使用相同的iv
     * @param data      需要解密的密文的字节数组
     * @return 解密后的明文
     */
    public static byte[] decryption(SecretKey secretKey, byte[] iv, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_MODE);
        // 分组密码分组模式的初始化向量参数，本例中是CBC模式
        IvParameterSpec ivp = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivp);
        return cipher.doFinal(data);
    }
}
