package demos.javaFxDemo3;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.RibbonTab;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class CaptureController implements Initializable {

    @FXML
    public Button colorPaletteBtn;
    @FXML
    public Button captureBtn;
    @FXML
    private TabPane centerTabPane;

    @FXML
    private Button newFileBtn;

    @FXML
    private Button openFileBtn;

    @FXML
    private Button saveFileBtn;

    @FXML
    private Button saveAsBtn;

    @FXML
    private ImageView quickAccessSaveImgView;

    @FXML
    private RibbonTab homeRibbonTab;

    @FXML
    private Ribbon ribbon;

    private Stage primaryStage;
    private Stage captureStage;

    @FXML
    void onOpenButtonReleased(MouseEvent event) {

    }

    @FXML
    void onPaletteButtonReleased(MouseEvent event) {

    }

    @FXML
    void onSaveButtonReleased(MouseEvent event) {

    }

    @FXML
    void onCaptureBtnClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            //隐藏primaryStage
            primaryStage.setIconified(true);

            //创建一个截图专用的stage
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setStyle("-fx-background-color:#00000080");
            Scene scene = new Scene(anchorPane);
            scene.setFill(Paint.valueOf("00000000"));
            captureStage = new Stage();
            captureStage.setScene(scene);
            captureStage.initStyle(StageStyle.TRANSPARENT);
            captureStage.setFullScreen(true);
            captureStage.setFullScreenExitHint("");
            captureStage.show();

            //监听退出按钮
            scene.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode().equals(KeyCode.ESCAPE)) {
                    captureStage.close();
                    primaryStage.setIconified(false);
                }
            });

            //监听截屏窗口
            captureStageListener(anchorPane);

        }
    }

    private void captureStageListener(AnchorPane anchorPane) {
        anchorPane.setOnMouseMoved(mouseEvent -> {
            anchorPane.getChildren().clear();
            double startX = mouseEvent.getSceneX();
            double startY = mouseEvent.getSceneY();

            Line xLine = new Line(startX, startY, startX + 1000, startY);
            Line yLine = new Line(startX, startY, startX, startY+1000);
            anchorPane.getChildren().addAll(xLine,yLine);
        });

    }

    @FXML
    void onQuickAccessCaptureBtnClicked(MouseEvent event) {
        System.out.println("event = " + event);
        onCaptureBtnClicked(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("location.getPath() = " + location.getPath());
//        ribbon.setVisible(false);
//        ribbon.setPrefHeight(0);

    }

    public void addShotCutKeys() {
        this.primaryStage = (Stage) captureBtn.getScene().getWindow();
        //为截图按钮设置快捷键
        Mnemonic captureBtnMnemonic = new Mnemonic(captureBtn, KeyCombination.valueOf("ctrl+alt+p"));
        captureBtn.getScene().addMnemonic(captureBtnMnemonic);
    }

}
