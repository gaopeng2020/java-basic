package ept.gui.internalbehavior;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class InternalBehaviorApp extends JFrame {
    public  Scene scene;

    public InternalBehaviorApp(Thread callerThread) {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                dispose();
                synchronized (callerThread) {
                    callerThread.notify();
                }
            }
        });

        setTitle("Internal Behavior Definition");
        setIconImage(Toolkit.getDefaultToolkit().getImage(InternalBehaviorApp.class.getResource("icons/appnet.png")));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1500, 800);
        final JFXPanel fxPanel = new JFXPanel();
        setContentPane(fxPanel);

        Platform.runLater(() -> {
                FXMLLoader fxmlLoader = new FXMLLoader(InternalBehaviorApplication.class.getResource("internal-behavior.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 1500, 800);
                fxPanel.setScene(scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        this.scene = fxPanel.getScene();
        setVisible(true);
    }

    public void run() {
        // This method is invoked on Swing thread
        JFrame frame = new JFrame("Internal Behavior Definition");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(InternalBehaviorApp.class.getResource("icons/appnet.png")));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setSize(1500, 800);
        final JFXPanel fxPanel = new JFXPanel();
        frame.add(fxPanel);
        frame.setVisible(true);

        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(InternalBehaviorApplication.class
                        .getResource("internal-behavior.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 1500, 800);
                fxPanel.setScene(scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
