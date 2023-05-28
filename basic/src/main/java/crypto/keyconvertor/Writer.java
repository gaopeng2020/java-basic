package crypto.keyconvertor;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECPoint;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

/**
 *
 * <p>
 * (c) Copyright Vector Informatik GmbH. All Rights Reserved.
 * </p>
 *
 * @since 1.0
 */
public class Writer {

    /**
     * @brief The format of the key loaded.
     */
    private static String format = "pkcs8_encrypted";

    /**
     * @brief The location of the input file.
     */
    private static String path = ".";

    /**
     * @brief Constant for generator UID upper part.
     */
    private static final Integer kGeneratorUIDQwordMS = 18;

    /**
     * @brief Constant for generator UID lower part.
     */
    private static final Integer kGeneratorUIDQwordLS = 7;

    /**
     * @brief The algorithm that was used in imported key. 'RSA' or 'ECDSA'
     */
    private static String algorithm = ".";

    /**
     * @brief The size of key in bits that was used in imported key.
     */
    private static int keySize = -1;
    /**
     * @brief Reads in a key file and outputs a pair of public and private key on the console.
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        printHeader();

        // Parse command line arguments
        parseArguments(args);

        // Write the keys
        try {
            writeKeys(format, path);

        } catch (final NoSuchAlgorithmException e) {
            System.err.println("Caught NoSuchAlgorithmException: " + e.getMessage());
        } catch (final InvalidKeySpecException e) {
            System.err.println("Caught InvalidKeySpecException: " + e.getMessage());
        } catch (final InvalidKeyException e) {
            System.err.println("Caught InvalidKeyException: " + e.getMessage());
        } catch (final IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
    }

    /**
     * @brief Writes a pair of private and public keys.
     * @param format The format of the key loaded.
     * @param path The location of the input file.
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    static private void writeKeys(final String format, final String path) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException {
        System.out.println("-------------------------------------------------------------\n");
        System.out.println("Loading key with\n" +
                "  format " + format + "\n" +
                "  from " + path + "\n");

        // Get key from input file
        final Object key = Reader.read(format, path, algorithm);
        final Integer privateStamp = Math.abs(path.hashCode()) + 2;
        final Integer publicStamp = Math.abs(path.hashCode()) + 3;
        final UUID genUUID = new UUID((long)kGeneratorUIDQwordMS,(long)kGeneratorUIDQwordLS );
        // Load JSON key templates to fill
        String privateKey = Template.loadTemplate();
        String publicKey = Template.loadTemplate();

        if (format.equals("pkcs8_encrypted") ||
                format.equals("pkcs8_unencrypted")) {
            // Get data from key
            if (key instanceof RSAPrivateKey) {
                System.out.println("Importing an RSA private key...");

                final BigInteger keyModulus = ((RSAPrivateKey) key).getModulus();
                final BigInteger keyPrivateExponent = ((RSAPrivateKey) key).getPrivateExponent();
                final BigInteger keyPublicExponent = ((RSAPrivateCrtKey) key).getPublicExponent();

                // Fill data of private key
                privateKey = privateKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>VERSION_STAMP", privateStamp.toString());
                privateKey = privateKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>GENERATOR_UID", genUUID.toString());


                privateKey = privateKey.replace("SLOT_PAYLOAD>KEY_BIT_LENGTH", Integer.toString(keySize));
                privateKey = privateKey.replace("SLOT_PAYLOAD>KEY_TYPE", "\"PrivateKey\"");
                privateKey = privateKey.replace("SLOT_CONTENT_PROPS>ALG_ID", "\"RSA-" + Integer.toString(keySize) + "\"");

                privateKey = InsertRSAKey(keyModulus.toString(16), keyPrivateExponent.toString(16), privateKey, true);
                privateKey = Template.replaceValues(privateKey);

                // Fill data of public key
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>VERSION_STAMP", publicStamp.toString());
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>GENERATOR_UID", genUUID.toString());
                publicKey = publicKey.replace("SLOT_PAYLOAD>KEY_BIT_LENGTH",Integer.toString(keySize));
                publicKey = publicKey.replace("SLOT_PAYLOAD>KEY_TYPE", "\"PublicKey\"");
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>ALG_ID", "\"RSA-" + Integer.toString(keySize) + "\"");

                publicKey = InsertRSAKey(keyModulus.toString(16),keyPublicExponent.toString(16),publicKey, false);

                publicKey = Template.replaceValues(publicKey);
            } else if (key instanceof RSAPublicKey) {
                System.out.println("Importing an RSA public key...");
                final BigInteger keyModulus = ((RSAPublicKey) key).getModulus();
                final BigInteger keyPublicExponent = ((RSAPublicKey) key).getPublicExponent();
                // Fill data of public key
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>VERSION_STAMP", publicStamp.toString());
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>GENERATOR_UID", genUUID.toString());


                publicKey = publicKey.replace("SLOT_PAYLOAD>KEY_BIT_LENGTH", Integer.toString(keySize));
                publicKey = publicKey.replace("SLOT_PAYLOAD>KEY_TYPE", "\"PublicKey\"");
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>ALG_ID", "\"RSA-" + Integer.toString(keySize) + "\"");

                publicKey = InsertRSAKey(keyModulus.toString(16), keyPublicExponent.toString(16), publicKey, false);

                publicKey = Template.replaceValues(publicKey);
            } else if (key instanceof ECPrivateKey) {
                System.out.println("Importing an Elliptic curve private key...");

                final BigInteger privateKeyS = ((ECPrivateKey) key).getS();

                // Fill data of private key
                privateKey = privateKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>VERSION_STAMP", privateStamp.toString());
                privateKey = privateKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>GENERATOR_UID", genUUID.toString());
                privateKey = privateKey.replace("SLOT_PAYLOAD>KEY_BIT_LENGTH", Integer.toString(keySize));
                privateKey = privateKey.replace("SLOT_PAYLOAD>KEY_TYPE", "\"PrivateKey\"");
                privateKey = privateKey.replace("SLOT_CONTENT_PROPS>ALG_ID", "\"PRIVATE_KEY_ECC/NIST_P-" + Integer.toString(keySize) + "\"");
                privateKey = InsertPrivateECKey(privateKeyS.toString(16),privateKey);

                privateKey = Template.replaceValues(privateKey);
            } else if (key instanceof ECPoint) {

                // Fill data of public key
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>VERSION_STAMP", publicStamp.toString());
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>OBJECT_UID>GENERATOR_UID", genUUID.toString());
                publicKey = publicKey.replace("SLOT_PAYLOAD>KEY_BIT_LENGTH", Integer.toString(keySize));
                publicKey = publicKey.replace("SLOT_PAYLOAD>KEY_TYPE", "\"PublicKey\"");
                publicKey = publicKey.replace("SLOT_CONTENT_PROPS>ALG_ID", "\"PUBLIC_KEY_ECC/NIST_P-" + Integer.toString(keySize) + "\"");

                BigInteger public_x = ((ECPoint) key).getAffineX();
                BigInteger public_y = ((ECPoint) key).getAffineY();

                if(keySize == 256) {
                    publicKey = InsertPublicECKey(String.format("%032x",public_x), String.format("%032x",public_y), publicKey);
                } else if (keySize == 384) {
                    publicKey = InsertPublicECKey(String.format("%048x",public_x), String.format("%048x",public_y), publicKey);
                } else if (keySize == 521) {
                    publicKey = InsertPublicECKey(String.format("%066x",public_x), String.format("%066x",public_y), publicKey);
                }

                publicKey = Template.replaceValues(publicKey);
            }
            // Output both keys
            if (! (key instanceof ECPoint) && !(key instanceof RSAPublicKey)) {
                /* ECPoint (or EC public key) does not have private key parts. Neither does RSA Public keys. Thus printing is suppressed
                 */
            System.out.println("\n-------------------------------------------------------------\n");
            System.out.println("Generating private key...");
            System.out.println("Note: The following key in JSON notation is missing values.");
            System.out.println("      Please provide those by replacing <YOUR_VALUE_HERE>.\n");
            System.out.println(privateKey);
            }

            if (! (key instanceof ECPrivateKey)) {
                /* ECPrivateKey does not have public key parts (need an asn.1 decoder for that). Thus printing is suppressed
                */
            System.out.println("\n-------------------------------------------------------------\n");
            System.out.println("Generating public key...");
            System.out.println("Note: The following key in JSON notation is missing values.");
            System.out.println("      Please provide those by replacing <YOUR_VALUE_HERE>.\n");
            System.out.println(publicKey);
            }
        }
    }
    /**
     * @brief Replaces RSA values of the template.
     * @param keyTemplate The template to replace values for.
     * @param keyModulus The modulus to insert.
     * @param rsaExponent The exponent to insert.
     * @return The adjusted template.
     */
    public static String InsertRSAKey(String keyModulus, String rsaExponent, String keyTemplate, boolean priv) {
        String processed = keyTemplate;

        if (priv) {
            processed = processed.replace("\"e\": \"SLOT_PAYLOAD>KEY_EXPONENT\",", "");
        } else {
            processed = processed.replace("\"d\": \"SLOT_PAYLOAD>KEY_EXPONENT\",", "");
        }

        processed = processed.replace("SLOT_PAYLOAD>KEY_MODULUS", keyModulus);
        processed = processed.replace("SLOT_PAYLOAD>KEY_EXPONENT", rsaExponent);

        /* Remove ECC fields from template : */
        processed = processed.replaceAll("\"x\": \"SLOT_PAYLOAD>KEY_X_PUBLIC\",", "");
        processed = processed.replace("\"y\": \"SLOT_PAYLOAD>KEY_Y_PUBLIC\",", "");
        processed = processed.replace("\"d\": \"SLOT_PAYLOAD>PRIVATE_KEY\"", "");
        return processed;
    }

    /**
     * @brief Replaces EC public values of the template.
     * @param keyTemplate The template to replace values for.
     * @param publicKeyX The public X number in elliptic curve.
     * @param publicKeyY The public Y number in elliptic curve.
     * @return The adjusted template.
     */
    public static String InsertPublicECKey(String publicKeyX, String publicKeyY, String keyTemplate) {
        String processed = keyTemplate;
        processed = processed.replace("SLOT_PAYLOAD>KEY_X_PUBLIC", publicKeyX);
        processed = processed.replace("SLOT_PAYLOAD>KEY_Y_PUBLIC", publicKeyY);

        /* Remove RSA fields from template : */
        processed = processed.replace("\"modulus\": \"SLOT_PAYLOAD>KEY_MODULUS\",", "");
        processed = processed.replace("\"e\": \"SLOT_PAYLOAD>KEY_EXPONENT\",", "");
        processed = processed.replace("\"d\": \"SLOT_PAYLOAD>KEY_EXPONENT\",", "");

        /* Remove EC private key fields from template: */
        processed = processed.replace("\"d\": \"SLOT_PAYLOAD>PRIVATE_KEY\"", "");

        return processed;
    }
    /**
     * @brief Replaces EC values of the template.
     * @param keyTemplate The template to replace values for.
     * @param privateKey The private key number.
     * @return The adjusted template.
     */
    public static String InsertPrivateECKey(String privateKey, String keyTemplate) {
        String processed = keyTemplate;
        processed = processed.replace("SLOT_PAYLOAD>PRIVATE_KEY", privateKey);

        /* Remove RSA fields from template : */
        processed = processed.replace("\"modulus\": \"SLOT_PAYLOAD>KEY_MODULUS\",", "");
        processed = processed.replace("\"e\": \"SLOT_PAYLOAD>KEY_EXPONENT\",", "");
        processed = processed.replace("\"d\": \"SLOT_PAYLOAD>KEY_EXPONENT\",", "");

        /* Remove EC public key fields from template: */
        processed = processed.replace("\"x\": \"SLOT_PAYLOAD>KEY_X_PUBLIC\",", "");
        processed = processed.replace("\"y\": \"SLOT_PAYLOAD>KEY_Y_PUBLIC\",", "");
        return processed;
    }
    /**
     * @brief Parses the command line arguments.
     * @param args The given arguments.
     */
    static private void parseArguments(final String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (!(args[i].equals("-h") || args[i].equals("-p") || args[i].equals("-f") || args[i].equals("-a")|| args[i].equals("-b"))) {
                printHelpMessage();
                System.exit(1);
            } else if (args[i].equals("-h")) {
                printHelpMessage();
                System.exit(0);
            } else if (args[i].equals("-p") && (i + 1) < args.length) {
                path = args[i + 1];
                i++;
            } else if (args[i].equals("-f") && (i + 1) < args.length) {
                if (args[i + 1].equals("pkcs8_encrypted") ||
                        args[i + 1].equals("pkcs8_unencrypted")) {
                    format = args[i + 1];
                    i++;
                } else {
                    System.out.println("Invalid input file format!\n" +
                            "Options are: [pkcs8_encrypted], [pkcs8_unencrypted]");
                    System.exit(1);
                }
            } else if (args[i].equals("-a") && (i + 1) < args.length) {
                if (args[i + 1].equals("RSA") ||
                        args[i + 1].equals("EC")) {
                    algorithm = args[i + 1];
                    i++;
                } else {
                    System.out.println("Invalid algorithm. Only 'RSA' and 'EC' is supported!\n" +
                            "Options are: [RSA], [EC]");
                    System.exit(1);
                }
            } else if (args[i].equals("-b") && (i + 1) < args.length) {
                keySize = Integer.parseInt(args[i + 1]);
                i++;
            }
        }
        // Check if path was set
        if (path.equals(".")) {
            System.out.println("No input file provided!\n");
            printHelpMessage();
            System.exit(1);
        }
        // Check if algorithm was set
        if (algorithm.equals(".")) {
            System.out.println("Algorithm not specified!\n");
            printHelpMessage();
            System.exit(1);
        }
        if (keySize == -1){
            System.out.println("keySize not specified!\n");
            printHelpMessage();
            System.exit(1);
        }
        if (algorithm.equals("EC")) {
            if (keySize != 256 && keySize != 384 && keySize != 521) {
                System.out.println("Only key lengths of 256 bits (nist-p 256 curve), 384 bits (nist-p 384 curve) and 521 bits (nist-p 521 curve) are supported.\n");
                printHelpMessage();
                System.exit(1);
            }
        }
    }

    /**
     * @brief Prints the help message on the console.
     */
    static private void printHelpMessage() {
        System.out.println(
                "Usage: keycon [-h] -p <path> [-f <format>]\n" +
                        "-h                       Print this message and exit.\n" +
                        "-p <path>                Specify the location of the input file.\n" +
                        "-a [RSA | EC]            Specify if the key is an RSA key or an Elliptic Curve key.\n" +
                        "-f <format>              Format of the input file. Default is encrypted PKCS#8.\n"+
                        "-b <bits>                Size of key imported in number of bits.\n");
    }

    /**
     * @brief Prints the copyright note and the disclaimer on the console.
     */
    static private void printHeader() {
        System.out.println("(c) Copyright Vector Informatik GmbH. All Rights Reserved.\n" +
                "KeyConverter for the AMSR Crypto Stack.\n");

        System.out.println("DISCLAIMER\n" +
                "This software is a draft implementation and is given to you\n" +
                "without laying any claim to correctness and completeness.\n" +
                "As such all functionality is subject to change. For questions\n" +
                "please refer to the respective technical reference.\n");
    }
}
