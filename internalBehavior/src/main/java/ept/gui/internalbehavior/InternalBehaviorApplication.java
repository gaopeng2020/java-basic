package ept.gui.internalbehavior;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.concurrent.CountDownLatch;

public class InternalBehaviorApplication extends Application {
    public static Thread currentThread;
    CountDownLatch latch = new CountDownLatch(1);

    /**
     * @param stage stage
     * @throws Exception fxmlLoader.load Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(InternalBehaviorApplication.class.getResource("internal-behavior.fxml"));
        System.out.println("fxmlLoader.getLocation() = " + fxmlLoader.getLocation());
        Scene scene = new Scene(fxmlLoader.load(), 1500, 800);
        stage.setTitle("Internal Behavior Definition");
        stage.getIcons().add(new Image("file:/icons/appnet.png", 48, 48, true, true));
//        stage.setIconified(true);
        stage.setScene(scene);
        stage.setMinWidth(600.0);
        stage.setMinHeight(400.0);
        stage.initStyle(StageStyle.UNIFIED);
//        stage.initModality(Modality.NONE);
        stage.setOpacity(0.98);
        stage.show();
        currentThread = Thread.currentThread();
        stage.setOnCloseRequest(event -> latch.countDown());
    }

    /**
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch();
        System.out.println(currentThread.getName()+"========="+currentThread.isAlive());
    }
}
