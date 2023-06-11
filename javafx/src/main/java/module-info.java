module javafxdemos {
    requires java.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires static lombok;
    requires static eptutils;
    requires excelhandler;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.jfxtras.styles.jmetro;
    requires fxribbon;


    opens projects.internalbehavior to javafx.fxml;
    exports projects.internalbehavior;

    opens projects.smIpCmxCheck to javafx.fxml;
    exports projects.smIpCmxCheck;

    opens demos.javaFxDemo1 to javafx.fxml;
    exports demos.javaFxDemo1;

    opens demos.javaFxDemo2 to javafx.fxml;
    exports demos.javaFxDemo2;

    opens demos.RibbonDemos to javafx.fxml;
    exports demos.RibbonDemos;
}