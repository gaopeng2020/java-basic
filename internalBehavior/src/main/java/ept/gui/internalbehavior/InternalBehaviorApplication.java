package ept.gui.internalbehavior;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class InternalBehaviorApplication extends Application {
    /**
     * @param stage stage
     * @throws Exception fxmlLoader.load Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("internal-behavior.fxml"));
        System.out.println("fxmlLoader.getLocation() = " + fxmlLoader.getLocation());
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);
        stage.setTitle("Internal Behavior Definition");
        stage.getIcons().add(new Image("file:/icons/appnet.png",48,48,true,true));
        stage.setIconified(true);
        stage.setScene(scene);
        stage.setMinWidth(600.0);
        stage.setMinHeight(400.0);
        stage.initStyle(StageStyle.DECORATED);
//        stage.initModality(Modality.NONE);
        stage.setOpacity(0.98);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
