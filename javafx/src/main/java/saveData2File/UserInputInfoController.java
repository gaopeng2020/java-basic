package saveData2File;

import ept.commonapi.EPTUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class UserInputInfoController {
    private File file;
    @Getter
    private UserInputInfo userInputInfo;

    /**
     *
     */
    public UserInputInfoController(String fileName) {
        file = getOrCreateFile(fileName);
        if (file.length() == 0) {
            this.userInputInfo = new UserInputInfo("", "", "", "", "", "");
        } else {
            readUserData();
        }
    }

    private void readUserData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(this.file))) {
            this.userInputInfo = (UserInputInfo) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("Read object from file caught an exception {}", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }


    private void writeUserData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(this.file))) {
            oos.writeObject(this.userInputInfo);
            oos.flush();
        } catch (IOException e) {
            log.error("Write object to file caught an exception {}", Arrays.toString(e.getStackTrace()));
            throw new RuntimeException(e);
        }
    }

    /**
     * getOrCreateFile
     *
     * @param fileName file name
     * @return file
     */
    private File getOrCreateFile(String fileName) {
        File dir = new File("./temp");
        if (!dir.exists()) {
            dir.mkdir();
        }
        String canonicalPath;
        try {
            canonicalPath = dir.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                .filter(tmpFile -> tmpFile.getName().equals(fileName))
                .findAny().orElseGet(() -> {
                    String path = canonicalPath + EPTUtils.SLASH + fileName;
                    try {
                        this.file = new File(path);
                        this.file.createNewFile();
                        log.info("created new file : {}", file.getAbsoluteFile());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return this.file;
                });
    }
}
