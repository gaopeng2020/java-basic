package gui.qt;

import io.qt.uic.Main;
import io.qt.widgets.QApplication;
import io.qt.widgets.QMessageBox;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author gaopeng
 */
public class Tutorial1 {
    public static void main(String[] args) {
//        String libPath = "D:\\Users\\ProgramFiles\\DeveloperTools\\maven-3.8.6\\repossitory\\io\\qtjambi\\qtjambi-native-windows-x64\\5.15.3\\lib";
//        try {
//            addLibraryDir(libPath);
//        } catch (IOException e) {
//            System.out.println(Arrays.toString(e.getStackTrace()));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        System.setProperty("java.library.path","libPath");


        QApplication.initialize(args);
//        QPushButton hello = new QPushButton("Hello World!");
//        hello.resize(120, 40);
//        hello.show();
        QMessageBox.information(null, "QtJambi", "Hello World!");

//        QUiLoader loader = new QUiLoader();
//        QFile qFile = new QFile("C:\\Users\\gaopeng\\Documents\\Qt\\qt-solutions-master\\qtwinmigrate\\examples\\qtdll\\form.ui");
//        qFile.open(QIODevice.OpenModeFlag.ReadOnly);
//        QWidget widget = loader.load(qFile);
//        widget.show();
//        qFile.close();

        QApplication.exec();
        QApplication.shutdown();
    }

    public static void addLibraryDir(String libraryPath) throws IOException {
        try {
//            Field field = ClassLoader.class.getDeclaredField("usr_paths");
            Field field = ClassLoader.class.getDeclaredField("sys_paths");
            field.setAccessible(true);
            String[] paths = (String[]) field.get(null);
            int pathLength = paths.length;
            for (int i = 0; i < pathLength; i++) {
                System.out.println("paths[" + i + "] = " + paths[i]);
                if (libraryPath.equals(paths[i])) {
                    return;
                }
            }

            String[] tmp = new String[pathLength + 1];
            System.arraycopy(paths, 0, tmp, 0, pathLength);
            tmp[pathLength] = libraryPath;
            field.set(null, tmp);
        } catch (IllegalAccessException e) {
            throw new IOException("Failed to get permissions to set library path");
        } catch (NoSuchFieldException e) {
            throw new IOException("Failed to get field handle to set library path");
        }
    }

}
