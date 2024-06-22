package demos.javaFxDemo1;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;

import java.io.File;

public class HelloController {
    public ListView<String> listView;
    @FXML
    private AnchorPane rootPane;

    @FXML
    @Getter
    private ComboBox<String> comboBox1;
    @FXML
    @Getter
    private ChoiceBox<String> choiceBox;

    @FXML // fx:id="cancelButton"
    private Button cancelButton; // Value injected by FXMLLoader

    @FXML //初始化函数
    private void initialize() {
        //init item of comboBox1
        comboBox1.setItems(FXCollections.observableArrayList("combo1", "combo2", "combo3", "combo4"));
        comboBox1.getItems().addAll("combo5", "combo6");
        comboBox1.setValue(comboBox1.getItems().get(comboBox1.getItems().size() - 1)); //初始值

        //init choiceBox
        choiceBox.setItems(FXCollections.observableArrayList("Item1", "Item2", "Item3"));
        choiceBox.setValue(comboBox1.getItems().get(0));

        //layout size
        MenuBar menuBar = (MenuBar) rootPane.lookup("#munuBar");

        FlowPane flowPane = (FlowPane) rootPane.lookup("#flowPane");
        DoubleBinding binding = rootPane.heightProperty().multiply(0.1);
        flowPane.prefHeightProperty().bind(binding);
        flowPane.prefWidthProperty().bind(rootPane.widthProperty());

        comboBox1.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.2));
        ComboBox<?> comboBox2 = (ComboBox<?>) rootPane.lookup("#comboBox2");
        comboBox2.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.2));
        choiceBox.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.2));

        ButtonBar buttonBar = (ButtonBar) rootPane.lookup("#buttonBar");
        buttonBar.prefHeightProperty().bind(binding);

        HBox hbox = (HBox) rootPane.lookup("#hbox");
//        hbox.prefHeightProperty().bind(rootPane.heightProperty().subtract(bBoxHeight));
//        hbox.prefHeightProperty().bind(rootPane.heightProperty().subtract(125));
        hbox.prefWidthProperty().bind(rootPane.widthProperty());

        listViewInit();
        tableViewInit();

        TreeView<String> treeView = (TreeView<String>) rootPane.lookup("#treeView");
        treeView.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.25));
    }

    private void tableViewInit() {
        TableView<Student> tableview = (TableView<Student>) rootPane.lookup("#tableView");
        //bind width
        tableview.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        tableview.setPlaceholder(new Button("No Data"));

        //construct data
        Student student1 = new Student("AAA", 15, "7", SexEnum.Boy);
        Student student2 = new Student("BBB", 15, "8", SexEnum.Boy);
        Student student3 = new Student("CCC", 14, "7", SexEnum.Boy);
        Student student4 = new Student("DDD", 16, "9", SexEnum.Boy);
        Student student5 = new Student("EEE", 15, "7", SexEnum.Boy);
        ObservableList<Student> students = FXCollections.observableArrayList(student1, student2, student3, student4, student5);
        tableview.setItems(students);

        //create table column
        TableColumn<Student, String> studentNameColumn = new TableColumn<>("Name");
        TableColumn<Student, Number> studentAgeColumn = new TableColumn<>("Age");
        TableColumn<Student, String> studentGradeColumn = new TableColumn<>("Grade");
        TableColumn<Student, String> studentSexColumn = new TableColumn<>("Sex");
        tableview.getColumns().addAll(studentNameColumn, studentAgeColumn, studentGradeColumn, studentSexColumn);

        //add data
        studentNameColumn.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getName()));
        studentAgeColumn.setCellValueFactory(cellDataFeatures -> new SimpleIntegerProperty(cellDataFeatures.getValue().getAge()));
//        studentGradeColumn.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getGrade()));
        studentGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        studentSexColumn.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue().getSex().toString()));
