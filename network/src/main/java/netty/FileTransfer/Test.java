package netty.FileTransfer;

import java.io.File;
import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        int start = 0;
//        File file = getFieByName("test01.txt");
//        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());


//        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw")) {
//            byte[] bytes = "skdgfejlgn;ehfiylwgfiuhihwioe234inskdflsdfn;dfjhpoertjhklshdfiowyw8f\n".getBytes(StandardCharsets.UTF_8);
//            //移动至start位置开始写入文件
//            start += bytes.length;
//            randomAccessFile.seek(start);
//            randomAccessFile.write(bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }

    private static File getFieByName(String fileName) {
        File userHome = new File(System.getProperty("user.home"));
        File[] files = userHome.listFiles(pathname -> pathname.isDirectory() && pathname.getName().equals("Downloads"));
        File dir;
        if (files == null) {
            dir = new File(userHome, "Downloads");
        } else {
            dir = files[0];
        }

        File[] fileNames = dir.listFiles(name -> name.getName().equals(fileName));
        if (fileNames == null || fileNames.length == 0) {
            return new File(dir, fileName);
        } else {
            return fileNames[0];
        }
    }
}
