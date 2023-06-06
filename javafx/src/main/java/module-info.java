module javafxdemos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires java.base;
    requires static lombok;
    requires static eptutils;
    requires excelhandler;
    requires java.desktop;
    requires org.apache.poi.poi;


    opens projects.internalbehavior to javafx.fxml;
    exports projects.internalbehavior;

    opens demos.javaFxDemo1 to javafx.fxml;
    exports demos.javaFxDemo1;

    opens demos.javaFxDemo2 to javafx.fxml;
    exports demos.javaFxDemo2;
}