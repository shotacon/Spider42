package top.shotacon.application;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import top.shotacon.application.spider.Pornhub;
import top.shotacon.application.spider.UrlUtil;

public class MainScene implements Initializable {

    private static final String pornhub = "Pornhub";
    private static final String youtube = "Youtube";

    private static final String defaultHost = "127.0.0.1";
    private static final String defaultPort = "8001";

    @FXML
    private Button clickButton;

    @FXML
    private TextField textParam;

//    @FXML
//    private TextArea textArea;
    @FXML
    private TableView<VideoInfo> tableView;

    @FXML
    private TableColumn<VideoInfo, String> tvColumn;

    @FXML
    private TableColumn<VideoInfo, String> tvValue;

    @FXML
    private ComboBox<String> siteTypeList;

    @FXML
    private CheckBox isProxy;

    @FXML
    private TextField proxyPortText;

    @FXML
    private TextField proxyHostText;

    private ObservableList<VideoInfo> dataList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        textArea.editableProperty().set(false);
        List<String> list = Arrays.asList(youtube, pornhub);
        siteTypeList.getItems().addAll(list);
        siteTypeList.setPromptText("Site Type");

        tvColumn.setCellValueFactory(cellData -> cellData.getValue().getColumn());
        tvValue.setCellValueFactory(cellData -> cellData.getValue().getValue());
        tvValue.setCellFactory(TextFieldTableCell.forTableColumn());
        tableView.setItems(dataList);
        tableView.setEditable(true);
    }

    public void onClickButtonClick(ActionEvent event) {
        dataList.clear();
        String text = textParam.getText();
        if (text.isEmpty()) {
            dataList.add(new VideoInfo("Warning", "输入栏请不要为空哦."));
//            textArea.setText("输入栏请不要为空哦.");
            return;
        }

        // init urlUtil
        UrlUtil.proxy = isProxy.isSelected();
        UrlUtil.proxyHost = StringUtils.isEmpty(proxyHostText.getText()) ? defaultHost : proxyHostText.getText();
        UrlUtil.proxyPort = Integer
                .parseInt(StringUtils.isEmpty(proxyPortText.getText()) ? defaultPort : proxyPortText.getText());

        if (siteTypeList.getValue().equals(pornhub)) {
            try {
                dataList.addAll(Pornhub.doSpider(text));
            } catch (Exception e) {
                dataList.add(new VideoInfo("Error", e.getMessage()));
//                textArea.setText(e.getMessage());
            }
        }
    }

}
