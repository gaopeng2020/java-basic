package crypto;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RsaDemo {
    public static void main(String[] args) throws Exception {
        String input = "RSA非对称加密测试";
        // 加密算法
        String algorithm = "RSA";

        //生成密钥对并保存在本地文件中
        generateKeyToFile(algorithm, "key.pub", "key.pri");

        //用私钥加密
        PrivateKey privateKey = getPrivateKey("key.pri", algorithm);
        String s = encryptRSA(algorithm, privateKey, input);
        System.out.println("密文："+s);

        //用公钥解密
        PublicKey publicKey = getPublicKey("key.pub", algorithm);
        String s1 = decryptRSA(algorithm, publicKey, s);
        System.out.println("明文："+s1);

        //获取签名
        PrivateKey priKey = loadPrivateKeyFromFile("RSA", "key.pri");
        String signatureData = getSignature(input, "sha256withrsa", priKey);
        System.out.println("签名数据："+signatureData);

        //认证签名
        PublicKey pubKey =loadPublicKeyFromFile("RSA", "key.pub");
        boolean b = verifySignature(input, "sha256withrsa", pubKey, signatureData);
        System.out.println("签名验证是否成功："+b);
    }

    public static PublicKey getPublicKey(String path,String algorithm) throws Exception{
        // 将文件内容转为字符串
        String publicKeyString = FileUtils.readFileToString(new File(path), Charset.defaultCharset());
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        // 构建密钥规范 进行Base64解码
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.decode(publicKeyString));
        // 生成公钥
        return keyFactory.generatePublic(spec);
    }

    public static PrivateKey getPrivateKey(String path,String algorithm) throws Exception{
        // 将文件内容转为字符串
        String privateKeyString = FileUtils.readFileToString(new File(path), Charset.defaultCharset());
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        // 构建密钥规范 进行Base64解码
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.decode(privateKeyString));
        // 生成私钥
        return keyFactory.generatePrivate(spec);
    }

    /**
     * 生成密钥对并保存在本地文件中
     *
     * @param algorithm : 算法
     * @param pubPath   : 公钥保存路径
     * @param priPath   : 私钥保存路径
     */
    public static void generateKeyToFile(String algorithm, String pubPath, String priPath) throws Exception {
        // 获取密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        // 获取密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 获取公钥
        PublicKey publicKey = keyPair.getPublic();
        // 获取私钥
        PrivateKey privateKey = keyPair.getPrivate();
        // 获取byte数组
        byte[] publicKeyEncoded = publicKey.getEncoded();
        byte[] privateKeyEncoded = privateKey.getEncoded();
        // 进行Base64编码
        String publicKeyString = Base64.encode(publicKeyEncoded);
        String privateKeyString = Base64.encode(privateKeyEncoded);
        // 保存文件
        FileUtils.writeStringToFile(new File(pubPath), publicKeyString, StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(priPath), privateKeyString, StandardCharsets.UTF_8);
    }

    /**
     * 解密数据
     *
     * @param algorithm      : 算法
     * @param encrypted      : 密文
     * @param key            : 密钥
     * @return : 原文
     */
    public static String decryptRSA(String algorithm,Key key,String encrypted) throws Exception{
        // 创建加密对象
        // 参数表示加密算法
        Cipher cipher = Cipher.getInstance(algorithm);
        // 私钥进行解密
        cipher.init(Cipher.DECRYPT_MODE,key);
        // 由于密文进行了Base64编码, 在这里需要进行解码
        byte[] decode = Base64.decode(encrypted);
        // 对密文进行解密，不需要使用base64，因为原文不会乱码
        byte[] bytes1 = cipher.doFinal(decode);
        return new String(bytes1);

    }
    /**
     * 使用密钥加密数据
     *
     * @param algorithm      : 算法
     * @param input          : 原文
     * @param key            : 密钥
     * @return : 密文
     */
    public static String encryptRSA(String algorithm,Key key,String input) throws Exception{
        // 创建加密对象
        // 参数表示加密算法
        Cipher cipher = Cipher.getInstance(algorithm);
        // 初始化加密
        // 第一个参数:加密的模式
        // 第二个参数：使用私钥进行加密
        cipher.init(Cipher.ENCRYPT_MODE,key);
        // 私钥加密
        byte[] bytes = cipher.doFinal(input.getBytes());
        // 对密文进行Base64编码
        return Base64.encode(bytes);
    }

    /**
     * 从文件中加载公钥
     *
     * @param algorithm : 算法
     * @param filePath  : 文件路径
     * @return : 公钥
     */
    public static PublicKey loadPublicKeyFromFile(String algorithm, String filePath) throws Exception {
        // 将文件内容转为字符串
        String keyString = FileUtils.readFileToString(new File(filePath), Charset.forName("UTF-8"));
        return loadPublicKeyFromString(algorithm, keyString);
    }

    /**
     * 从字符串中加载公钥
     *
     * @param algorithm : 算法
     * @param keyString : 公钥字符串
     * @return : 公钥
     */
    public static PublicKey loadPublicKeyFromString(String algorithm, String keyString) throws Exception {
        // 进行Base64解码
        byte[] decode = Base64.decode(keyString);
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        // 构建密钥规范
        X509EncodedKeySpec keyspec = new X509EncodedKeySpec(decode);
        // 获取公钥
        return keyFactory.generatePublic(keyspec);

    }

    /**
     * 从文件中加载私钥
     *
     * @param algorithm : 算法
     * @param filePath  : 文件路径
     * @return : 私钥
     */
    public static PrivateKey loadPrivateKeyFromFile(String algorithm, String filePath) throws Exception {
        // 将文件内容转为字符串
        String keyString = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        return loadPrivateKeyFromString(algorithm, keyString);
    }

    /**
     * 从字符串中加载私钥
     *
     * @param algorithm : 算法
     * @param keyString : 私钥字符串
     * @return : 私钥
     */
    public static PrivateKey loadPrivateKeyFromString(String algorithm, String keyString) throws Exception {
        // 进行Base64解码
        byte[] decode = Base64.decode(keyString);
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        // 构建密钥规范
        PKCS8EncodedKeySpec keyspec = new PKCS8EncodedKeySpec(decode);
        // 生成私钥
        return keyFactory.generatePrivate(keyspec);
    }
    /**
     * 生成签名
     *
     * @param input      : 原文
     * @param algorithm  : 算法
     * @param privateKey : 私钥
     * @return : 签名
     */
    private static String getSignature(String input, String algorithm, PrivateKey privateKey) throws Exception {
        // 获取签名对象
        Signature signature = Signature.getInstance(algorithm);
        // 初始化签名
        signature.initSign(privateKey);
        // 传入原文
        signature.update(input.getBytes());
        // 开始签名
        byte[] sign = signature.sign();
        // 对签名数据进行Base64编码
        return Base64.encode(sign);
    }

    /**
     * 校验签名
     *
     * @param input          : 原文
     * @param algorithm      : 算法
     * @param publicKey      : 公钥
     * @param signaturedData : 签名
     * @return : 数据是否被篡改
     */
    private static boolean verifySignature(String input, String algorithm, PublicKey publicKey, String signaturedData) throws Exception {
        // 获取签名对象
        Signature signature = Signature.getInstance(algorithm);
        // 初始化签名
        signature.initVerify(publicKey);
        // 传入原文
        signature.update(input.getBytes());
        // 校验数据
        return signature.verify(Base64.decode(signaturedData));

    }
}