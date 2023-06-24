package socket.tcpdemo.FileTransfer;

import java.io.Serializable;
import java.util.Arrays;

public class FileTransferStruct implements Serializable {
   public String filename;
    public long fileSize;
    public byte[] fileContent;
    public String md5Digest;

    @Override
    public String toString() {
        return "FileTransferStruct{" +
                "filename='" + filename + '\'' +
                ", fileSize=" + fileSize +
                ", fileContent=" + Arrays.toString(fileContent) +
                ", md5Digest='" + md5Digest + '\'' +
                '}';
    }
}
