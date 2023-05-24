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
 * Algorithm Name	Description
 * DiffieHellman	Keys for the Diffie-Hellman KeyAgreement algorithm.Note: key.getAlgorithm() will return "DH" instead of "DiffieHellman".
 * DSA	Keys for the Digital Signature Algorithm.
 * EC	Keys for the Elliptic Curve algorithm.
 * EdDSA	Keys for Edwards-Curve signature algorithm with elliptic curves as defined in RFC 8032.
 * Ed25519	Keys for Edwards-Curve signature algorithm with Ed25519 as defined in RFC 8032.
 * Ed448	Keys for Edwards-Curve signature algorithm with Ed448 as defined in RFC 8032.
 * RSA	Keys for the RSA algorithm (Signature/Cipher).
 * RSASSA-PSS	Keys for the RSASSA-PSS algorithm (Signature).
 * XDH	Keys for Diffie-Hellman key agreement with elliptic curves as defined in RFC 7748.
 * X25519	Keys for Diffie-Hellman key agreement with Curve25519 as defined in RFC 7748.
 * X448	Keys for Diffie-Hellman key agreement with Curve448 as defined in RFC 7748.
 */
public class DhKeyAgreementDemo {
    /**
     * KeyPairGenerator Algorithms :
     *       DiffieHellman(1024, 2048, 4096)，DSA(1024, 2048)，RSA(1024, 2048, 4096)，RSASSA-PSS，
     *       EC(256,384)，EdDSA，Ed25519，Ed448，XDH，X25519，X448
     */
    private static final String KEY_PAIR_ALGORITHM = "EC";
    private static final int KEY_SIZE = 256;
    /**
     * KeyAgreement Algorithms :
     *       DiffieHellman、ECDH、ECMQV、XDH、X25519、X448
     * */
    private static final String KEY_AGREEMENT_ALGORITHM = "ECDH";
    /**
     * Cipher Algorithm
     *      AES,AESWrap,AESWrapPad,ARCFOUR,Blowfish,ChaCha20,ChaCha20-Poly1305,
     *      DES,DESede(Triple DES),DESedeWrap,ECIES,RC2,RC4,RC5,RSA,
     */
    private static final String CIPHER_ALGORITHM = "AES";

    /**
     * Cipher Algorithm Modes:
     *      NONE,CBC,CCM,CFB, CFBx,CTR,CTS,ECB,GCM,KW,KWP,OFB, OFBx,PCBC,
     * Cipher Algorithm Paddings:
     *      NoPadding,ISO10126Padding,OAEPPadding, PKCS1Padding,PKCS5Padding,SSL3Padding
     * 在AES标准规范中，分组密码长度只能是128位，即每个分组为16个字节。密钥的长度可以使用128位、192位或256位
     * DES,3DES分组密码长度为8字节
     */
    private static final String CIPHER_MODE = "AES/CBC/PKCS5Padding";
    private static final SecureRandom RANDOM = new SecureRandom();

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
        SecretKey secretKeyAlice = generateSecretKeyBySHA256(publicKeyBob, keyPairAlice.getPrivate(), KEY_AGREEMENT_ALGORITHM);
        System.out.println("Bobby生成的对称密钥：" + Base64.encodeBase64String(secretKeyAlice.getEncoded()));

        // 6.Bobby根据自己的私钥和Alice的公钥生成对称密钥
        SecretKey secretKeyBob = generateSecretKeyBySHA256(publicKeyAlice, keyPairBob.getPrivate(), KEY_AGREEMENT_ALGORITHM);
        System.out.println("Alice生成的对称密钥：" + Base64.encodeBase64String(secretKeyBob.getEncoded()));

        String message = "Diffie-Hellman密钥协商算法简介测试";
        byte[] data = message.getBytes(StandardCharsets.UTF_8);

        // 初始化分组密码分组模式的初始化向量，本例AES CBC模式，分组长度128bit，所以IV也是128bit
        byte[] iv = initIV(16);
        // 加密
        byte[] encryptData = encryption(secretKeyAlice, iv, data);
        System.out.println("加密后的密文：" + Base64.encodeBase64String(encryptData));

        // 解密
        byte[] plaintext = decryption(secretKeyBob, iv, encryptData);
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
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_PAIR_ALGORITHM);
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
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_PAIR_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // Bobby根据Alice的公钥生成自己的密钥对
        AlgorithmParameterSpec parameterSpec = switch (KEY_PAIR_ALGORITHM) {
            case "EC" -> ((ECPublicKey) publicKey).getParams();
            case "DiffieHellman", "DH" -> ((DHPublicKey) publicKey).getParams();
            default -> null;
        };
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_PAIR_ALGORITHM);
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
    public static SecretKey generateSecretKeyBySHA256(PublicKey publicKey, PrivateKey privateKey, String algorithm) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance(algorithm);
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);
        // 当前推荐的做法
        byte[] keyArray = keyAgreement.generateSecret();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] digest = messageDigest.digest(keyArray);
        return new SecretKeySpec(digest, CIPHER_ALGORITHM);
    }

    /**
     * 生成分组密码分组模式的初始化向量
     * 本例中，AES的分组128bit，16字节
     *
     * @param length length
     * @return byte[]
     */
    public static byte[] initIV(int length) {
        return RANDOM.generateSeed(length);
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
