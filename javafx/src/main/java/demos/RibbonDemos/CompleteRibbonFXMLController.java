package demos.RibbonDemos;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CompleteRibbonFXMLController {

    @FXML
    private Button FIleNew;

    @FXML
    private Button FIleOpen;

    @FXML
    private Button FileSave;

    @FXML
    private ImageView quickAccessSave;

    @FXML
    void onOpenButtonReleased(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.xlsx"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            File file = fileChooser.showOpenDialog(new Stage());
            System.out.println("file.getName() = " + file.getName());
        }
    }

    public void onSaveButtonReleased(MouseEvent event) {
         String fileName ="SaveTestFile";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Resource File");
            fileChooser.setInitialFileName(fileName + "_Log-" + format.format(new Date()));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.log"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            File file = fileChooser.showSaveDialog(new Stage());
            System.out.println("file.getName() = " + file.getName());
        }
    }
}
