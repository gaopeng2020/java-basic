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

    public static void start(Stage stage) {
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

    public static void main(String[] args) throws Exception {
//        Platform.startup(()->{});
////        Platform.setImplicitExit(false);
//        appStart();
//        try {
//            latch.await();
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//        System.out.println(currentThread.getName() + "=========" + currentThread.isAlive());
        InternalBehaviorApplication.main(args);

    }

    private static void appStart() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            InternalBehaviorApp app = new InternalBehaviorApp();
            start(stage);
            stage.setOnCloseRequest(e -> latch.countDown());
        });
    }
}
