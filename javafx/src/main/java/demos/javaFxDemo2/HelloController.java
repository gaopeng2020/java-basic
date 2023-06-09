/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package demos.javaFxDemo2;

import ept.abstractClass.soa.SomeIpMatrixReader;
import ept.commonapi.EPTUtils;
import ept.consistency.ept.ExcelCheckerReturnType;
import ept.consistency.ept.FailureReason;
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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelloController {

    @FXML
    private ToggleGroup autosarTypeEnum;

    @FXML
    private TextField filePath;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private ScrollPane tableViewScrollPane;


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
    private List<String> checkReports;

    /**
     * file Chooser button listener
     * @param event event
     */
    @FXML
    void fileChooserAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        String path = Objects.requireNonNull(getClass().getResource(" /projects/smIpCmxCheck/MatrixInitDirectory.txt")).getPath();
        setInitialDirectory(fileChooser, path);

        File file = fileChooser.showOpenDialog(new Stage());
        fileName = file.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf(".") - 1);
        filePath.setText(file.getAbsolutePath());
        saveCurrentDirectory(file, path);
    }

    /**
     * run Consistency Check button listener
     * @param event event
     */
    @FXML
    void runConsistencyCheck(ActionEvent event) {
        if (fileName == null || "".equals(fileName)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请先选择要检查的SOME/IP服务矩阵，在执行一致性检查！");
            alert.show();
            return;
        }
        String autosarType = ((RadioButton) autosarTypeEnum.getSelectedToggle()).getText();
        System.out.println("autosarType = " + autosarType);
        System.out.println("filePath.getText() = " + filePath.getText());

        Workbook workbook = null;
        try {
            workbook = EPTUtils.getWorkBookFromFile(new File(filePath.getText()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        output2TableView(FXCollections.observableArrayList(allCheckResult));

        boolean checkResult = getCheckReports(allCheckResult);
        if (checkResult) {

        }
    }

    /**
     * output check result to TableView
     * @param observableList observableList
     */
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

                            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(" /projects/smIpCmxCheck/error.png"));
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

    /**
     * get Check Reports
     * @param allCheckResult allCheckResult
     * @return true if no error
     */
    private boolean getCheckReports(List<ExcelCheckerReturnType> allCheckResult) {
        checkReports = new LinkedList<>();
        String date = new Date().toString();
        allCheckResult.forEach(returnType -> {
            StringBuilder sb = new StringBuilder(date);
            String sheetName = returnType.getCheckedSheetName();
            String failureMessage = returnType.getCheckerFailureMessage();
            StringBuilder failureReason = new StringBuilder();
            returnType.getFailureReason().stream().map(FailureReason::getFailureReasonMessage)
                    .forEach(message -> failureReason.append(message).append(EPTUtils.ENTER_CHARACTER));

            sb.append(EPTUtils.TAB_CHARACTER).append("[Error]")
                    .append(EPTUtils.TAB_CHARACTER).append(sheetName)
                    .append(EPTUtils.TAB_CHARACTER).append(failureMessage)
                    .append(EPTUtils.TAB_CHARACTER).append(failureReason);
            checkReports.add(sb.toString());
        });
        return checkReports.size() == 0;
    }

    /**
     * save check result to Log
     * @param event event
     */
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

        //get Log InitDirectory and setInitialDirectory for fileChooser
        String path = Objects.requireNonNull(getClass().getResource(" /projects/smIpCmxCheck/LogInitDirectory.txt")).getPath();
        setInitialDirectory(fileChooser, path);

        //save current selected directory to LogInitDirectory.txt
        File file = fileChooser.showSaveDialog(new Stage());
        saveCurrentDirectory(file, path);
        System.out.println("file.getName() = " + file.getAbsolutePath());

        //save check reports to log
        try (FileWriter fw = new FileWriter(file)) {
            checkReports.forEach(report -> {
                try {
                    fw.write(report);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * set Initial Directory for file chooser
     *
     * @param fileChooser file chooser
     * @param path        Initial Directory path
     */
    private void setInitialDirectory(FileChooser fileChooser, String path) {
        try (FileInputStream fis = new FileInputStream(path);) {
            byte[] bytes = new byte[104];
            int len;
            while ((len = fis.read(bytes)) != -1) {
                String lastDir = new String(bytes, 0, len);
                System.out.println("lastDir = " + lastDir);
                fileChooser.setInitialDirectory(new File(lastDir));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * save th directory of current selected file to Initial directory
     *
     * @param file current selected file
     * @param path Initial directory path
     */
    private void saveCurrentDirectory(File file, String path) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            byte[] bytes = file.getParent().getBytes();
            System.out.println("newDir = " + new String(bytes));
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
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

    private void scrollPaneSizeListener() {
        //tableViewScrollPane.heightProperty().addListener((observableValue, oldValue, newValue) -> tableView.setPrefHeight(newValue.doubleValue()));
        tableView.prefHeightProperty().bind(tableViewScrollPane.heightProperty());
        tableViewScrollPane.widthProperty().addListener((observableValue, oldValue, newValue) -> tableView.setPrefWidth(newValue.doubleValue() - 15));
    }
}
