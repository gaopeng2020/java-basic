/**
 * Sample Skeleton for 'hello-view.fxml' Controller Class
 */

package projects.smIpCmxCheck;


import ept.abstractClass.soa.SomeIpMatrixReader;
import ept.commonapi.AUTOSARTypeEnum;
import ept.commonapi.EPTUtils;
import ept.consistency.ept.ApplicationDefinition.ApplicationDefinitionPolicy;
import ept.consistency.ept.ExcelCheckerReturnType;
import ept.consistency.ept.FailureReason;
import ept.consistency.ept.Policy;
import ept.consistency.ept.PolicyBuilder;
import ept.consistency.ept.functiongroupdefinitionchecker.FunctionGroupDefinitionPolicy;
import ept.consistency.ept.machinedefinitionchecker.MachineDefinePolicy;
import ept.consistency.ept.sdtransformercheck.SdTransformerPolicy;
import ept.consistency.ept.serviceInterfaceDefinitionChecks.ServiceInterfaceDefinitionPolicy;
import ept.consistency.ept.servicedeploymentchecker.ServiceDeploymentPolicy;
import ept.consistency.ept.someIpMatrixPreCheck.SomeIpMatrixPreCheckPolicyAPChecker;
import ept.consistency.ept.someIpMatrixPreCheck.SomeIpMatrixPreCheckPolicyCPChecker;
import ept.consistency.ept.startupConfigurationChecker.StartupConfigurationPolicy;
import ept.consistency.ept.switchdefinitionchecker.SwitchDefinitionPolicy;
import ept.excelReader.ept.soa.EptCpMatrixReader;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author gaopeng
 */
