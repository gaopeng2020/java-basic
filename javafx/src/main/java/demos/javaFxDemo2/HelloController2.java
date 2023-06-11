/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package demos.javaFxDemo2;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController2 implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView backGroudImageView;

    @FXML
    private ImageView leftSideImageView;

    @FXML
    private VBox leftSideVBox;



    private TranslateTransition transition;
    private static final double offset = 250.0;

    @FXML
    void onShoeHideBtnClicked(MouseEvent event) {
        boolean toShowWindow = Double.doubleToLongBits(transition.getNode().getTranslateX()) == Double.doubleToLongBits(-offset);
        if (toShowWindow) {
            transition.setFromX(-offset);
            transition.setToX(0);
        } else {
            transition.setFromX(0);
            transition.setToX(-offset);
        }
        transition.play();
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backGroudImageView.fitHeightProperty().bind(rootPane.heightProperty());
        backGroudImageView.fitWidthProperty().bind(rootPane.widthProperty());

        leftSideWindowHandler();
    }

    private void leftSideWindowHandler() {
        leftSideVBox.setPrefWidth(offset);
        for (int i = 0; i < 20; i++) {
            leftSideVBox.getChildren().add(new Text("java fx name " + i));
        }
        StackPane parent = (StackPane) leftSideVBox.getParent();
        parent.setEffect(new DropShadow());

        //初始状态先隐藏掉侧边栏
        parent.setTranslateX(-offset);

        //为左侧窗口创建TranslateTransition设置
        transition = new TranslateTransition(Duration.seconds(0.5), parent);

        //对StackPane添加宽度方向变化的监听，并截取背景图(包含图片与按钮)，最后设置给leftSideImageView
        parent.translateXProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int x = (int) offset - Math.abs(newValue.intValue());
                int y = (int) rootPane.getHeight();
                if (x > 0 && y > 0) {
                    WritableImage image = new WritableImage(x, y);
                    WritableImage snapshot = backGroudImageView.getParent().snapshot(new SnapshotParameters(), image);
                    leftSideImageView.setImage(snapshot);
                }
            }
        });

        //当Stage窗口大小发生变化时重新截图
        rootPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!(Double.doubleToLongBits(transition.getNode().getTranslateX()) == Double.doubleToLongBits(-offset))) {
                    return;
                }
                int x = (int) offset;
                WritableImage image = new WritableImage(x, newValue.intValue());
                WritableImage snapshot = backGroudImageView.getParent().snapshot(new SnapshotParameters(), image);
                leftSideImageView.setImage(snapshot);
            }
        });

    }

}
