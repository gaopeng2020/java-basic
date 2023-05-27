package projects.internalbehavior;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class InternalBehaviorController implements Initializable {

    public ComboBox<String> newTriggerChoiceBox;
    public ComboBox<String> newAccessPointChoiceBox;
    @FXML // fx:id="rootPane"
    private AnchorPane rootPane; // Value injected by FXMLLoader
    @FXML // fx:id="stackPane"
    private StackPane stackPane; // Value injected by FXMLLoader
    @FXML // fx:id="runnableSplitPane"
    private SplitPane runnableSplitPane; // Value injected by FXMLLoader
    @FXML // fx:id="exclusiveAreaSplitPane"
    private SplitPane exclusiveAreaSplitPane; // Value injected by FXMLLoader
    @FXML // fx:id="irvSplitPane"
    private SplitPane irvSplitPane; // Value injected by FXMLLoader

    @FXML // fx:id="exclusiveAreaBtn"
    private Button exclusiveAreaBtn; // Value injected by FXMLLoader

    @FXML // fx:id="exclusiveAreaListView"
    private ListView<?> exclusiveAreaListView; // Value injected by FXMLLoader

    @FXML // fx:id="irvBtn"
    private Button irvBtn; // Value injected by FXMLLoader

    @FXML // fx:id="irvCalibrationAccess"
    private ChoiceBox<?> irvCalibrationAccess; // Value injected by FXMLLoader

    public ToggleGroup irvComTypeGroup;
    @FXML // fx:id="irvComExplicit"
    private RadioButton irvComExplicit; // Value injected by FXMLLoader
    @FXML // fx:id="irvComImplicit"
    private RadioButton irvComImplicit; // Value injected by FXMLLoader

    @FXML // fx:id="irvDataTypeProperty"
    private Button irvDataTypeProperty; // Value injected by FXMLLoader

    @FXML // fx:id="irvInitValueProperty"
    private Button irvInitValueProperty; // Value injected by FXMLLoader

    @FXML // fx:id="irvInitValueType"
    private ChoiceBox<?> irvInitValueType; // Value injected by FXMLLoader

    @FXML // fx:id="irvListView"
    private ListView<?> irvListView; // Value injected by FXMLLoader

    @FXML // fx:id="newIRVDataType"
    private Button newIRVDataType; // Value injected by FXMLLoader

    @FXML // fx:id="runnableBtn"
    private Button runnableBtn; // Value injected by FXMLLoader

    @FXML // fx:id="runnableListView"
    private ListView<?> runnableListView; // Value injected by FXMLLoader

    @FXML // fx:id="selectIRVDataType"
    private Button selectIRVDataType; // Value injected by FXMLLoader

    @FXML
    void onExclusiveAreaBtnClicked(MouseEvent event) {
        ObservableList<Node> children = stackPane.getChildren();
        children.removeAll(irvSplitPane, runnableSplitPane);
        if (!children.contains(exclusiveAreaSplitPane)) {
            children.add(exclusiveAreaSplitPane);
        }
    }

    @FXML
    void onIRVBtnClicked(MouseEvent event) {
        ObservableList<Node> children = stackPane.getChildren();
        children.removeAll(runnableSplitPane, exclusiveAreaSplitPane);
        if (!children.contains(irvSplitPane)) {
            children.add(irvSplitPane);
        }
    }

    @FXML
    void onRunnableBtnClicked(MouseEvent event) {
        ObservableList<Node> children = stackPane.getChildren();
        children.removeAll(irvSplitPane, exclusiveAreaSplitPane);
        if (!children.contains(runnableSplitPane)) {
            children.add(runnableSplitPane);
        }
    }

    @FXML
    void onActiveReasonPropertyBtnClicked(MouseEvent event) {

    }

    @FXML
    void onDeleteAccessPointBtnClicked(MouseEvent event) {

    }

    @FXML
    void onDeleteActiveReasonBtnClicked(MouseEvent event) {

    }

    @FXML
    void onDeleteIRVBtnClicked(MouseEvent event) {

    }

    @FXML
    void onDeleteTriggerBtnClicked(MouseEvent event) {

    }

    @FXML
    void onIRVDataTypePropertyBtnClicked(MouseEvent event) {

    }

    @FXML
    void onIRVDataTypeSelectionBtnClicked(MouseEvent event) {

    }

    @FXML
    void onIRVInitValuePropertyBtnClicked(MouseEvent event) {

    }

    @FXML
    void onNewActiveReasonBtnClicked(MouseEvent event) {

    }

    @FXML
    void onNewIRVBtnClicked(MouseEvent event) {

    }

    @FXML
    void onNewIRVDataTypeBtnClicked(MouseEvent event) {

    }

    /**
     * @param url            url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
