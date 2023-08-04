package crypto.symmetric;

import basic.thread.waitnotify.input;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
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
        // AES加密算法，密钥的长度可以是128位16个字节、192位（24字节）或者256位(32字节),
        // 密码可以随机生成也可以手动指定，例如String key = "A1B2C3D4E5F6G7H8";
        byte[] key = new SecureRandom().generateSeed(32);
        System.out.println("key = " + Base64.getEncoder().encodeToString(key));

        //Cipher Algorithm Modes:ECB、CBC、CFB、OFB、CTR、GCM
        //Cipher Algorithm Paddings:NoPadding、PKCS5Padding
        String transformation = "AES/GCM/NoPadding";

        String input = "AES加密算法测试 : "+transformation;
        System.out.println("加密前的数据 = " + input);

        byte[] encryptAES = encryptAES(input.getBytes(), key, transformation);
        System.out.println(transformation + "加密,并将其编码成Base64字符串 = " + Base64.getEncoder().encodeToString(encryptAES));
        byte[] decryptAES = decryptAES(encryptAES, key, transformation);
        System.out.println(transformation + "解密,并将其转换成UTF8字符串 = " + new String(decryptAES, StandardCharsets.UTF_8));

        //ASE-GCM TEST
        byte[] encryptGCM = encryptAesGcmWithIV(input.getBytes(), key);
        System.out.println("AES-GCM加密,并将其编码成Base64字符串 = " + Base64.getEncoder().encodeToString(encryptGCM));
        byte[] decryptGCM = decryptAesGcmWithIV(encryptGCM, key);
        System.out.println("AES-GCM解密,并将其转换成UTF8字符串 = " + new String(decryptGCM, StandardCharsets.UTF_8));
    }

    /**
     * 使用DES加密数据
     *
     * @param input          : 原文
     * @param key            : 密钥(AES,密钥的长度必须是8个字节)
     * @param transformation : 获取Cipher对象的算法
     * @return : 密文
     */
    private static byte[] encryptAES(byte[] input, byte[] key, String transformation) throws Exception {
        // 获取加密对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 创建加密规则
        SecretKeySpec sks = new SecretKeySpec(key, "AES");

        //初始向量中密钥的长度需符合算法标准，DES为8字节，AES为16字节
        AlgorithmParameterSpec parameterSpec = null;
        if (transformation.contains("CBC") || transformation.contains("CFB/NoPadding")
                || transformation.contains("OFB/NoPadding")) {
            parameterSpec = new IvParameterSpec(KEY_VI);
        } else if (transformation.contains("GCM/NoPadding")) {
            parameterSpec = new GCMParameterSpec(128, key);
        }
        cipher.init(Cipher.DECRYPT_MODE, sks, parameterSpec);

        // 初始化加密模式和算法
        cipher.init(Cipher.ENCRYPT_MODE, sks, parameterSpec);
        // 加密
        return cipher.doFinal(input);
    }

    /**
     * 使用DES解密
     *
     * @param encryptData    : 密文
     * @param key            : 密钥
     * @param transformation : 获取Cipher对象的算法
     * @return: decrypt bytes
     */
    private static byte[] decryptAES(byte[] encryptData, byte[] key, String transformation) throws Exception {
        // 获取Cipher对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 指定密钥规则
        SecretKeySpec sks = new SecretKeySpec(key, "AES");

        //初始向量，参数表示跟谁进行异或，初始向量的长度必须是16字节, CBC/CFB/OFB模式需要IV
        AlgorithmParameterSpec parameterSpec = null;
        if (transformation.contains("CBC") || transformation.contains("CFB/NoPadding")
                || transformation.contains("OFB/NoPadding")) {
            parameterSpec = new IvParameterSpec(KEY_VI);
        } else if (transformation.contains("GCM/NoPadding")) {
            parameterSpec = new GCMParameterSpec(128, key);
        }
        cipher.init(Cipher.DECRYPT_MODE, sks, parameterSpec);

        //解密，上面使用的base64编码，下面直接用密文
        return cipher.doFinal(encryptData);
    }

    /**
     * @param encryptData encryptData
     * @param key         key
     * @return decrypt bytes
     * @throws Exception
     */
    public static byte[] decryptAesGcmWithIV(byte[] encryptData, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);

        //12是加密时在前12byte插入的IV数据
        GCMParameterSpec params = new GCMParameterSpec(128, encryptData, 0, 12);
        cipher.init(Cipher.DECRYPT_MODE, sks, params);

        return cipher.doFinal(encryptData, 12, encryptData.length - 12);
    }

    /**
     * @param input to encrypt data
     * @param key   key
     * @return encrypt bytes
     * @throws Exception
     */
    public static byte[] encryptAesGcmWithIV(byte[] input, byte[] key) throws Exception {
        //将输入数据加密
        SecretKeySpec sks = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        byte[] encryptData = cipher.doFinal(input);
        assert encryptData.length == input.length + 16;

        //获取cipher的IV
        byte[] iv = cipher.getIV();
        assert iv.length == 12;// 偏移参数及长度要在解密的时候保持一致

        //将IV以及encryptData复制到buffer
        byte[] buffer = new byte[12 + encryptData.length];

        //将IV复制到buffer
        System.arraycopy(iv, 0, buffer, 0, 12);
        //将加密数据复制到buffer
        System.arraycopy(encryptData, 0, buffer, 12, encryptData.length);
        return buffer;
    }
}
