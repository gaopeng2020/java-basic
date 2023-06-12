package demos.javaFxDemo4;


import javafx.application.Application;
        import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
        import javafx.stage.Stage;
        import org.kordamp.bootstrapfx.BootstrapFX;
        import org.kordamp.bootstrapfx.scene.layout.Panel;

import java.util.Map;
import java.util.Objects;

public class CssSampleApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {                   //(1)
//        Panel panel = new Panel("This is the title");
//        panel.getStyleClass().add("panel-primary");                            //(2)
//        BorderPane content = new BorderPane();
//        content.setPadding(new Insets(20));
//        Button button = new Button("Hello BootstrapFX");
//        button.getStyleClass().setAll("btn","btn-danger");                     //(2)
//        content.setCenter(button);
//        panel.setBody(content);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("sample2.css")).toExternalForm());
        ToolBar toolBar = new ToolBar(
                new Button("New"),
                new Button("Open"),
                new Button("Save"),
                new Separator(Orientation.HORIZONTAL),
                new Button("Clean"),
                new Button("Compile"),
                new Button("Run"),
                new Separator(Orientation.HORIZONTAL),
                new Button("Debug"),
                new Button("Profile")
        );
        toolBar.setOrientation(Orientation.VERTICAL);
        anchorPane.getChildren().addAll(toolBar);

        Scene scene = new Scene(anchorPane);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setTitle("BootstrapFX");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}