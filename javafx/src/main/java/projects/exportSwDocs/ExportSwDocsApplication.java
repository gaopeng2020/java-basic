package projects.exportSwDocs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ExportSwDocsApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ExportSwDocsApplication.class.getResource("ExportSwDocsView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Export System Weaver Document");
        stage.setScene(scene);
        InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream("/projects/exportSwDocs/logo.ico"));
        Image image = new Image(inputStream);
        stage.getIcons().add(image);

        Platform.setImplicitExit(true);

        //关闭时存储用户数据
        stage.setOnCloseRequest(windowEvent -> {
            ExportSwDocsController controller = fxmlLoader.getController();
            controller.writeUserDataToFile();
            try {
                stage.close();
                stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}