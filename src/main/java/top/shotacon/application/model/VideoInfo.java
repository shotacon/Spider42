package top.shotacon.application.model;

import javafx.beans.property.SimpleStringProperty;

public class VideoInfo {

    private SimpleStringProperty column;

    private SimpleStringProperty value;

    public VideoInfo(String c, String v) {
        super();
        this.column = new SimpleStringProperty(c);
        this.value = new SimpleStringProperty(v);
    }

    public SimpleStringProperty getColumn() {
        return column;
    }

    public void setColumn(SimpleStringProperty column) {
        this.column = column;
    }

    public SimpleStringProperty getValue() {
        return value;
    }

    public void setValue(SimpleStringProperty value) {
        this.value = value;
    }

}
