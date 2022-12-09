package ept.gui.javaFxDemo1;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    public FXMLLoader fxmlLoader;


    @Override
    public void start(Stage stage) throws IOException {
        fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.DECORATED);
//        stage.initModality(Modality.WINDOW_MODAL);

        stage.setOpacity(0.98);
//        stage.setAlwaysOnTop(true);
//        stage.xProperty().addListener((observableValue, number, t1) -> System.out.println("X Index =  = " + t1));
//        stage.yProperty().addListener((observableValue, number, t1) -> System.out.println("Y Index = " + t1));

        HostServices hostServices = getHostServices();

        HelloController controller = fxmlLoader.getController();
        controller.getComboBox1().getSelectionModel().selectedItemProperty().addListener((observableValue, s, t1) -> System.out.println("\"ComboBox1 selected Item is\" = " + t1));
        String selectedValue = controller.getComboBox1().getSelectionModel().selectedItemProperty().getValue();
        System.out.println("selectedValue = " + selectedValue);
        stage.show();
    }
}