@Slf4j
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

    private String someIpMatrixPath;
    private List<String> checkReports;
    private File cmxLastDir;
    private File reportLastDir;

    @FXML
    private void initialize() {
        //create or get MatrixInitDirectory.txt
        String cmxFileName = "MatrixInitDirectory.txt";
        cmxLastDir = getOrCreateFile(cmxFileName);
        //create or get LogInitDirectory.txt
        String logFileName = "LogInitDirectory.txt";
        reportLastDir = getOrCreateFile(logFileName);

        filePath.textProperty().addListener((observable, oldValue, newValue) -> {
            someIpMatrixPath = filePath.getText();
            System.out.println("someIpMatrixPath = " + someIpMatrixPath);
            someIpMatrixPath = someIpMatrixPath.substring(someIpMatrixPath.lastIndexOf("\\") + 1, someIpMatrixPath.lastIndexOf("."));
            System.out.println("someIpMatrixPath = " + someIpMatrixPath);
        });

        resultStatues.setText("请先选择一个基于SOME/IP的AP或CP服务接口矩阵,再选择AUTOSAR类型，最后执行检查！");
        resultStatues.setTextFill(Color.YELLOWGREEN);
    }

    private File getOrCreateFile(String fileName) {
        File directory = new File("./temp");
        if (!directory.exists()) {
            directory.mkdir();
        }
        String canonicalPath;
        try {
            canonicalPath = directory.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String finalCanonicalPath = canonicalPath;
        return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                .filter(file -> file.getName().equals(fileName))
                .findAny().orElseGet(() -> {
                    String s = finalCanonicalPath + EPTUtils.SLASH + fileName;
                    File file;
                    try {
                        file = new File(s);
                        file.createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return file;
                });
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

        setInitialDirectory(fileChooser, cmxLastDir);

        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            someIpMatrixPath = file.getName();
            someIpMatrixPath = someIpMatrixPath.substring(0, someIpMatrixPath.lastIndexOf("."));
            System.out.println("someIpMatrixPath = " + someIpMatrixPath);
            filePath.setText(file.getAbsolutePath());
            saveCurrentDirectory(file, cmxLastDir);
        }
    }

    /**
     * run Consistency Check button listener
     *
     * @param event event
     */
    @FXML
    void runConsistencyCheck(ActionEvent event) {
        if (EPTUtils.isStringEmpty(someIpMatrixPath)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请先选择要检查的SOME/IP服务矩阵，在执行一致性检查！");
            alert.show();
            return;
        }

        List<ExcelCheckerReturnType> allCheckResult = getExcelCheckResults();
        boolean checkResult = getCheckReports(allCheckResult);
        if (checkResult) {
            resultStatues.setText("恭喜您！SOME/IP服务矩阵一致性检查通过！");
            resultStatues.setTextFill(Color.GREEN);
        } else {
            output2TableView(FXCollections.observableArrayList(allCheckResult));
            resultStatues.setText("一致性检查未通过！请根据检查结果更新SOME/IP服务矩阵");
            resultStatues.setTextFill(Paint.valueOf("RED"));
        }
    }

    private List<ExcelCheckerReturnType> getExcelCheckResults() {
        String autosarType = ((RadioButton) autosarTypeEnum.getSelectedToggle()).getText();
        Workbook workbook;
        try {
            workbook = EPTUtils.getWorkBookFromFile(new File(filePath.getText()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Policy sheetAndTitleCheckPolicy;
        AUTOSARTypeEnum autosarTypeEnum = AUTOSARTypeEnum.CLASSIC;
        if (autosarType.equalsIgnoreCase("ADAPTIVE AUTOSAR")) {
            sheetAndTitleCheckPolicy = new SomeIpMatrixPreCheckPolicyAPChecker();
            autosarTypeEnum = AUTOSARTypeEnum.ADAPTIVE;
        } else {
            sheetAndTitleCheckPolicy = new SomeIpMatrixPreCheckPolicyCPChecker();
        }

        List<ExcelCheckerReturnType> checkResult;
        PolicyBuilder workbookReadPolicy = new PolicyBuilder(workbook)
                .addPolicy(sheetAndTitleCheckPolicy);
        checkResult = workbookReadPolicy.checkPoliciesPreRead();

        if (checkResult.stream().noneMatch(ExcelCheckerReturnType::isHasFailed)) {
            PolicyBuilder policyBuilder = new PolicyBuilder(workbook)
                    .addPolicy(new ServiceInterfaceDefinitionPolicy())
                    .addPolicy(new ServiceDeploymentPolicy())
                    //.addPolicy(new DataTypeDefinitionPolicy())
                    .addPolicy(new MachineDefinePolicy())
                    .addPolicy(new SwitchDefinitionPolicy())
                    .addPolicy(new SdTransformerPolicy());
            if (autosarTypeEnum == AUTOSARTypeEnum.ADAPTIVE) {
                policyBuilder.addPolicy(new ApplicationDefinitionPolicy())
                        .addPolicy(new StartupConfigurationPolicy())
                        .addPolicy(new FunctionGroupDefinitionPolicy());
            }

            List<ExcelCheckerReturnType> preReadCheckResult = policyBuilder.checkPoliciesPreRead();
            checkResult.addAll(preReadCheckResult);

            if (preReadCheckResult.stream().noneMatch(ExcelCheckerReturnType::isHasFailed)) {
                SomeIpMatrixReader someIpMatrixReader = new EptCpMatrixReader(workbook);
                someIpMatrixReader.setAutosarType(autosarTypeEnum);
                someIpMatrixReader.readInfoFromExcel();
                policyBuilder.setPolicyExcelCheckContext(someIpMatrixReader);
                List<ExcelCheckerReturnType> excelCheckerReturnTypeList = policyBuilder.checkPolicies();
                checkResult.addAll(excelCheckerReturnTypeList);
            }
        }
        return checkResult.stream().filter(Objects::nonNull)
                .filter(ExcelCheckerReturnType::isHasFailed)
                .distinct().collect(Collectors.toList());
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
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (!empty) {
                            HBox hBox = new HBox();
                            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/projects/smIpCmxCheck/error.png"));
                            Image image = new Image(is);
                            javafx.scene.image.ImageView imageView = new ImageView(image);
                            imageView.setPreserveRatio(true);
                            imageView.setFitWidth(16);

                            Label label = new Label(item);
                            hBox.getChildren().addAll(imageView, label);
                            this.setGraphic(hBox);
                            hBox.setAlignment(Pos.CENTER_LEFT);
                        }
                    }
                };
            }
        });
        stringTableCellCenterAlignment(colSheetName, Pos.CENTER_LEFT);
        stringTableCellCenterAlignment(colErrorCategory, Pos.CENTER_LEFT);
    }

    private void stringTableCellCenterAlignment(TableColumn<ExcelCheckerReturnType, String> colSheetName, Pos alignment) {
        colSheetName.setCellFactory(new Callback<>() {
            @Override
            public TableCell<ExcelCheckerReturnType, String> call(TableColumn<ExcelCheckerReturnType, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        String prefix = "[Error] ";
                        if (item != null && item.startsWith(prefix)) {
                            item = item.substring(prefix.length());
                        }
                        super.updateItem(item, empty);
                        this.setText(item);
                        this.setAlignment(alignment);
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
        if (someIpMatrixPath == null || "".equals(someIpMatrixPath)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("请先执行一致性检查，才能保存一致性检查结果！");
            alert.show();
            return;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Resource File");
        fileChooser.setInitialFileName(someIpMatrixPath + "_Log-" + format.format(new Date()));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        //get Log InitDirectory and setInitialDirectory for fileChooser
        setInitialDirectory(fileChooser, reportLastDir);

        File file = fileChooser.showSaveDialog(new Stage());
        if (file == null) {
            return;
        }
        //save current selected directory to LogInitDirectory.txt
        saveCurrentDirectory(file, reportLastDir);

        //save check result to txt or excel log file
        String fileName = file.getName();
        String postfix = fileName.substring(fileName.lastIndexOf(EPTUtils.DOTE) + 1);
        switch (postfix) {
            case "xlsx" -> saveLog2Excel(file, getExcelCheckResults());
            case "txt" -> {
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
            default -> log.error("不支持的日志类型：{}", postfix);
        }
    }

    private void saveLog2Excel(@NonNull File file, @NonNull List<ExcelCheckerReturnType> excelCheckResults) {
        if (excelCheckResults.isEmpty()) {
            return;
        }
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        EPTUtils.createExcelTitle(sheet, new String[]{"错误类型", "Sheet名称", "错误类别", "错误描述"});
        for (int i = 0; i < excelCheckResults.size(); i++) {
            ExcelCheckerReturnType returnType = excelCheckResults.get(i);
            StringBuilder failureReason = new StringBuilder();
            returnType.getFailureReason().stream().map(FailureReason::getFailureReasonMessage)
                    .forEach(message -> failureReason.append(message).append(EPTUtils.ENTER_CHARACTER));
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue("[Error]");
            row.createCell(1).setCellValue(returnType.getCheckedSheetName());
            row.createCell(2).setCellValue(returnType.getCheckerFailureMessage());
            row.createCell(3).setCellValue(failureReason.toString());
        }
        EPTUtils.autoFormatWorkSheet(sheet);
        try {
            EPTUtils.exportWorkbook2Excel(workbook, file.getAbsolutePath());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * set Initial Directory for file chooser
     *
     * @param fileChooser file chooser
     * @param file        Initial Directory file
     */
    private void setInitialDirectory(@NonNull FileChooser fileChooser, @NonNull File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[104];
            int len;
            while ((len = fis.read(bytes)) != -1) {
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
     * @param file     current selected file
     * @param cashFile Initial directory file
     */
    private void saveCurrentDirectory(@NonNull File file, File cashFile) {
        if (cashFile == null) {
            log.error("cannot find cash File");
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(cashFile)) {
            byte[] bytes = file.getParent().getBytes();
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
