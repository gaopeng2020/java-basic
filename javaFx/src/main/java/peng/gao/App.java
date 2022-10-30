package peng.gao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Hello world!
 */
public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader  fxmlLoader = new FXMLLoader(App.class.getResource("hello-view.fxml"));
        System.out.println("fxmlLoader.getLocation() = " + fxmlLoader.load());

        AnchorPane anchorPane = new AnchorPane();
        Button button1 = new Button();
//        button1.setPrefHeight(100);
        button1.setText("Button1");
        anchorPane.getChildren().addAll(button1);
        AnchorPane.setTopAnchor(button1,100.0);
        AnchorPane.setLeftAnchor(button1,100.0);

//        Scene scene =  new Scene(fxmlLoader.load(), 800, 600);
        Scene scene =  new Scene(anchorPane, 800, 600);
        stage.setTitle("Hello!");
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED);
        stage.show();
    }
}
