package demos.javaFxDemo3;

import com.pixelduke.control.Ribbon;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.*;
import lombok.Getter;
import lombok.NonNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author gaopeng
 */
public class CaptureController implements Initializable {
    @FXML
    private ColorPicker foregroundColorPicker;
    @FXML
    private ColorPicker backgroundColorPicker;
    @FXML
    @Getter
    private Button captureBtn;
    @FXML
    private Button hideToolbar;
    @FXML
    private Button saveImageBtn;
    @FXML
    private Button saveAllImagesBtn;
    @FXML
    private Button toolShapesBtn;
    @FXML
    @Getter
    private TabPane centerTabPane;
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

    /***********************************************************************
     **********************customized private fields***********************
     ***********************************************************************/
    private Stage primaryStage;
    private Stage captureStage;
    private int counter;
    private Tab selectedTab;
    private Double strokeWidth;
    private Color strokeColor;
    private ObservableList<Double> strokeDashArray;
    @Getter
    private Color backColor;
    private static final java.util.List<Popup> POPUP_LIST = new ArrayList<>();
    private static final AtomicReference<Double> CURRENT_X = new AtomicReference<>((double) 0);
    private static final AtomicReference<Double> CURRENT_Y = new AtomicReference<>((double) 0);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        counter = centerTabPane.getTabs().size();
        //stroke initialize
        strokeDashArray = FXCollections.observableArrayList();
        strokeDashArray.addAll(15.0, 0.0);
        strokeWidth = 1.0;
        strokeColor = Color.RED;
        foregroundColorPicker.setValue(strokeColor);
        foregroundColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> strokeColor = newValue);
        backColor = backgroundColorPicker.getValue();
        backgroundColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> backColor = newValue);

        //disable some buttons
        saveImageBtn.setDisable(true);
        saveAllImagesBtn.setDisable(true);
        toolShapesBtn.setDisable(true);

        //add listener for zoomIn/Out Button、scaleChoiceBox、scaleSlider
        centerTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                saveImageBtn.setDisable(false);
                saveAllImagesBtn.setDisable(false);
                toolShapesBtn.setDisable(false);

                selectedTab = newValue;
                ScrollPane scrollPane = (ScrollPane) newValue.getContent();
                AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
                ImageView imageView = (ImageView) anchorPane.getChildren().stream().filter(node -> node instanceof ImageView).findFirst().orElse(null);

                zoomInZoomOutBinding(imageView);

                //add listener for ctrl+Middle key of mouse
                assert imageView != null;
                scrollMouseKeyBinding(imageView);
            }
        });
    }

    @FXML
    void onHideToolbarClicked(MouseEvent mouseEvent) {
        int height = (int) ribbon.getPrefHeight();
        if (height != 0) {
            ribbon.setPrefHeight(0.0);
        } else {
            ribbon.setPrefHeight(185.0);
        }
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
    void onSaveButtonReleased() {
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
        if (file == null) {
            return;
        }

        Image snapshot = getImageOfSelectedTab();
        String suffix = file.getName().substring(file.getName().lastIndexOf(".") + 1);
        BufferedImage awtImage = null;
        if (suffix.equals("jpg")) {
            awtImage = new BufferedImage((int) snapshot.getWidth(), (int) snapshot.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, awtImage);
        try {
            ImageIO.write(bufferedImage, suffix, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onNewFileBtnAction(MouseEvent event) {
        Stage stage = new Stage(StageStyle.DECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(CaptureApplication.class.getResource("new-image-dialog-view.fxml"));
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load(), 350, 200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
        stage.show();

        NewImageController controller = fxmlLoader.getController();
        controller.setCaptureController(this);
    }

    @FXML
    void onCaptureBtnAction() {
        //隐藏primaryStage
        primaryStage.setIconified(true);

        //创建一个截图专用的stage
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color:#ffffff55");
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
        captureWindowListener(anchorPane);
    }

    @FXML
    void onQuickAccessCaptureBtnClicked(MouseEvent event) {
        onCaptureBtnAction();
    }

    @FXML
    void onToolShapesBtnClick(MouseEvent event) {
        Popup popup = createPopupWithVBox(event, "ShapeToolPopup", 120.0, 80.0);
        VBox vBox = (VBox) popup.getContent().get(0);
        if (vBox.getChildren().isEmpty()) {
            vBox.setSpacing(5.0);

            //lines
            Label lineLabel = createLabel("Lines");
            lineLabel.prefWidthProperty().bind(vBox.widthProperty());
            Button lineBtn = createButton("", "icons8_line_16.png");
            lineBtn.setOnMouseClicked(mouseEvent -> drawLine());

            Button curveBtn = createButton("", "icons8_wavy_line_16.png");
            HBox lineHBox = new HBox(10);
            lineHBox.getChildren().addAll(lineBtn, curveBtn);

            //rectangle
            Label lineShapes = createLabel("Shapes");
            lineShapes.prefWidthProperty().bind(vBox.widthProperty());
            Button recBtn = createButton("", "icons8_rectangular_16.png");
            recBtn.setOnMouseClicked(mouseEvent -> drawRectangle(null));

            Button roundRecBtn = createButton("", "icons8_rounded_rectangle_stroked_16.png");
            roundRecBtn.setOnMouseClicked(mouseEvent -> drawRectangle(20.0));

            Button circleBtn = createButton("", "icons8_circle_16.png");
            circleBtn.setOnMouseClicked(mouseEvent -> drawCircle());

            Button ellipseBtn = createButton("", "icons8_ellipse_16.png");
            ellipseBtn.setOnMouseClicked(mouseEvent -> drawEllipse());
            HBox shapeHBox = new HBox(10);
            shapeHBox.getChildren().addAll(recBtn, roundRecBtn, circleBtn, ellipseBtn);

            vBox.getChildren().addAll(lineLabel, lineHBox, lineShapes, shapeHBox);
        }

        popup.show(primaryStage);

        //hide popup
        hidePopupWindow(popup, event);
    }

    @FXML
    void onLineTypeBtnClick(MouseEvent event) {
        Popup popup = createPopupWithVBox(event, "StrokeTypePopup", 120.0, 80.0);
        VBox vBox = (VBox) popup.getContent().get(0);
        if (vBox.getChildren().isEmpty()) {
            vBox.setSpacing(5.0);
            Button solidLine = createButton("Solid Line", null);
            solidLine.setOnMouseClicked(mouseEvent -> {
                strokeDashArray.clear();
                strokeDashArray.addAll(15.0, 0.0);
            });

            Button dotLine = createButton("Dot Line", null);
            dotLine.setOnMouseClicked(mouseEvent -> {
                strokeDashArray.clear();
                strokeDashArray.addAll(1.0, 10.0);
            });

            Button dashLine = createButton("Dash Line", null);
            dashLine.setOnMouseClicked(mouseEvent -> {
                strokeDashArray.clear();
                strokeDashArray.addAll(15.0, 10.0);
            });

            Button dashDotLine = createButton("Dash Dot Line", null);
            dashDotLine.setOnMouseClicked(mouseEvent -> {
                strokeDashArray.clear();
                strokeDashArray.addAll(15.0, 10.0, 1.0, 10.0);
            });
            vBox.getChildren().addAll(solidLine, dotLine, dashLine, dashDotLine);
        }
        popup.show(primaryStage);

        //hide popup
        hidePopupWindow(popup, event);
    }

    @FXML
    void onLineWidthBtnClick(MouseEvent event) {
        Popup popup = createPopupWithVBox(event, "StrokeWidthPopup", 120.0, 80.0);
        VBox vBox = (VBox) popup.getContent().get(0);
        if (vBox.getChildren().isEmpty()) {
            Label label = createLabel("Line Width");
            label.setStyle("");
            ObservableList<String> list = FXCollections.observableArrayList();
            list.addAll("1 px", "2 px", "3 px", "4 px", "5 px", "6 px", "7 px", "8 px", "9 px", "10 px");
            Spinner<String> spinner = new Spinner<>(list);
            spinner.setPrefWidth(80);

            vBox.getChildren().addAll(new Label(), label, spinner);
            spinner.valueProperty().addListener((observable, oldValue, newValue) ->
                    this.strokeWidth = Double.parseDouble(newValue.substring(0, newValue.indexOf("px")).trim()));
        }

        popup.show(primaryStage);

        //hide popup
        hidePopupWindow(popup, event);
    }


    /***********************************************************************
     **********************public methods**********************************
     ***********************************************************************/
    public AnchorPane createTabForImageView(TabPane tabPane, Image image) {
        AnchorPane anchorPane = new AnchorPane();
        ImageView imageView = new ImageView();
        if (image != null) {
            imageView.setImage(image);
            imageView.setFitHeight(image.getHeight());
            imageView.setFitWidth(image.getWidth());
        }
        anchorPane.getChildren().add(imageView);
        AnchorPane.setLeftAnchor(imageView, 0.0);
        AnchorPane.setTopAnchor(imageView, 0.0);
        AnchorPane.setBottomAnchor(imageView, 0.0);
        AnchorPane.setRightAnchor(imageView, 0.0);
        ScrollPane scrollPane = new ScrollPane(anchorPane);

        Tab tab = new Tab("image" + counter, scrollPane);
        tabPane.getTabs().add(tab);
        tabPane.getSelectionModel().select(tab);
        counter++;

        ContextMenu contextMenu = new ContextMenu();
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setGraphic(createImageView("icons8_copy_16.png"));
        copyItem.setOnAction(event -> copyImage2Clipboard(getImageOfSelectedTab()));

        MenuItem saveItem = new MenuItem("Save");
        saveItem.setGraphic(createImageView("icons8_Save_16px.png"));
        saveItem.setOnAction(event -> onSaveButtonReleased());

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setGraphic(createImageView("icons8_Delete_Trash_16.png"));
        deleteItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure to delete this image?", ButtonType.YES, ButtonType.NO);
            alert.resultProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals(ButtonType.YES)) {
                    tabPane.getTabs().remove(tab);
                }
            });
            alert.show();
        });

        contextMenu.getItems().addAll(copyItem, saveItem, deleteItem);
        tab.setContextMenu(contextMenu);

        return anchorPane;
    }

    public ImageView createImageView(String imageName) {
        if (imageName != null && !imageName.isEmpty()) {
            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(imageName));
            Image image = new Image(is);
            Rectangle clip = new Rectangle(image.getWidth(), image.getHeight());
            clip.setArcWidth(5);
            clip.setArcHeight(5);
            ImageView imageView = new ImageView(image);
            imageView.setClip(clip);
            return imageView;
        }
        return null;
    }

    public Image getImageOfSelectedTab() {
        AnchorPane anchorPane = (AnchorPane) ((ScrollPane) selectedTab.getContent()).getContent();
        WritableImage writableImage = new WritableImage((int) anchorPane.getWidth(), (int) anchorPane.getHeight());
        return anchorPane.snapshot(new SnapshotParameters(), writableImage);
    }

    public Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color:#D9D9D9");
        label.setPrefHeight(35.0);
        label.minHeight(label.getPrefHeight());
        return label;
    }

    public Button createButton(String text, String imageName) {
        Button button = new Button(text);
        ImageView imageView = createImageView(imageName);
        if (imageView != null) {
            button.setGraphic(imageView);
        }

        button.setStyle("-fx-border-color:#ffffffff");
        button.styleProperty().bind(
                Bindings.when(button.hoverProperty())
                        .then(
                                new SimpleStringProperty("-fx-background-color: lightskyblue")
                        )
                        .otherwise(
                                new SimpleStringProperty("-fx-background-color:#ffffffff")
                        )
        );
        return button;
    }

    public Popup createPopupWithVBox(MouseEvent event, String popupName, double vBoxPrefWidth, double vBoxPrefHeight) {
        Button button = (Button) event.getSource();
        Popup elseGet = POPUP_LIST.stream().filter(popup -> popup.getUserData().equals(popupName)).findFirst().orElseGet(() -> {
            Popup popup = new Popup();
            popup.setUserData(popupName);

            VBox vBox = new VBox(10);
            vBox.setStyle("-fx-background-color:#ffffffff");
            vBox.setPrefWidth(vBoxPrefWidth);
            vBox.setPrefHeight(vBoxPrefHeight);
            popup.getContent().addAll(vBox);

            POPUP_LIST.add(popup);
            return popup;
        });
        elseGet.setX(event.getScreenX() - button.getWidth() / 2);
        elseGet.setY(event.getScreenY() + button.getHeight() / 2);
        return elseGet;
    }

    public void hidePopupWindow(Popup popup, MouseEvent event) {
        Scene scene = ((Button) event.getSource()).getScene();
        scene.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (popup.isShowing()) {
                popup.hide();
            }
        });
    }

    public void addShotCutKeys(Button button) {
        this.primaryStage = (Stage) button.getScene().getWindow();
        //为截图按钮设置快捷键
        Mnemonic mnemonic = new Mnemonic(captureBtn, KeyCombination.valueOf("ctrl+alt+p"));
        button.getScene().addMnemonic(mnemonic);
    }

    public void copyImage2Clipboard(Image image) {
        ClipboardContent content = new ClipboardContent();
        content.putImage(image);
        Clipboard.getSystemClipboard().setContent(content);
    }

    public void drawLine() {
        AnchorPane anchorPane = getCoordinatesOfTab();
        anchorPane.setOnDragDetected(mouseEvent -> anchorPane.startFullDrag());
        anchorPane.setOnMouseDragOver(mouseEvent -> {
            List<Node> list = anchorPane.getChildren().stream()
                    .filter(node -> node instanceof Line && !node.isManaged())
                    .findAny().stream().toList();
            anchorPane.getChildren().removeAll(list);

            double startX = CURRENT_X.get();
            double startY = CURRENT_Y.get();
            //获取鼠标当前位置
            double endX = mouseEvent.getSceneX();
            double endY = mouseEvent.getSceneY() - 210.0;

            Line xLine = new Line(startX, startY, endX, endY);
            xLine.setManaged(false);
            xLine.setStroke(strokeColor);
            xLine.setStrokeWidth(strokeWidth);
            xLine.getStrokeDashArray().addAll(strokeDashArray);
            anchorPane.getChildren().addAll(xLine);
        });
        anchorPane.setOnMouseDragExited(mouseEvent -> anchorPane.getChildren().stream().filter(node -> node instanceof Line)
                .forEach(line -> line.setManaged(true)));
    }

    public void drawRectangle(Double arcRadius) {
        AnchorPane anchorPane = getCoordinatesOfTab();
        anchorPane.setOnDragDetected(mouseEvent -> anchorPane.startFullDrag());
        anchorPane.setOnMouseDragOver(mouseEvent -> {
            List<Node> list = anchorPane.getChildren().stream()
                    .filter(node -> node instanceof Rectangle && !node.isManaged())
                    .findAny().stream().toList();
            anchorPane.getChildren().removeAll(list);

            double startX = CURRENT_X.get();
            double startY = CURRENT_Y.get();
            //获取鼠标当前位置
            double width = mouseEvent.getSceneX() - startX;
            double height = mouseEvent.getSceneY() - 210 - startY;

            Rectangle rectangle = new Rectangle(startX, startY, width, height);
            rectangle.setFill(Color.valueOf("#ffffff00"));
            rectangle.setManaged(false);
            rectangle.setStroke(strokeColor);
            rectangle.setStrokeWidth(strokeWidth);
            if (arcRadius != null) {
                rectangle.setArcHeight(10);
                rectangle.setArcWidth(10);
            }
            rectangle.getStrokeDashArray().addAll(strokeDashArray);

            anchorPane.getChildren().addAll(rectangle);
        });
        anchorPane.setOnMouseDragExited(mouseEvent -> anchorPane.getChildren().stream().filter(node -> node instanceof Rectangle)
                .forEach(node -> node.setManaged(true)));
    }

    public void drawCircle() {
        AnchorPane anchorPane = getCoordinatesOfTab();
        anchorPane.setOnDragDetected(mouseEvent -> anchorPane.startFullDrag());
        anchorPane.setOnMouseDragOver(mouseEvent -> {
            List<Node> list = anchorPane.getChildren().stream()
                    .filter(node -> node instanceof Circle && !node.isManaged())
                    .findAny().stream().toList();
            anchorPane.getChildren().removeAll(list);

            double startX = CURRENT_X.get();
            double startY = CURRENT_Y.get();
            //获取鼠标当前位置
            double width = mouseEvent.getSceneX() - startX;
            double height = mouseEvent.getSceneY() - 210.0 - startY;
            double radius = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
            Circle circle = new Circle(startX + width / 2, startY + height / 2, radius, Color.valueOf("#ffffff00"));
            circle.setManaged(false);
            circle.setStroke(strokeColor);
            circle.setStrokeWidth(strokeWidth);
            circle.getStrokeDashArray().addAll(strokeDashArray);
            anchorPane.getChildren().addAll(circle);
        });
        anchorPane.setOnMouseDragExited(mouseEvent -> anchorPane.getChildren().stream().filter(node -> node instanceof Circle)
                .forEach(node -> node.setManaged(true)));
    }

    public void drawEllipse() {
        AnchorPane anchorPane = getCoordinatesOfTab();
        anchorPane.setOnDragDetected(mouseEvent -> anchorPane.startFullDrag());
        anchorPane.setOnMouseDragOver(mouseEvent -> {
            List<Node> list = anchorPane.getChildren().stream()
                    .filter(node -> node instanceof Ellipse && !node.isManaged())
                    .findAny().stream().toList();
            anchorPane.getChildren().removeAll(list);

            double startX = CURRENT_X.get();
            double startY = CURRENT_Y.get();
            //获取鼠标当前位置
            double width = mouseEvent.getSceneX() - startX;
            double height = mouseEvent.getSceneY() - 210.0 - startY;

            Ellipse ellipse = new Ellipse(startX + width / 2, startY + height / 2, width / 2, height / 2);
            ellipse.setManaged(false);
            ellipse.setFill(Color.valueOf("#ffffff00"));
            ellipse.setStroke(strokeColor);
            ellipse.setStrokeWidth(strokeWidth);
            ellipse.getStrokeDashArray().addAll(strokeDashArray);

            anchorPane.getChildren().addAll(ellipse);
        });
        anchorPane.setOnMouseDragExited(mouseEvent -> anchorPane.getChildren().stream().filter(node -> node instanceof Ellipse)
                .forEach(node -> node.setManaged(true)));
    }

    /***********************************************************************
     **********************private methods**********************************
     ***********************************************************************/
    private void scrollMouseKeyBinding(@NonNull ImageView imageView) {
        AnchorPane anchorPane = (AnchorPane) imageView.getParent();
        anchorPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.isControlDown()) {
                double width = imageView.getImage().getWidth();
                double height = imageView.getImage().getHeight();
                double rate;
                if (event.getDeltaY() > 0) {
                    rate = 0.25;
                } else {
                    rate = -0.25;
                }
                double newWidth = imageView.getFitWidth() + width * rate;
                double newHeight = imageView.getFitHeight() + height * rate;
                if (newWidth < width * 0.25 || newWidth > width * 10) {
                    return;
                }
                imageView.setFitWidth(newWidth);
                imageView.setFitHeight(newHeight);
                anchorPane.setPrefWidth(newWidth);
                anchorPane.setPrefHeight(newHeight);

                //bind scaleChoiceBox
                switch (String.format("%.2f", newWidth / width)) {
                    case "0.25" -> scaleChoiceBox.getSelectionModel().select("25%");
                    case "0.50" -> scaleChoiceBox.getSelectionModel().select("50%");
                    case "0.75" -> scaleChoiceBox.getSelectionModel().select("75%");
                    case "1.00" -> scaleChoiceBox.getSelectionModel().select("100%");
                    case "2.00" -> scaleChoiceBox.getSelectionModel().select("200%");
                    case "3.00" -> scaleChoiceBox.getSelectionModel().select("300%");
                    case "4.00" -> scaleChoiceBox.getSelectionModel().select("400%");
                    case "5.00" -> scaleChoiceBox.getSelectionModel().select("500%");
                    case "6.00" -> scaleChoiceBox.getSelectionModel().select("600%");
                    case "7.00" -> scaleChoiceBox.getSelectionModel().select("700%");
                    case "8.00" -> scaleChoiceBox.getSelectionModel().select("800%");
                }
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
            AnchorPane parent = (AnchorPane) imageView.getParent();
            parent.setPrefWidth(newWidth);
            parent.setPrefHeight(newWidth);
        });

    }

    private void captureWindowListener(AnchorPane anchorPane) {
        //获取屏幕宽高
        Rectangle2D bounds = Screen.getPrimary().getBounds();
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
            copyImage2Clipboard(fxImage);
        });

    }

    private AnchorPane getCoordinatesOfTab() {
        ScrollPane scrollPane = (ScrollPane) selectedTab.getContent();
        AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();
        anchorPane.setOnMouseMoved(mouseEvent -> {
            //获取鼠标当前位置
            double sceneX = mouseEvent.getSceneX();
            CaptureController.CURRENT_X.set(sceneX);
            double sceneY = mouseEvent.getSceneY() - 210.0;
            CaptureController.CURRENT_Y.set(sceneY);
        });
        return anchorPane;
    }

}
