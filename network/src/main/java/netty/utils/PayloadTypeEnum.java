package netty.utils;

/**
 * @author gaopeng
 */

public enum PayloadTypeEnum {
    /**
     * PayloadType is string
     */
    STRING_ECHO,
    /**
     * PayloadType is file
     */
    UPLOAD_FILE,
    /**
     * PayloadType is file
     */
    DOWNLOAD_FILE,

    /**
     * TEXT_CHAT function
     */
    TEXT_CHAT,
    /**
     * PayloadType is invalid
     */
    INVALID
}
