package ept.gui.others;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class HelloController {
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane redAnchorPane;
    @FXML
    private AnchorPane purpleAnchorPane;
    @FXML
    private AnchorPane blueAnchorPane;

    @FXML
    private Button blueBtn;
    @FXML
    private Button purpleBtn;
    @FXML
    private Button redBtn;


    @FXML
    void onBlueBtnAction(ActionEvent event) {
        ObservableList<Node> children = stackPane.getChildren();
        if (!children.contains(blueAnchorPane)) {
            children.add(blueAnchorPane);
        }
        children.removeAll(redAnchorPane, purpleAnchorPane);
    }

    @FXML
    void onPurpleBtnAction(ActionEvent event) {
        ObservableList<Node> children = stackPane.getChildren();
        if (!children.contains(purpleAnchorPane)) {
            children.add(purpleAnchorPane);
        }
        stackPane.getChildren().removeAll(redAnchorPane, blueAnchorPane);
    }

    @FXML
    void onrRdBtnAction(ActionEvent event) {
        ObservableList<Node> children = stackPane.getChildren();
        if (!children.contains(redAnchorPane)) {
            children.add(redAnchorPane);
        }
        stackPane.getChildren().removeAll(blueAnchorPane, purpleAnchorPane);
    }

}