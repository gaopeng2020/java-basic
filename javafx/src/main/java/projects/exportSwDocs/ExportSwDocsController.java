package projects.exportSwDocs;

import ept.commonapi.EPTUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ExportSwDocsController {

    public TextField tf_Search;

    private File userDataFile;
    private static UserInputInfo userData;
    private ObservableList<DocInfo> docInfoList;

    @FXML
    private TextField exportRange;
    @FXML
    private TableColumn<DocInfo, Number> docNumCol;
    @FXML
    private TableColumn<DocInfo, String> docNameCol;
    @FXML
    private TableColumn<DocInfo, String> docUuidCol;
    @FXML
    public TableColumn<DocInfo, Boolean> docCheckStatus;
    @FXML
    public TableColumn<DocInfo, String> docExportStatus;

    @FXML
    private TextField filePath;
    @FXML
    private TableView<DocInfo> tableView;

    @FXML
    private void initialize() throws IOException {
        // create or get MatrixInitDirectory.txt
        userDataFile = getOrCreateFile("userData");
        System.out.println("userDataFile.getAbsolutePath() = " + userDataFile.getCanonicalPath());

        if (userDataFile.length() == 0) {
            userData = new UserInputInfo("", "");
        } else {
            readUserDataFromFile();
        }

        initTableView();

//        File file = new File("cmake-3.28.1/bin");
//        if (file.exists()) {
//            String path = file.getCanonicalPath();
//            log.info("cmake path = {}", path);
//            JavaInvokeCmd cmd = new JavaInvokeCmd(Thread.currentThread());
//            cmd.inputCommand("cd " + path);
//            cmd.inputCommand("cmake -version");
//        } else {
//            log.error("Please place cmake in the root directory");
//        }
    }

    @FXML
    void onOpenBtnMousePressed(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Document info Excel exported from System Weaver");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));

        if (!userData.getLastOpenDir().isEmpty()) {
            fileChooser.setInitialDirectory(new File(userData.getLastOpenDir()));
        }

        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) {
            log.error("File Not Found after open dialog closed");
            return;
        }
        filePath.setText(file.getAbsolutePath());
        log.info("Doc Info path = {}", filePath.getText());

//        saveCurrentDirectory
        userData.setLastOpenDir(file.getParent());

        docInfoList = getDocInfo(filePath.getText());
        tableView.getItems().addAll(docInfoList);

        writeUserDataToFile();
    }


    /**
     * output check result to TableView
     */
    private void initTableView() {
        tableView.setEditable(true);

        tableView.setTableMenuButtonVisible(true);

        docNumCol.setCellValueFactory(tc -> tc.getValue().numProperty());
        docNameCol.setCellValueFactory(tc -> tc.getValue().nameProperty());

        //可双击编辑单元格
        docNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        docNameCol.setOnEditCommit(event -> System.out.println("new doc Name = " + event.getNewValue()));

        docUuidCol.setCellValueFactory(tc -> tc.getValue().uuidProperty());
        docUuidCol.setCellFactory(TextFieldTableCell.forTableColumn());
        docUuidCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DocInfo, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<DocInfo, String> event) {
                event.getRowValue().setUuid("0x0000000000000");
                System.out.println("new doc ID = " + event.getNewValue());
            }
        });

        docCheckStatus.setCellValueFactory(tc -> tc.getValue().checkProperty());
        docCheckStatus.setCellFactory(tc -> new CheckBoxTableCell<>());

        docExportStatus.setCellValueFactory(tc -> tc.getValue().exportStatusProperty());

        //multi selected mode
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


        //监听选择项
        tableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldItem, newItem) -> {
            if (newItem != null) {
                System.out.println("newItem = " + newItem);
            }
        });

        //搜索TableView
        tf_Search.textProperty().addListener((observableValue, oldVale, newValue) -> {
            FilteredList<DocInfo> list = docInfoList.filtered(docInfo -> docInfo.getName().toLowerCase().contains(newValue.toLowerCase())
                    || docInfo.getUuid().toLowerCase().contains(newValue.toLowerCase()));
            tableView.setItems(list);
        });
    }

    /**
     * get Doc Info list
     *
     * @param docInfoPath docInfoPath
     * @return List<DocInfo>
     */
    private ObservableList<DocInfo> getDocInfo(String docInfoPath) {
        List<DocInfo> list = new LinkedList<>();
        SplitExcel2Array excel2Array = new SplitExcel2Array(new File(docInfoPath), "");
        Map<String, String>[] docInfos = excel2Array.getSplitedDocInfo();
        AtomicInteger num = new AtomicInteger();
        for (Map<String, String> map : docInfos) {
            map.forEach((name, uuid) -> {
                var docInfo = new DocInfo(num.get(), name, uuid, false, "");
                list.add(docInfo);
                num.getAndIncrement();
            });
        }
        return FXCollections.observableArrayList(list);
    }

    @FXML
    void onExportBtnMousePressed(MouseEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select Document saved path");

        if (!userData.getLastSaveDir().isEmpty()) {
            chooser.setInitialDirectory(new File(userData.getLastSaveDir()));
        }
        File file = chooser.showDialog(new Stage());

        //save dir path to file
        try {
            userData.setLastSaveDir(file.getCanonicalPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeUserDataToFile();

        List<DocInfo> checkedItems = tableView.getItems().stream().filter(DocInfo::isCheck).toList();

        checkedItems.forEach(item -> {
            item.setExportStatus("In Progress...");
            log.info("{} was checked", item.getName());
            String path = file.getAbsolutePath() + "/" + item.getName() + ".txt";
//            exportDocInfo2File(path, item.getUuid());
        });

        //周期调用
        checkDocExportCompletely(checkedItems, file);
    }

    private void checkDocExportCompletely(List<DocInfo> checkedItems, File file) {
        CountDownLatch latch = new CountDownLatch(1);
        ScheduledThreadPoolExecutor poolExecutor = new ScheduledThreadPoolExecutor(1);

        ScheduledFuture<?> scheduledFuture = poolExecutor.scheduleWithFixedDelay(() -> {
            DocInfo docInfo = checkedItems.stream()
                    .filter(item -> item.getExportStatus().equals("In Progress...")).findFirst().orElse(null);
            if (docInfo == null) {
                latch.countDown();
                return;
            }

            File[] files = file.listFiles();
            System.out.println("files.length = " + files.length);
            for (File containedFile : Objects.requireNonNull(files)) {
                if (containedFile.getName().contains(docInfo.getName())) {
                    docInfo.setExportStatus("Export Completely!");
                }
            }
        }, 1000, 1000, TimeUnit.MICROSECONDS);

        //取消定时任务
        try {
            latch.await();
            if (latch.getCount() == 0) {
                scheduledFuture.cancel(true);
                poolExecutor.shutdown();
                log.info("All selected documents export completely.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void exportDocInfo2File(String path, String content) {
        try (FileOutputStream out = new FileOutputStream(path)) {
            byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            out.write(bytes);
            Thread.sleep(1000);
        } catch (IOException e) {
            log.error("{}", e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void onNewItemAction(ActionEvent actionEvent) {
        int num = tableView.getItems().size() + 1;
        var docInfo = new DocInfo(num, "必填项", "必填项", false, "");
        tableView.getItems().add(docInfo);
    }

    @FXML
    public void onCheckAllAction(ActionEvent actionEvent) {
        tableView.getItems().forEach(docInfo -> docInfo.setCheck(true));
    }

    @FXML
    public void onCheckSelectedAction(ActionEvent actionEvent) {
        tableView.getItems().forEach(docInfo -> docInfo.setCheck(false));
    }

    @FXML
    public void onUncheckAllAction(ActionEvent actionEvent) {
        tableView.getSelectionModel().getSelectedItems().forEach(docInfo -> docInfo.setCheck(false));
    }

    @FXML
    public void onUncheckSelectedAction(ActionEvent actionEvent) {
        tableView.getSelectionModel().getSelectedItems().forEach(docInfo -> docInfo.setCheck(true));
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

    private void readUserDataFromFile() {
        try (var in = new ObjectInputStream(new FileInputStream(userDataFile))) {
            userData = (UserInputInfo) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeUserDataToFile() {
        try (var out = new ObjectOutputStream(new FileOutputStream(userDataFile))) {
            out.writeObject(userData);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void onRevealInExplorerAction(ActionEvent actionEvent) {
        try {
            Runtime.getRuntime().exec("explorer /select,"+".");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