//        tableview.getItems().add

        //edit table
        tableview.setEditable(true);
        studentAgeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            @Override
            public String toString(Number number) {
                return String.valueOf(number.intValue());
            }

            @Override
            public Number fromString(String s) {
                return Integer.valueOf(s);
            }
        }));

        //multi selected mode
        tableview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //获取选择的项
        tableview.getSelectionModel().getSelectedItems();

        //监听选择项
        tableview.getSelectionModel().selectedItemProperty().addListener((observableValue, oldItem, newItem) ->
                System.out.println("newItem.toString() = " + newItem.toString()));
        ContextMenu tableviewContextMenu = tableview.getContextMenu();

    }

    private void listViewInit() {
//        ListView<String> listView = (ListView<String>) rootPane.lookup("#listView");
        listView.setItems(FXCollections.observableArrayList("AAAAA", "BBBBB", "CCCCC", "DDDDD"));
        listView.getItems().add("EEEEE");

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                System.out.println("t1 = " + t1);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Selected Items is ...");
                alert.setContentText(t1);
                alert.setHeaderText(t1);
                Button button = (Button) alert.getDialogPane().lookupButton(ButtonType.OK); //获取面板上的Button
//                alert.getButtonTypes().add(ButtonType.APPLY);
//                alert.show();
//                PropertyChangeSupport pcs = new PropertyChangeSupport();
            }
        });
        listView.setEditable(true);
        listView.setCellFactory(TextFieldListCell.forListView(new StringConverter<String>() {
            @Override
            public String toString(String s) {
                return s;
            }

            @Override
            public String fromString(String s) {
                return s;
            }
        }));
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.25));

        ContextMenu listViewContextMenu = listView.getContextMenu();
        listView.setOnMouseClicked(mouseEvent -> listViewContextMenu.getItems().forEach(item -> {
            if (listView.getItems().size()<=1 && item.getText().equals("Delete")) {
                item.setDisable(true);
            }else if (listView.getItems().size()>=10 && item.getText().equals("New")) {
                item.setDisable(true);
            }else {
                item.setDisable(false);
            }
        }));
    }

    @FXML
    void OnKeyAPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.A)) {
            System.out.println("Pressed Key Name = " + event.getCode().getName());
        }
    }

    @FXML
    private void cancelButtonOnMouseClicked(MouseEvent event) {
        if (event.getClickCount() == 1 && event.getButton().name().equals(MouseButton.PRIMARY.name())) {
            Stage window = (Stage) cancelButton.getScene().getWindow();
            window.close();
        }
    }

    @FXML
    void anchorOnMouseDragged(MouseEvent event) {
        System.out.println("anchorOnMouseDragged EventType = " + event.getEventType().getName());
    }

    @FXML
    void onFileCloseAction(ActionEvent event) {
        Stage window = (Stage) cancelButton.getScene().getWindow();
        window.close();
    }

    @FXML
    void onFileOpenAction(ActionEvent event) {
        MenuItem eventSource = (MenuItem) event.getSource();
        System.out.println("eventSource.getId() = " + eventSource.getId());
//        if (event.getClickCount() == 1 && event.getButton().name().equals(MouseButton.PRIMARY.name())) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(new Stage());
        System.out.println("file.getName() = " + file.getAbsolutePath());
//        }
    }

    @FXML
    void onFileSaveAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(new Stage());
        System.out.println("file.getName() = " + file.getAbsolutePath());
    }


    @FXML
    void onListViewDelete(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        ObservableList<String> selectedItems = listView.getSelectionModel().getSelectedItems();
        listView.getItems().removeAll(selectedItems);
//        listView.refresh();
    }

    @FXML
    void onListViewNewAction(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        long count = listView.getItems().size()+1;
        listView.getItems().add("Item"+count);
    }

    @FXML
    void onTableViewDeleteAction(ActionEvent event) {

    }

    @FXML
    void onTableViewNewAction(ActionEvent event) {

    }

    @FXML
    void onTableViewPropertiesAction(ActionEvent event) {

    }
}