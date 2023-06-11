package demos.javaFxDemo3;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class CaptureController implements Initializable {

    @FXML
    private Button capture;

    @FXML
    private Button colorPalette;

    @FXML
    private Button fileNew;

    @FXML
    private Button fileOpen;

    @FXML
    private Button fileSave;

    @FXML
    private Button fileSaveAs;

    @FXML
    private ImageView quickAccessSave;

    @FXML
    void onOpenButtonReleased(MouseEvent event) {

    }

    @FXML
    void onPaletteButtonReleased(MouseEvent event) {

    }

    @FXML
    void onSaveButtonReleased(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("location.getPath() = " + location.getPath());

    }
}
