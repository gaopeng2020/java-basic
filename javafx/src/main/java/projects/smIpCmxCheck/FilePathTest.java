package projects.smIpCmxCheck;

import ept.commonapi.EPTUtils;
import ept.excelReader.ept.soa.EptCpMatrixReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class FilePathTest {
    public static void main(String[] args) throws IOException {
        String protocol = Objects.requireNonNull(EptCpMatrixReader.class.getResource("")).getProtocol();
        System.out.println("protocol = " + protocol);
        String filePath = "resource/matrix/Ethernet_someip_cmx_template_2.1.0.xlsx";
        if ("jar".equals(protocol)) {
            InputStream is = EptCpMatrixReader.class.getClassLoader().getResourceAsStream(filePath);
            int length = is.readAllBytes().length;
            System.out.println("length = " + length);
//            System.out.println("jar path = " + path);
        } else {
            System.out.println("path = " + EptCpMatrixReader.class.getResource(""));
        }

        File directory = new File("./temp");
        if (!directory.exists()) {
            directory.mkdir();
        }
        String canonicalPath = directory.getCanonicalPath();
        AtomicReference<File> cmxFile = new AtomicReference<>();
        File cmdFile = Arrays.stream(Objects.requireNonNull(directory.listFiles())).filter(file -> file.getName().equals("cmx.txt"))
                .findAny().orElseGet(() -> {
                    String s = canonicalPath + EPTUtils.SLASH + "cmx.txt";
                    File file;
                    try {
                        file = new File(s);
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return file;
                });

        String property = System.getProperty("user.dir");
        System.out.println("property = " + property);
    }
}
