package crypto.keyconvertor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 *
 * <p>
 * (c) Copyright Vector Informatik GmbH. All Rights Reserved.
 * </p>
 *
 * @since 1.0
 */
public class Template {

    /**
     * @brief Reads the key file template.
     * @return The key file template as string.
     * @throws IOException
     */
    static String loadTemplate() throws IOException {

        final InputStream inputStream = Writer.class.getResourceAsStream("crypto/template.tpl");
        if (inputStream == null) {
            throw new IOException("Key slot template not found");
        }
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8.name()))) {
            return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    /**
     * @brief Replaces undefined values of the template.
     * @param keyTemplate The template to replace values for.
     * @return The adjusted template.
     */
    public static String replaceValues(String keyTemplate) {
        // Fill slot content props
        keyTemplate = keyTemplate.replace("SLOT_CONTENT_PROPS>OBJECT_UID>VERSION_STAMP", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_CONTENT_PROPS>OBJECT_UID>GENERATOR_UID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_CONTENT_PROPS>DEPENDENCY_UID>VERSION_STAMP", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_CONTENT_PROPS>DEPENDENCY_UID>GENERATOR_UID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_CONTENT_PROPS>ALG_ID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_CONTENT_PROPS>OBJECT_SIZE", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_CONTENT_PROPS>OBJECT_TYPE", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_CONTENT_PROPS>EXPORTABILITY", "<YOUR_VALUE_HERE>");

        // Fill slot prototype props
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>VERSION_CONTROL", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>DEPENDENCY_TYPE", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>OBJECT_TYPE", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>SLOT_CAPACITY", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>ALG_ID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>VERSION_TRACK>VERSION_STAMP", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>VERSION_TRACK>GENERATOR_UID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>OWNER_UID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>CRYPTO_PROVIDER_UID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>LOGICAL_SLOT_UID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>EXPORTABILITY", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>DEPENDENCY_COUID_GEN_UUID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PROTOTYPE_PROPS>DEPENDENCY_COUID_VERSION", "<YOUR_VALUE_HERE>");

        // Fill slot payload
        keyTemplate = keyTemplate.replace("SLOT_PAYLOAD>KEY_BIT_LENGTH", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PAYLOAD>PUBLIC", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PAYLOAD>KEY_TYPE", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PAYLOAD>KEY_MODULUS", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("SLOT_PAYLOAD>KEY_EXPONENT", "<YOUR_VALUE_HERE>");

        // Fill related key uid
        keyTemplate = keyTemplate.replace("RELATED_KEY_UID>VERSION_STAMP", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("RELATED_KEY_UID>GENERATOR_UID", "<YOUR_VALUE_HERE>");

        // Fill related key type
        keyTemplate = keyTemplate.replace("RELATED_KEY_TYPE", "<YOUR_VALUE_HERE>");

        // Fill acl
        keyTemplate = keyTemplate.replace("ACL>ACTOR_UID", "<YOUR_VALUE_HERE>");
        keyTemplate = keyTemplate.replace("ACL>ALLOWED_USAGE", "<YOUR_VALUE_HERE>");

        return keyTemplate;
    }
}
