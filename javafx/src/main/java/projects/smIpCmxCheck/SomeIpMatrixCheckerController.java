/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package projects.smIpCmxCheck;


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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author gaopeng
 */
public class SomeIpMatrixCheckerController {

    @FXML
    private ToggleGroup autosarTypeEnum;

    @FXML
    private TextField filePath;

    @FXML
    private TableColumn<ExcelCheckerReturnType, String> colErrorType;
    @FXML
    private TableColumn<ExcelCheckerReturnType, String> colSheetName;
    @FXML
    private TableColumn<ExcelCheckerReturnType, String> colErrorCategory;
    @FXML
    private TableColumn<ExcelCheckerReturnType, String> colErrorDescription;

    @FXML
    private Label resultStatues;

    private String fileName;
    private List<String> checkReports;

    @FXML
    private void initialize() {
        resultStatues.setText("一致性检查通过！");
        resultStatues.setTextFill(Color.GREEN);
    }

    /**
     * file Chooser button listener
     *
     * @param event event
     */
    @FXML
    void fileChooserAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        String name = "/projects/smIpCmxCheck/MatrixInitDirectory.txt";
//        String name = "MatrixInitDirectory.txt";
        if ("jar".equals(SomeIpMatrixCheckerController.class.getResource("").getProtocol())) {

        } else {
            String path = Objects.requireNonNull(getClass().getResource(name)).getPath();
            System.out.println("path = " + path);
        }


        setInitialDirectory(fileChooser, name);

        File file = fileChooser.showOpenDialog(new Stage());
        fileName = file.getName();
        fileName = fileName.substring(0, fileName.lastIndexOf(".") - 1);
        filePath.setText(file.getAbsolutePath());
        try {
            saveCurrentDirectory(file, name);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * run Consistency Check button listener
     *
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

        Workbook workbook;
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

        boolean checkResult = getCheckReports(allCheckResult);
        if (checkResult) {
            resultStatues.setText("恭喜您！SOME/IP服务矩阵一致性检查通过！");
            resultStatues.setTextFill(Color.GREEN);
        } else {
            output2TableView(FXCollections.observableArrayList(allCheckResult));
            resultStatues.setText("一致性检查通过！请根据检查结果更新SOME/IP服务矩阵");
            resultStatues.setTextFill(Paint.valueOf("RED"));
        }
    }

    /**
     * output check result to TableView
     *
     * @param observableList observableList
     */
    private void output2TableView(ObservableList<ExcelCheckerReturnType> observableList) {
        colErrorType.getTableView().setItems(observableList);

        //将数据写入TableView
        colErrorType.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(" [Error]"));
        colSheetName.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(String.valueOf(cellDataFeatures.getValue().getCheckedSheetName())));
        colErrorCategory.setCellValueFactory(new PropertyValueFactory<>("checkerFailureMessage"));

        colErrorDescription.setCellValueFactory(cellDataFeatures -> {
            StringBuilder sb = new StringBuilder();
            cellDataFeatures.getValue().getFailureReason().stream().map(FailureReason::getFailureReasonMessage)
                    .forEach(message -> sb.append(message).append(EPTUtils.ENTER_CHARACTER));
            return new SimpleStringProperty(sb.toString());
        });

        //自定义tc_ErrorType单元格样式
        colErrorType.setCellFactory(new Callback<>() {
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
                            javafx.scene.image.ImageView imageView = new ImageView(image);
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
     *
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
     *
     * @param event event
     */
    @FXML
    void saveCheckResult(ActionEvent event) {
        if (fileName == null || "".equals(fileName)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请先执行一致性检查，才能保存一致性检查结果！");
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
        String path = Objects.requireNonNull(SomeIpMatrixCheckerController.class.getResource("LogInitDirectory.txt")).getPath();
        setInitialDirectory(fileChooser, path);

        //save current selected directory to LogInitDirectory.txt
        File file = fileChooser.showSaveDialog(new Stage());
        try {
            saveCurrentDirectory(file, path);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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
        String path1 = SomeIpMatrixCheckerController.class.getClassLoader().getResource(path).getPath();
        System.out.println("path1 = " + path1);
        try (InputStream is = SomeIpMatrixCheckerController.class.getClassLoader().getResourceAsStream(path)) {
            byte[] bytes = new byte[104];
            int len;
            while ((len = is.read(bytes)) != -1) {
                String lastDir = new String(bytes, 0, len);
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
    private void saveCurrentDirectory(File file, String path) throws URISyntaxException {
        String filePath = System.getProperty("user.dir").replace("\\", "/");
        System.out.println("filePath = " + filePath);
        File cashFile = new File(Objects.requireNonNull(SomeIpMatrixCheckerController.class.getResource(path)).toURI());
        try (FileOutputStream fos = new FileOutputStream(cashFile)) {
            byte[] bytes = file.getParent().getBytes();
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
