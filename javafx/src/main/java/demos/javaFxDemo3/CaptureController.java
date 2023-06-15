package demos.javaFxDemo3;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.RibbonTab;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

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
    private RibbonTab homeRibbonTab;

    @FXML
    private Ribbon ribbon;

    @FXML
    private ChoiceBox<String> scaleChoiceBox;

    @FXML
    private Slider scaleSlider;

    @FXML
    private Button zoomInBtn;

    @FXML
    private Button zoomOutBtn;


    private Stage primaryStage;
    private Stage captureStage;
    private int counter;

    Tab selectedTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        ribbon.setVisible(false);
//        ribbon.setPrefHeight(0);
        counter = centerTabPane.getTabs().size();

        centerTabPane.selectionModelProperty().addListener((observable, oldValue, newValue) -> selectedTab = newValue.getSelectedItem());


        zoomInBtn.setOnMouseClicked(event -> {
            int value = (int) scaleSlider.getValue();
            double newValue = value + 1.0;
            scaleSlider.adjustValue(newValue);
        });
        zoomOutBtn.setOnMouseClicked(event -> {
            int value = (int) scaleSlider.getValue();
            double newValue = value - 1.0;
            scaleSlider.adjustValue(newValue);

        });
        scaleChoiceBox.getItems().addAll("25%", "50%", "75%", "100%", "200%", "300%", "400%", "500%", "600%", "700%", "800%");
        scaleChoiceBox.getSelectionModel().select("100%");
        scaleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case "25%" -> scaleSlider.adjustValue(0);
                case "50%" -> scaleSlider.adjustValue(1);
                case "75%" -> scaleSlider.adjustValue(2);
                case "100%" -> scaleSlider.adjustValue(3);
                case "200%" -> scaleSlider.adjustValue(4);
                case "300%" -> scaleSlider.adjustValue(5);
                case "400%" -> scaleSlider.adjustValue(6);
                case "500%" -> scaleSlider.adjustValue(7);
                case "600%" -> scaleSlider.adjustValue(8);
                case "700%" -> scaleSlider.adjustValue(9);
                case "800%" -> scaleSlider.adjustValue(10);
            }
        });
        scaleSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int value = newValue.intValue();
            double scale = 1.0;
            selectedTab = centerTabPane.getSelectionModel().getSelectedItem();
            AnchorPane anchorPane = (AnchorPane) selectedTab.getContent();
            ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(0);
            ImageView imageView = (ImageView) scrollPane.getContent();

            switch (value) {
                case 0 -> {
                    scaleChoiceBox.getSelectionModel().select("25%");
                    scale = 0.25;
                }
                case 1 -> {
                    scaleChoiceBox.getSelectionModel().select("50%");
                    scale = 0.5;
                }
                case 2 -> {
                    scaleChoiceBox.getSelectionModel().select("75%");
                    scale = 0.75;
                }
                case 3 -> scaleChoiceBox.getSelectionModel().select("100%");
                case 4 -> {
                    scaleChoiceBox.getSelectionModel().select("200%");
                    scale = 2.0;
                }
                case 5 -> {
                    scaleChoiceBox.getSelectionModel().select("300%");
                    scale = 3.0;
                }
                case 6 -> {
                    scaleChoiceBox.getSelectionModel().select("400%");
                    scale = 4.0;
                }
                case 7 -> {
                    scaleChoiceBox.getSelectionModel().select("500%");
                    scale = 5.0;
                }
                case 8 -> {
                    scaleChoiceBox.getSelectionModel().select("600%");
                    scale = 6.0;
                }
                case 9 -> {
                    scaleChoiceBox.getSelectionModel().select("700%");
                    scale = 7.0;
                }
                case 10 -> {
                    scaleChoiceBox.getSelectionModel().select("800%");
                    scale = 8.0;
                }
            }
            double fitHeight = imageView.getFitHeight();
            System.out.println("fitHeight = " + fitHeight);
            double fitWidth = imageView.getFitWidth();
            System.out.println("fitWidth = " + fitWidth);

            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
