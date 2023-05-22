package crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author gaopeng
 */
public class AesEncryptDemo {
    // DES加密算法,key的大小必须是8个字节


    public static void main(String[] args) throws Exception {
        String input = "AES加密算法测试 : AES/CBC/PKCS5Padding";
        // AES加密算法，key的大小必须是16个字节
        String key = "A1B2C3D4E5F6G7H8";

        // CBC模式,必须指定初始向量,初始向量中密钥的长度需符合算法标准，DES为8字节，AES为16字节
        String transformation = "AES/CBC/PKCS5Padding";
        // 指定获取密钥的算法
        String algorithm = "AES";

        String encryptAES = encryptAES(input, key, transformation, algorithm);
        System.out.println("加密:" + encryptAES);

        String s = decryptAES(encryptAES, key, transformation, algorithm);
        System.out.println("解密:" + s);
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
    private static String encryptAES(String input, String key, String transformation, String algorithm) throws Exception {
        // 获取加密对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 创建加密规则
        // 第一个参数key的字节
        // 第二个参数表示加密算法
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);

        //初始向量，参数表示跟谁进行异或，初始向量的长度必须是8位
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());

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
    private static String decryptAES(String input, String key, String transformation, String algorithm) throws Exception {
        // 1,获取Cipher对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 指定密钥规则
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);

        //初始向量，参数表示跟谁进行异或，初始向量的长度必须是8位
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, sks, iv);
        // 3. 解密，上面使用的base64编码，下面直接用密文
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(input));

        //  因为是明文，所以直接返回
        return new String(bytes);
    }
}
