package gui.qt;

import io.qt.widgets.QApplication;
import io.qt.widgets.QMessageBox;
import io.qt.widgets.QPushButton;

/**
 * @author gaopeng
 */
public class Tutorial1 {
    public static void main(String[] args) {
        QApplication.initialize(args);
//        QPushButton hello = new QPushButton("Hello World!");
//        hello.resize(120, 40);
//        hello.show();
        QMessageBox.information(null, "QtJambi", "Hello World!");
        QApplication.exec();
    }
}
