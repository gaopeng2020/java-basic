module peng.gao.fxdemo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.desktop;

    opens peng.gao.fxdemo1 to javafx.fxml;
    exports peng.gao.fxdemo1;
}