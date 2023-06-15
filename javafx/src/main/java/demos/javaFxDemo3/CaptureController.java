package demos.javaFxDemo3;

import com.pixelduke.control.Ribbon;
import com.pixelduke.control.ribbon.RibbonTab;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.BorderPane;
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

/**
 * @author gaopeng
 */
public class CaptureController implements Initializable {
    @FXML
    private BorderPane rootPan;
    @FXML
    public Button colorPaletteBtn;
    @FXML
    public Button captureBtn;
    @FXML
    public Button hideToolbar;
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
    private Tab selectedTab;
    private Scene shapeScene;
    private Stage shapeStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        counter = centerTabPane.getTabs().size();

        //add listener for zoomIn/Out Button、scaleChoiceBox、scaleSlider
        centerTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedTab = newValue;
            AnchorPane anchorPane = (AnchorPane) newValue.getContent();
            ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(0);
            ImageView imageView = (ImageView) scrollPane.getContent();
            zoomInZoomOutBinding(imageView);

            //add listener for ctrl+Middle key of mouse
            scrollMouseKeyBinding(anchorPane, imageView);
        });

        //load shapes tool fxml
        FXMLLoader fxmlLoader = new FXMLLoader(CaptureApplication.class.getResource("tool-shapes_view.fxml"));
        try {
            shapeScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onHideToolbarClicked(MouseEvent mouseEvent) {
        int height = (int) ribbon.getPrefHeight();
        if (height != 0) {
            ribbon.setPrefHeight(0.0);
        } else {
            ribbon.setPrefHeight(185.0);
        }
    }

    private void scrollMouseKeyBinding(AnchorPane anchorPane, ImageView imageView) {
        anchorPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double width = imageView.getImage().getWidth();
                double height = imageView.getImage().getHeight();
                double rate;
                if (event.getDeltaY() > 0) {
                    rate = 0.1;
                } else {
                    rate = -0.1;
                }
                double newWidth = imageView.getFitWidth() + width * rate;
                double newHeight = imageView.getFitHeight() + height * rate;
                if (newWidth <= width * 0.25 || newWidth >= height * 10) {
                    return;
                }
                imageView.setFitWidth(newWidth);
                imageView.setFitHeight(newHeight);
            }
        });
    }

    private void zoomInZoomOutBinding(ImageView imageView) {
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
            double width = imageView.getImage().getWidth();
            double height = imageView.getImage().getHeight();
            double scale = 1.0;
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
            double newWidth = width * scale;
            double newHeight = height * scale;
            imageView.setFitWidth(newWidth);
            imageView.setFitHeight(newHeight);
        });

    }

    @FXML
    void onOpenButtonReleased(MouseEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "bmp", "*.gif", "*.tif", "wbm"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            BufferedImage bufferedImage = ImageIO.read(file);
            WritableImage fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
            createTabForImageView(centerTabPane, fxImage);
        }
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
                    new FileChooser.ExtensionFilter("TIFF", "*.tif"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            String text = selectedTab.getText();
            fileChooser.setInitialFileName(text);
            File file = fileChooser.showSaveDialog(new Stage());

            ScrollPane scrollPane = (ScrollPane) ((AnchorPane) selectedTab.getContent()).getChildren().get(0);
            ImageView imageView = (ImageView) scrollPane.getContent();
            Image image = imageView.getImage();
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());

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

                    //shape button
                    shapeStage.close();
                }
            });

            //监听截屏窗口
            captureWindowListener(anchorPane);
        }
    }

    private void captureWindowListener(AnchorPane anchorPane) {
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
            xLine.setStroke(Color.RED);
            xLine.setStrokeWidth(1);
            xLine.getStrokeDashArray().addAll(5.0, 10.0);
            Line yLine = new Line(sceneX, sceneY, sceneX, screenHeight);
            yLine.setStroke(Color.RED);
            yLine.setStrokeWidth(1);
            yLine.getStrokeDashArray().addAll(5.0, 10.0);

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
            createTabForImageView(centerTabPane, fxImage);
            primaryStage.setIconified(false);

            //将图片复制到系统的粘贴板
            copy2Clipboard(fxImage);
        });

    }

    private void createTabForImageView(TabPane tabPane, Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(image.getHeight());
        imageView.setFitWidth(image.getWidth());

        AnchorPane anchorPane = new AnchorPane();
        ScrollPane scrollPane = new ScrollPane(imageView);
        anchorPane.getChildren().add(scrollPane);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);

        Tab tab = new Tab("image" + counter, anchorPane);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        counter++;
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

        primaryStageMouseClickEventListener(primaryStage);
    }
    private void primaryStageMouseClickEventListener(Stage primaryStage) {
        Scene primaryScene = primaryStage.getScene();
        primaryScene.addEventFilter(MouseEvent.MOUSE_CLICKED,event -> {
            if (shapeStage!=null && shapeStage.isShowing()) {
                shapeStage.close();
            }
        });
    }
    public void onToolShapesBtnClick(MouseEvent event) {
            Button button = (Button) event.getSource();
        if (shapeStage == null) {
            shapeStage = new Stage();
            shapeStage.setScene(shapeScene);
            shapeStage.setAlwaysOnTop(true);
            shapeStage.initStyle(StageStyle.UNDECORATED);
        }
        shapeStage.setX(event.getScreenX() - button.getWidth() / 2);
        shapeStage.setY(event.getScreenY() + button.getHeight() / 2);
        if (shapeStage.isShowing()) {
            shapeStage.close();
        } else {
            shapeStage.show();
        }
    }
}