//            imageView.setX(0);
//            imageView.setY(0);
            imageView.setLayoutX(0.0);
            imageView.setLayoutY(0.0);
        });

    }

    @FXML
    void onOpenButtonReleased(MouseEvent event) {

    }

    @FXML
    void onSaveButtonReleased(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                    new FileChooser.ExtensionFilter("JPEG", "*.jpg"),
                    new FileChooser.ExtensionFilter("GIF", "*.gif"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            for (Tab tab : centerTabPane.getTabs()) {
                String text = tab.getText();
                fileChooser.setInitialFileName(text);
                if (tab.isSelected()) {
                    File file = fileChooser.showSaveDialog(new Stage());
                    AnchorPane anchorPane = (AnchorPane) tab.getContent();
                    ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(0);
                    Image image = ((ImageView) scrollPane.getContent()).getImage();
                    copy2Clipboard(image);
                    try {
                        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                        BufferedImage awtImage = null;
                        if (suffix.equals("jpg")) {
                            awtImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);
                        }
                        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, awtImage);
                        ImageIO.write(bufferedImage, suffix, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void copy2Clipboard(Image image) {
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        Clipboard.getSystemClipboard().setContent(content);
    }

    @FXML
    void onPaletteButtonReleased(MouseEvent event) {

    }

    @FXML
    void onCaptureBtnClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            //隐藏primaryStage
            primaryStage.setIconified(true);

            //创建一个截图专用的stage
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.setStyle("-fx-background-color:#ffffff01");
            Scene scene = new Scene(anchorPane);
            scene.setFill(Paint.valueOf("ffffff00"));
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

            //右键监听

        }
    }

    private void captureStageListener(AnchorPane anchorPane) {
        ObservableList<Screen> screens = Screen.getScreens();

        Rectangle2D bounds = Screen.getPrimary().getBounds();
        //获取屏幕宽高
        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();

        AtomicReference<Double> startX = new AtomicReference<>((double) 0);
        AtomicReference<Double> startY = new AtomicReference<>((double) 0);
        AtomicReference<Integer> snapWidth = new AtomicReference<>((int) 0);
        AtomicReference<Integer> snapHeight = new AtomicReference<>((int) 0);


        //监听鼠标移动事件，在屏幕上显示截图参考坐标系
        anchorPane.setOnMouseMoved(mouseEvent -> {
            anchorPane.getChildren().clear();
            //获取鼠标当前位置
            double sceneX = mouseEvent.getSceneX();
            double sceneY = mouseEvent.getSceneY();
            startX.set(sceneX);
            startY.set(sceneY);

            Line xLine = new Line(sceneX, sceneY, screenWidth, sceneY);
            Line yLine = new Line(sceneX, sceneY, sceneX, screenHeight);

            anchorPane.getChildren().addAll(xLine, yLine);
        });

        //监听鼠标拖拽
        anchorPane.setOnDragDetected(mouseEvent -> anchorPane.startFullDrag());
        anchorPane.setOnMouseDragOver(mouseEvent -> {
            Double startRecX = startX.get();
            Double startRecY = startY.get();

            anchorPane.getChildren().clear();
            //获取鼠标当前位置
            double width = mouseEvent.getSceneX() - startRecX;
            snapWidth.set((int) width);
            double height = mouseEvent.getSceneY() - startRecY;
            snapHeight.set((int) height);

            Rectangle rectangle = new Rectangle(width, height, Paint.valueOf("#FFFFFF01"));
            rectangle.setStroke(Color.RED);
            rectangle.setStrokeWidth(1);
            rectangle.getStrokeDashArray().addAll(5.0, 10.0);
            AnchorPane.setLeftAnchor(rectangle, startRecX);
            AnchorPane.setTopAnchor(rectangle, startRecY);

            anchorPane.getChildren().addAll(rectangle);
        });

        //拖拽结束后执行截图，在TabPane上创建一个tab，并将截图放在tab上
        anchorPane.setOnMouseDragExited(mouseEvent -> {
            captureStage.close();
            anchorPane.getChildren().clear();

            double x = startX.get();
            double y = startY.get();
            java.awt.Rectangle rect = new java.awt.Rectangle((int) x, (int) y, snapWidth.get(), snapHeight.get());
            BufferedImage screenCapture;
            try {
                screenCapture = new Robot().createScreenCapture(rect);
            } catch (AWTException e) {
                throw new RuntimeException(e);
            }
            WritableImage fxImage = SwingFXUtils.toFXImage(screenCapture, null);
            ImageView imageView = new ImageView(fxImage);

            AnchorPane tabPane = new AnchorPane();
            ScrollPane scrollPane = new ScrollPane(imageView);
            tabPane.getChildren().add(scrollPane);
            AnchorPane.setLeftAnchor(scrollPane, 0.0);
            AnchorPane.setTopAnchor(scrollPane, 0.0);
            AnchorPane.setBottomAnchor(scrollPane, 0.0);
            AnchorPane.setRightAnchor(scrollPane, 0.0);


            Tab tab = new Tab("image" + counter, tabPane);
            centerTabPane.getTabs().add(tab);
            counter++;
            centerTabPane.getSelectionModel().select(tab);
            primaryStage.setIconified(false);

            //监听缩放
            scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
                if (event.isControlDown()) {
                    System.out.println("-------------------------");
                }
                System.out.println("event.isDirect() = " + event.isDirect());
                System.out.println("event.isInertia() = " + event.isInertia());
            });
        });

    }

    @FXML
    void onQuickAccessCaptureBtnClicked(MouseEvent event) {
        onCaptureBtnClicked(event);
    }

    public void addShotCutKeys() {
        this.primaryStage = (Stage) captureBtn.getScene().getWindow();
        //为截图按钮设置快捷键
        Mnemonic captureBtnMnemonic = new Mnemonic(captureBtn, KeyCombination.valueOf("ctrl+alt+p"));
        captureBtn.getScene().addMnemonic(captureBtnMnemonic);
    }

}
