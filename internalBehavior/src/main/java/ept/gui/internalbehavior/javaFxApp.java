package ept.gui.internalbehavior;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class javaFxApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 800, 600);
        Stage stage = new Stage();
        stage.setScene(scene);

        stage.show();
    }
}
