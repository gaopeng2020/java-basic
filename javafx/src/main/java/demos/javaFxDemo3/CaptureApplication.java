package demos.javaFxDemo3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.io.InputStream;
import java.util.Objects;

public class CaptureApplication extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(CaptureApplication.class.getResource("snapping-tool-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 562.5);
        stage.setScene(scene);

        new JMetro(scene, Style.LIGHT);

        InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream("/demos/javaFxDemo3/icons8_windows_snipping_tool_16.png"));
        Image image = new Image(inputStream);
        stage.getIcons().add(image);
        stage.setTitle("snipping tool");

        stage.show();
    }
}
