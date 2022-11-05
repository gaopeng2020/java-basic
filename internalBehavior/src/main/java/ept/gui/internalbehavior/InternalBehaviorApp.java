package ept.gui.internalbehavior;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class InternalBehaviorApp {
    static CountDownLatch latch = new CountDownLatch(1);
    public static Thread currentThread;

    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(InternalBehaviorApplication.class.getResource("internal-behavior.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 1500, 800);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setTitle("Internal Behavior Definition");
        stage.getIcons().add(new Image("file:/icons/appnet.png", 48, 48, true, true));
//        stage.setIconified(true);
        stage.setScene(scene);
        stage.setMinWidth(600.0);
        stage.setMinHeight(400.0);
        stage.initStyle(StageStyle.DECORATED);
//        stage.initModality(Modality.NONE);
        stage.setOpacity(0.98);
//        stage.setOnCloseRequest(event -> latch.countDown());
        currentThread = Thread.currentThread();
        stage.show();
    }

    public static void main(String[] args) {
        Platform.startup(() -> {
        });
        //        Platform.setImplicitExit(false);
        InternalBehaviorApp app = new InternalBehaviorApp();
        app.appStart(app);
        try {
            latch.await();
            Platform.exit();
            System.gc();
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(currentThread.getName() + "=========" + currentThread.isAlive());
    }

    public void appStart(InternalBehaviorApp app) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            app.start(stage);
            stage.setOnCloseRequest(e -> latch.countDown());
        });
    }
}
