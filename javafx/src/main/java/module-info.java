module javafxdemos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires java.base;
    requires lombok;
    requires java.desktop;


    opens projects.internalbehavior to javafx.fxml;
    exports projects.internalbehavior;

    opens demos.javaFxDemo1 to javafx.fxml;
    exports demos.javaFxDemo1;

}