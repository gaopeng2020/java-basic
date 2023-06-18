package demos.javaFxDemo3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class NewImageController implements Initializable {

    @FXML
    private Spinner<Integer> heightSpinner;

    @FXML
    private Spinner<Integer> widthSpinner;

    @Setter
    private CaptureController captureController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void onCancelBtnClicked(MouseEvent event) {
        Button source = (Button) event.getSource();
        Stage window = (Stage) source.getScene().getWindow();
        window.close();
    }

    @FXML
    void onOkBtnClicked(MouseEvent event) {
        TabPane tabPane = captureController.getCenterTabPane();
        String backColor ="#"+ captureController.getBackColor().toString().substring(2);
        AnchorPane pane = captureController.createTabForImageView(tabPane, null);
        pane.setPrefHeight(heightSpinner.getValue());
        pane.setPrefWidth(widthSpinner.getValue());
        pane.setStyle("-fx-background-color:"+backColor);
        onCancelBtnClicked(event);
    }


}
