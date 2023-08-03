package crypto.symmetric;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author gaopeng
 */
public class AesEncryptDemo {
    /**
     * 初始向量IV, 初始向量IV的长度规定为128位16个字节, 初始向量的来源为openssl随机生成(openssl rand -base64 16).
     */
    private static final byte[] KEY_VI = Base64.getDecoder().decode("muN/4hiPz8mxxIiKBVgTtg==");

    public static void main(String[] args) throws Exception {
        String input = "AES加密算法测试 : AES/CBC/PKCS5Padding";
        System.out.println("加密前数据 = " + input);

        // AES加密算法，密钥的长度可以是128位16个字节、192位（24字节）或者256位(32字节),
        // 密码可以随机生成也可以手动指定，例如String key = "A1B2C3D4E5F6G7H8";
        byte[] key = new SecureRandom().generateSeed(32);
        System.out.println("key = " + Base64.getEncoder().encodeToString(key));

        // CBC模式,必须指定初始向量,初始向量中密钥的长度需符合算法标准，DES为8字节，AES为16字节
        String transformation = "AES/CBC/PKCS5Padding";
        // 指定获取密钥的算法
        String algorithm = "AES";

        String encryptAES = encryptAES(input, key, transformation, algorithm);
        System.out.println("加密:" + encryptAES);

        String s = decryptAES(encryptAES, key, transformation, algorithm);
        System.out.println("解密:" + s);

        //ASE-256-GCM TEST
        AESGCMEncrypt(input,key);
        AESGCMDecrypt(input,key);
    }

    /**
     * 使用DES加密数据
     *
     * @param input          : 原文
     * @param key            : 密钥(AES,密钥的长度必须是8个字节)
     * @param transformation : 获取Cipher对象的算法
     * @param algorithm      : 获取密钥的算法
     * @return : 密文
     */
    private static String encryptAES(String input, byte[] key, String transformation, String algorithm) throws Exception {
        // 获取加密对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 创建加密规则
        // 第一个参数key的字节
        // 第二个参数表示加密算法
        SecretKeySpec sks = new SecretKeySpec(key, algorithm);

        //初始向量，参数表示跟谁进行异或，初始向量的长度必须是8位
        IvParameterSpec iv = new IvParameterSpec(KEY_VI);

        // ENCRYPT_MODE：加密模式
        // DECRYPT_MODE: 解密模式
        // 初始化加密模式和算法
        cipher.init(Cipher.ENCRYPT_MODE, sks, iv);
        // 加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }


    /**
     * 使用DES解密
     *
     * @param input          : 密文
     * @param key            : 密钥
     * @param transformation : 获取Cipher对象的算法
     * @param algorithm      : 获取密钥的算法
     * @return: 原文
     */
    private static String decryptAES(String input, byte[] key, String transformation, String algorithm) throws Exception {
        // 1,获取Cipher对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 指定密钥规则
        SecretKeySpec sks = new SecretKeySpec(key, algorithm);

        //初始向量，参数表示跟谁进行异或，初始向量的长度必须是16字节
        IvParameterSpec iv = new IvParameterSpec(KEY_VI);

        cipher.init(Cipher.DECRYPT_MODE, sks, iv);
        // 3. 解密，上面使用的base64编码，下面直接用密文
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(input));

        //  因为是明文，所以直接返回
        return new String(bytes);
    }

    //解密
    public static String AESGCMDecrypt(String content, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);

        byte[] message = Base64.getDecoder().decode(content);
        // 这里的12和16是加密的时候设置的偏移参数及加密长度
        if (message.length < 12 + 16) throw new IllegalArgumentException();
        GCMParameterSpec params = new GCMParameterSpec(128, message, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, sks, params);
        byte[] decryptData = cipher.doFinal(message, 12, message.length - 12);
        return new String(decryptData);
    }

    //加密
    public static String AESGCMEncrypt(String content, byte[] key) throws Exception {
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        byte[] iv = cipher.getIV();
        assert iv.length == 12;// 偏移参数及长度要在解密的时候保持一致
        byte[] encryptData = cipher.doFinal(content.getBytes());

        assert encryptData.length == content.getBytes().length + 16;
        byte[] message = new byte[12 + content.getBytes().length + 16];
        System.arraycopy(iv, 0, message, 0, 12);
        System.arraycopy(encryptData, 0, message, 12, encryptData.length);
        return Base64.getEncoder().encodeToString(message);

    }
}
