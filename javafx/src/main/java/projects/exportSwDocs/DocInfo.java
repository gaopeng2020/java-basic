package projects.exportSwDocs;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DocInfo {
    private final SimpleIntegerProperty num;
    private final SimpleStringProperty name;
    private final SimpleStringProperty uuid;
    private final SimpleBooleanProperty check;
    private final SimpleStringProperty exportStatus;



    public DocInfo(int num, String name, String uuid, boolean check, String exportStatus) {
        this.num = new SimpleIntegerProperty(num);
        this.name = new SimpleStringProperty(name);
        this.uuid = new SimpleStringProperty(uuid);
        this.check = new SimpleBooleanProperty(check);
        this.exportStatus = new SimpleStringProperty(exportStatus);
    }

    public int getNum() {
        return num.get();
    }

    public SimpleIntegerProperty numProperty() {
        return num;
    }

    public void setNum(int num) {
        this.num.set(num);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUuid() {
        return uuid.get();
    }

    public SimpleStringProperty uuidProperty() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public boolean isCheck() {
        return check.get();
    }

    public SimpleBooleanProperty checkProperty() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check.set(check);
    }
    public String getExportStatus() {
        return exportStatus.get();
    }

    public SimpleStringProperty exportStatusProperty() {
        return exportStatus;
    }

    public void setExportStatus(String exportStatus) {
        this.exportStatus.set(exportStatus);
    }

    @Override
    public String toString() {
        return "DocInfo{" +
                "num=" + num +
                ", name=" + name +
                ", uuid=" + uuid +
                ", check=" + check +
                ", exportStatus=" + exportStatus +
                '}';
    }
}
