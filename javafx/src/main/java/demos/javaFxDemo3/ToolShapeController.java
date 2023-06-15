package demos.javaFxDemo3;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ToolShapeController {
    @Setter
    private Node parentNode;

    @FXML
    void onLineBtnClicked(MouseEvent event) {
        Tab tab = ((TabPane) parentNode).getSelectionModel().getSelectedItem();
        ScrollPane scrollPane = (ScrollPane) tab.getContent();
        AnchorPane anchorPane = (AnchorPane) scrollPane.getContent();

        AtomicReference<Double> startX = new AtomicReference<>((double) 0);
        AtomicReference<Double> startY = new AtomicReference<>((double) 0);
        anchorPane.setOnMouseMoved(mouseEvent -> {
            //获取鼠标当前位置
            double sceneX = mouseEvent.getSceneX();
            startX.set(sceneX);
            double sceneY = mouseEvent.getSceneY() - 210.0;
            startY.set(sceneY);
        });

        anchorPane.setOnDragDetected(mouseEvent -> anchorPane.startFullDrag());
        anchorPane.setOnMouseDragOver(mouseEvent -> {
            List<Node> collect = anchorPane.getChildren().stream()
                    .filter(node -> node instanceof Line && !node.isManaged())
                    .findAny().stream().toList();
            anchorPane.getChildren().removeAll(collect);

            double linStartX = startX.get();
            double lineStartY = startY.get();
            //获取鼠标当前位置
            double linEndX = mouseEvent.getSceneX();
            double lineEndY = mouseEvent.getSceneY() - 210.0;

            Line xLine = new Line(linStartX, lineStartY, linEndX, lineEndY);
            xLine.setStroke(Color.RED);
            xLine.setStrokeWidth(2);
            xLine.setManaged(false);
            anchorPane.getChildren().addAll(xLine);

        });

        anchorPane.setOnMouseDragExited(mouseEvent -> {
            anchorPane.getChildren().stream().filter(node -> node instanceof Line)
                    .forEach(line -> line.setManaged(true));
        });
    }

    @FXML
    void onLineBtnExited(MouseEvent event) {
        Button button = (Button) event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onRectangleBtnClicked(MouseEvent event) {

    }

}
