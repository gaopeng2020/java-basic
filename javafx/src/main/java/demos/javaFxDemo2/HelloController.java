/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package demos.javaFxDemo2;

import demos.javaFxDemo1.SexEnum;
import demos.javaFxDemo1.Student;
import ept.abstractClass.soa.SomeIpMatrixReader;
import ept.commonapi.EPTUtils;
import ept.consistency.ept.FailureReason;
import ept.consistency.ept.ExcelCheckerReturnType;
import ept.consistency.ept.PolicyBuilder;
import ept.consistency.ept.datatypedefinitionchecker.DataTypeDefinitionPolicy;
import ept.consistency.ept.machinedefinitionchecker.MachineDefinePolicy;
import ept.consistency.ept.sdtransformercheck.SdTransformerPolicy;
import ept.consistency.ept.serviceInterfaceDefinitionChecks.ServiceInterfaceDefinitionPolicy;
import ept.consistency.ept.servicedeploymentchecker.ServiceDeploymentPolicy;
import ept.consistency.ept.someIpMatrixPreCheck.SomeIpMatrixPreCheckPolicy;
import ept.consistency.ept.switchdefinitionchecker.SwitchDefinitionPolicy;
import ept.excelReader.ept.soa.EptCpMatrixReader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class HelloController {

    @FXML // fx:id="autosarTypeEnum"
    private ToggleGroup autosarTypeEnum; // Value injected by FXMLLoader

    @FXML // fx:id="filePath"
    private TextField filePath; // Value injected by FXMLLoader

    @FXML // fx:id="rootPane"
    private AnchorPane rootPane; // Value injected by FXMLLoader

    @FXML // fx:id="tableViewScrollPane"
    private ScrollPane tableViewScrollPane; // Value injected by FXMLLoader

//    @FXML // fx:id="tableView"
//    private TableView<Student> tableView; // Value injected by FXMLLoader

//    @FXML
//    private TableColumn<Student, String> tc_ErrorDescription;
//
//    @FXML
//    private TableColumn<Student, String> tc_ErrorType;
//
//    @FXML
//    private TableColumn<Student, String> tc_SheetName;

    @FXML
    private TableView<ExcelCheckerReturnType> tableView;

    @FXML
    private TableColumn<ExcelCheckerReturnType, String> tc_ErrorDescription;

    @FXML
    private TableColumn<ExcelCheckerReturnType, String> tc_ErrorType;

    @FXML
    private TableColumn<ExcelCheckerReturnType, String> tc_ErrorCategory;

    @FXML
    private TableColumn<ExcelCheckerReturnType, String> tc_SheetName;

    private String fileName;

    @FXML
    void fileChooserAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showOpenDialog(new Stage());
        fileName = file.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf(".") - 1);
        filePath.setText(file.getAbsolutePath());
    }

    @FXML
    void runConsistencyCheck(ActionEvent event) throws Exception {
        if (fileName == null || "".equals(fileName)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.set
            alert.setContentText("请先选择要检查的SOME/IP服务矩阵，在执行一致性检查！");
            alert.show();
            return;
        }
        String autosarType = ((RadioButton) autosarTypeEnum.getSelectedToggle()).getText();
        System.out.println("autosarType = " + autosarType);
        System.out.println("filePath.getText() = " + filePath.getText());

        Workbook workbook = EPTUtils.getWorkBookFromFile(new File(filePath.getText()));
        PolicyBuilder policyBuilder = new PolicyBuilder(workbook)
                .addPolicy(new SomeIpMatrixPreCheckPolicy())
                .addPolicy(new ServiceInterfaceDefinitionPolicy())
                .addPolicy(new ServiceDeploymentPolicy())
                .addPolicy(new DataTypeDefinitionPolicy())
                .addPolicy(new MachineDefinePolicy())
                .addPolicy(new SwitchDefinitionPolicy())
                .addPolicy(new SdTransformerPolicy());
        List<ExcelCheckerReturnType> allCheckResult = new ArrayList<>();
        List<ExcelCheckerReturnType> preReadCheckResult = policyBuilder.checkPoliciesPreRead();
        if (preReadCheckResult.stream().noneMatch(ExcelCheckerReturnType::isHasFailed)) {
            SomeIpMatrixReader someIpMatrixReader = new EptCpMatrixReader(workbook);
            someIpMatrixReader.readInfoFromExcel();
            policyBuilder.setPolicyExcelCheckContext(someIpMatrixReader);
            List<ExcelCheckerReturnType> excelCheckerReturnTypeList = policyBuilder.checkPolicies();
            excelCheckerReturnTypeList.forEach(excelCheckerReturnType -> {
                if (excelCheckerReturnType.isHasFailed()) {
                    allCheckResult.add(excelCheckerReturnType);
                }
            });
        } else {
            preReadCheckResult.forEach(excelCheckerReturnType -> {
                if (excelCheckerReturnType.isHasFailed()) {
                    allCheckResult.add(excelCheckerReturnType);
                }
            });
        }
        ObservableList<ExcelCheckerReturnType> observableList = FXCollections.observableArrayList(allCheckResult);
        output2TableView(observableList);
    }

    @FXML
    void saveLog(ActionEvent event) {
        if (fileName == null || "".equals(fileName)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请先执行一致性检查，在保存日志！");
            alert.show();
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Resource File");
        fileChooser.setInitialFileName(fileName + "_Log-" + format.format(new Date()));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.log"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File file = fileChooser.showSaveDialog(new Stage());
        System.out.println("file.getName() = " + file.getAbsolutePath());
    }

    @FXML //初始化函数
    private void initialize() {
        scrollPaneSizeListener();

//        initializeTableView();

        radioButtonListener();

    }

    private void radioButtonListener() {
        autosarTypeEnum.selectedToggleProperty().addListener((observableValue, oldToggle, newToggle) -> {
            RadioButton selectedRadioButton = (RadioButton) newToggle;
            System.out.println("selectedRadioButton = " + selectedRadioButton.getText());
        });
    }

//    private void initializeTableView() {
//        Student student1 = new Student("AAA", 15, "7", SexEnum.Boy);
//        Student student2 = new Student("BBB", 15, "8", SexEnum.Boy);
//        Student student3 = new Student("CCC", 14, "7", SexEnum.Boy);
//        Student student4 = new Student("DDD", 16, "9", SexEnum.Boy);
//        Student student5 = new Student("EEE", 15, "7", SexEnum.Boy);
//        ObservableList<Student> students = FXCollections.observableArrayList(student1, student2, student3, student4, student5);
//        tableView.setItems(students);
//
//        //将数据写入TableView
//        tc_ErrorType.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Student, String>, ObservableValue<String>>() {
//            @Override
//            public ObservableValue<String> call(TableColumn.CellDataFeatures<Student, String> param) {
//                return new SimpleStringProperty(param.getValue().getName());
//            }
//        });
//        tc_SheetName.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getAge())));
//
//        tc_ErrorDescription.setCellValueFactory(new PropertyValueFactory<Student, String>("sex"));
//
//        //自定义tc_ErrorType单元格样式
//        tc_ErrorType.setCellFactory(new Callback<TableColumn<Student, String>, TableCell<Student, String>>() {
//            @Override
//            public TableCell<Student, String> call(TableColumn<Student, String> studentStringTableColumn) {
//                return new TableCell<>() {
//                    @Override
//                    protected void updateItem(String s, boolean b) {
//                        super.updateItem(s, b);
//                        if (!b) {
//                            HBox hBox = new HBox();
//
//                            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/demos/javaFxDemo2/error.png"));
//                            Image image = new Image(is);
//                            ImageView imageView = new ImageView(image);
//                            imageView.setPreserveRatio(true);
//                            imageView.setFitWidth(16);
//
//                            Label label = new Label(s);
//
//                            hBox.getChildren().addAll(imageView,label);
//                            this.setGraphic(hBox);
//                        }
//                    }
//                };
//            }
//        });
//    }

        private void output2TableView(ObservableList<ExcelCheckerReturnType> observableList) {
        tableView.setItems(observableList);

        //将数据写入TableView
        tc_ErrorType.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(" [Error]"));
        tc_SheetName.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getCheckedSheetName())));
