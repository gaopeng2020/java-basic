package crypto.keyconvertor;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECPoint;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;

/**
 *
 * <p>
 * (c) Copyright Vector Informatik GmbH. All Rights Reserved.
 * </p>
 *
 * @since 1.0
 */
public class Reader {

    /**
     * @brief Reads a private key from a path in given format.
     * @param format The input file format.
     * @param path Location of the input file.
     * @return A private key.
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public static Object read(final String format, final String path,final String algorithm) throws IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException {
        // Load key
        String key = new String(Files.readAllBytes(Paths.get(path)));

        // Remove tag
        if (format.equals("pkcs8_encrypted")) {

            key = key.replace("-----BEGIN ENCRYPTED PRIVATE KEY-----", "");
            key = key.replace("-----END ENCRYPTED PRIVATE KEY-----", "");
            final EncryptedPrivateKeyInfo privateKeyInfo = new EncryptedPrivateKeyInfo(Base64.getMimeDecoder().decode(key));

            // Get password
            final Scanner reader = new Scanner(System.in);
            System.out.println("Enter the password for given file: ");
            final String password = reader.next();
            reader.close();

            final PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray());
            final SecretKeyFactory pbeKeyFactory = SecretKeyFactory.getInstance(privateKeyInfo.getAlgName());
            final PKCS8EncodedKeySpec encodedKeySpec = privateKeyInfo.getKeySpec(pbeKeyFactory.generateSecret(pbeKeySpec));
            final KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            return (Key) keyFactory.generatePrivate(encodedKeySpec);

        } else if (format.equals("pkcs8_unencrypted")) {
            if (key.contains("-----BEGIN PUBLIC KEY-----")){
                /* importing a public key */
                key = key.replace("-----BEGIN PUBLIC KEY-----", "");
                key = key.replace("-----END PUBLIC KEY-----", "");
                final byte[] encoded = Base64.getMimeDecoder().decode(key);
                /* try to extract it as a RSA public key */
                if (algorithm.equals("RSA")) {
                    final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
                    final KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                    return (Key) keyFactory.generatePublic(keySpec);
                }
                /* if it is an EC key,  as per asn.1 structure */
                byte[] pubkey_bytes = Arrays.copyOfRange(encoded,24,encoded.length);

                /* public keys are prefixed with 0x04 as per https://tools.ietf.org/html/rfc5480#section-2.2 */
                if (encoded[23] != 0x04) {
                    if (encoded.length > 144 ) /* for nist-p 521 bit keys, the 0x04 mark lies 2 bytes later */
                    {
                        if (encoded[25] != 0x04){
                            throw new InvalidKeyException("only uncompressed public keys are accepted.");
                        } else {
                            pubkey_bytes = Arrays.copyOfRange(encoded,26,encoded.length);
                        }
                    } else {
                        throw new InvalidKeyException("only uncompressed public keys are accepted.");
                    }
                }

                if (pubkey_bytes.length == 64){
                    /* Key happens to be 256 bit (64 bytes for x and y cordinate.
                                                  32 bytes per coordinate => 32*8 => 256 bits)
                    */
                    final byte[] x_cordinate = Arrays.copyOfRange(pubkey_bytes, 0, 32);
                    final byte[] y_cordinate = Arrays.copyOfRange(pubkey_bytes, 32, 32+32);
                    return new ECPoint(new BigInteger(1, x_cordinate), new BigInteger(1, y_cordinate));
                } else if (pubkey_bytes.length == 96){
                    /* Key happens to be 384 bit (96 bytes for x and y cordinate.
                                48 bytes per coordinate => 48*8 => 384 bits)
                    */
                    final byte[] x_cordinate = Arrays.copyOfRange(pubkey_bytes, 0, 48);
                    final byte[] y_cordinate = Arrays.copyOfRange(pubkey_bytes, 48, 48+48);
                    return new ECPoint(new BigInteger(1, x_cordinate), new BigInteger(1, y_cordinate));
                } else if (pubkey_bytes.length == 132){
                    /* Key happens to be 521 bit (132 bytes for x and y cordinate.
                                66 bytes per coordinate => 66*8 => 528 bits, with 7 bits of leading zeros)
                    */
                    final byte[] x_cordinate = Arrays.copyOfRange(pubkey_bytes, 0, 66);
                    final byte[] y_cordinate = Arrays.copyOfRange(pubkey_bytes, 66, 66+66);
                    return new ECPoint(new BigInteger(1, x_cordinate), new BigInteger(1, y_cordinate));
                } else {
                    throw new InvalidKeyException("encoded size error encoded.length = " + pubkey_bytes.length );
                }

            } else {
                key = key.replace("-----BEGIN PRIVATE KEY-----", "");
                key = key.replace("-----END PRIVATE KEY-----", "");
                final byte[] encoded = Base64.getMimeDecoder().decode(key);
                final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
                final KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
                return (Key) keyFactory.generatePrivate(keySpec);
            }
        } else {
            throw new InvalidKeyException();
        }
    }
}