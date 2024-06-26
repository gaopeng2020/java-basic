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
    requires org.apache.poi.ooxml;
    requires org.jfxtras.styles.jmetro;
    requires fxribbon;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.logging.log4j.slf4j.impl;

    opens projects.internalbehavior to javafx.fxml;
    exports projects.internalbehavior;

    opens projects.smIpCmxCheck to javafx.fxml;
    exports projects.smIpCmxCheck;

    opens demos.javaFxDemo1 to javafx.fxml;
    exports demos.javaFxDemo1;

    opens demos.javaFxDemo2 to javafx.fxml;
    exports demos.javaFxDemo2;

    opens demos.javaFxDemo3 to javafx.fxml;
    exports demos.javaFxDemo3;

    opens demos.javaFxDemo4 to javafx.fxml;
    exports demos.javaFxDemo4;

    opens demos.RibbonDemos to javafx.fxml;
    exports demos.RibbonDemos;

    opens projects.exportSwDocs to javafx.fxml;
    exports projects.exportSwDocs;
}