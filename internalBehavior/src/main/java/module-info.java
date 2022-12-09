module ept.gui.internalbehavior {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires java.base;
    requires lombok;
    requires java.desktop;


    opens ept.gui.internalbehavior to javafx.fxml;
    exports ept.gui.internalbehavior;
    opens ept.gui.javaFxDemo1 to javafx.fxml;
    exports ept.gui.javaFxDemo1;
}