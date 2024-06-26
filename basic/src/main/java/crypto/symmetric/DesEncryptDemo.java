package crypto.symmetric;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author gaopeng
 */
public class DesEncryptDemo {
    // DES加密算法,key的大小必须是8个字节


    public static void main(String[] args) throws Exception {
        String input ="DES加密算法测试";
        // DES加密算法，key的大小必须是8个字节
        String key = "12345678";
        // 指定获取密钥的算法
        String algorithm = "DES";

        String encryptDES = encryptDES(input, key,  algorithm);
        System.out.println("加密:" + encryptDES);

        String s = decryptDES(encryptDES, key, algorithm);
        System.out.println("解密:" + s);
    }

    /**
     * 使用DES加密数据
     *
     * @param input          : 原文
     * @param key            : 密钥(DES,密钥的长度必须是8个字节)
     * @param algorithm      : 获取密钥的算法
     * @return : 密文
     */
    private static String encryptDES(String input, String key,String algorithm) throws Exception {
        // 获取加密对象
        Cipher cipher = Cipher.getInstance(algorithm);
        // 创建加密规则
        // 第一个参数key的字节
        // 第二个参数表示加密算法
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        // ENCRYPT_MODE：加密模式
        // DECRYPT_MODE: 解密模式
        // 初始化加密模式和算法
        cipher.init(Cipher.ENCRYPT_MODE,sks);
        // 加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        // 输出加密后的数据
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }


    /**
     * 使用DES解密
     *
     * @param input          : 密文
     * @param key            : 密钥
     * @param algorithm      : 获取密钥的算法
     * @return: 原文
     */
    private static String decryptDES(String input, String key, String algorithm) throws Exception {
        // 1,获取Cipher对象
        Cipher cipher = Cipher.getInstance(algorithm);
        // 指定密钥规则
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        // 3. 解密，上面使用的base64编码，下面直接用密文
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(input));
        return new String(bytes);
    }
}
