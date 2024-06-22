package projects.internalbehavior;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;

import javax.swing.*;
import java.util.concurrent.CountDownLatch;

public class InternalBehaviorApplication extends Application {
    @Getter
    private Thread currentThread;
    @Getter
    private final CountDownLatch latch = new CountDownLatch(1);

    /**
     * @param stage stage
     * @throws Exception fxmlLoader.load Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(InternalBehaviorApplication.class.getResource("internal-behavior.fxml"));
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
        stage.setOnCloseRequest(event -> {
            latch.countDown();
            stage.close();
        });
    }

    public void launchAppBySwing() throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            new JFXPanel();
            Platform.runLater(() -> {
                try {
                    new InternalBehaviorApplication().start(new Stage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        });

        try {
            latch.await();
            JFXPanel.getDefaultLocale();
            Platform.exit();
            System.gc();
            Thread.sleep(10);
        } catch (InterruptedException e) {
            currentThread.interrupt();
            throw new InterruptedException();
        }
    }

}