//        tc_ErrorCategory.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getCheckerFailureMessage())));
        tc_ErrorCategory.setCellValueFactory(new PropertyValueFactory<>("checkerFailureMessage"));

        tc_ErrorDescription.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ExcelCheckerReturnType, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<ExcelCheckerReturnType, String> cellDataFeatures) {
                StringBuilder sb = new StringBuilder();
                cellDataFeatures.getValue().getFailureReason().stream().map(FailureReason::getFailureReasonMessage).forEach(message -> {
                    sb.append(message).append(EPTUtils.ENTER_CHARACTER);
                });
                return new SimpleStringProperty(sb.toString());
            }
        });

        //自定义tc_ErrorType单元格样式
        tc_ErrorType.setCellFactory(new Callback<TableColumn<ExcelCheckerReturnType, String>, TableCell<ExcelCheckerReturnType, String>>() {
            @Override
            public TableCell<ExcelCheckerReturnType, String> call(TableColumn<ExcelCheckerReturnType, String> studentStringTableColumn) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String s, boolean b) {
                        super.updateItem(s, b);
                        if (!b) {
                            HBox hBox = new HBox();

                            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/ept/somipcmxcheck/error.png"));
                            Image image = new Image(is);
                            ImageView imageView = new ImageView(image);
                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(16);

                            Label label = new Label(s);

                            hBox.getChildren().addAll(imageView, label);
                            this.setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }

    private void scrollPaneSizeListener() {
        //tableViewScrollPane.heightProperty().addListener((observableValue, oldValue, newValue) -> tableView.setPrefHeight(newValue.doubleValue()));
        tableView.prefHeightProperty().bind(tableViewScrollPane.heightProperty());
        tableViewScrollPane.widthProperty().addListener((observableValue, oldValue, newValue) -> tableView.setPrefWidth(newValue.doubleValue() - 15));
    }
}
