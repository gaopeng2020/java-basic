package demos.javaFxDemo2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

public class HelloApplication2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication2.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 562.5);
        stage.setScene(scene);

        new JMetro(scene, Style.DARK);

        InputStream inputStream = Objects.requireNonNull(getClass().getResourceAsStream("/demos/javaFxDemo2/icons8_blur_16.png"));
        Image image = new Image(inputStream);
        stage.getIcons().add(image);
        stage.setTitle("毛玻璃效果演示");

        displaySystemTrayIcon(stage);

        stage.show();

    }

    private void displaySystemTrayIcon(Stage stage) throws MalformedURLException {
        if (!SystemTray.isSupported()) {
            System.err.println("SystemTray is not supported");
            return;
        }

        URL url = getClass().getResource("/demos/javaFxDemo2/icons8_blur.ico");
        java.awt.Image image = Toolkit.getDefaultToolkit().getImage(url);
        final TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setImageAutoSize(true);

        // Create a pop-up menu components
        final PopupMenu popup = new PopupMenu();
        //Add components to pop-up menu
        trayIcon.setPopupMenu(popup);
        trayIcon.setToolTip("SOME/IP矩阵一致性检查工具");

        MenuItem aboutItem = new MenuItem("关于");
        popup.add(aboutItem);
        popup.addSeparator();
        MenuItem openItem = new MenuItem("打开");
        popup.add(openItem);
        popup.addSeparator();
        MenuItem exitItem = new MenuItem("退出");
        popup.add(exitItem);

        //监听事件
        systemTrayActionListener(stage, trayIcon, aboutItem, openItem, exitItem);

        final SystemTray systemTray = SystemTray.getSystemTray();
        try {
            systemTray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    private void systemTrayActionListener(Stage stage, TrayIcon trayIcon, MenuItem aboutItem, MenuItem openItem, MenuItem exitItem) {
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (e.getButton() == 1) {
                    Platform.runLater(() -> {
                        if (!stage.isShowing()) {
                            stage.show();
                        }
                        stage.toFront();
                    });
                }
            }
        });

        aboutItem.addActionListener(e -> Platform.runLater(() -> {
            javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("关于SOME/IP矩阵一致性检查工具");
            String msg = """
                                        
                    SOME/IP矩阵一致性检查工具 2023.1.2 (v1.0.0)
                    适配怿星SOME/IP服务矩阵v2.1.0版本
                                        
                                        
                    Java Runtime version: 17.0.6+10-b829.9 amd64
                    Copyright © 2014–2023 上海怿星电子科技有限公司
                    """;
            dialog.setHeaderText(msg);
            Image image = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/demos/javaFxDemo2/EPT_Logo.png")));
            ImageView imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitHeight(69.8);
//            imageView.setFitWidth(329.8);
            dialog.setGraphic(imageView);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            dialog.getDialogPane().lookupButton(ButtonType.CLOSE);

            dialog.show();
        }));

        openItem.addActionListener(e -> Platform.runLater(() -> {
            if (!stage.isShowing()) {
                stage.show();
            }
            stage.toFront();
        }));
        exitItem.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        launch();
    }
}