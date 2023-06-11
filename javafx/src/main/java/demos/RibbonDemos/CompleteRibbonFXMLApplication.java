package demos.RibbonDemos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.Objects;

public class CompleteRibbonFXMLApplication extends Application {
    private static final String RESOURCE = "CompleteRibbonFXML.fxml";
    private static final String CSS_FILE = "ribbon_sample.css";

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader fxmlLoader = new FXMLLoader(CompleteRibbonFXMLApplication.class.getResource(RESOURCE));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

//        ScenicView.show(scene);
        new JMetro(Style.LIGHT).setScene(scene);
        scene.getStylesheets().add(Objects.requireNonNull(CompleteRibbonFXMLApplication.class.getResource(CSS_FILE)).toExternalForm());

        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
