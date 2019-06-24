package top.shotacon.application;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.concurrent.Task;
import top.shotacon.application.enums.TipType;
import top.shotacon.application.model.VideoInfo;
import top.shotacon.application.spider.Pornhub;
import top.shotacon.application.utils.MessageUtil;
import top.shotacon.application.utils.ThreadUtil;
import top.shotacon.application.utils.UrlUtil;
import top.shotacon.application.utils.ValidatorUtil;

@SuppressWarnings("restriction")
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
	private Label notify;

	@FXML
	private CheckBox isProxy;

	@FXML
	private TextField proxyPortText;

	@FXML
	private TextField proxyHostText;

	private ObservableList<VideoInfo> dataList = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<String> list = Arrays.asList(youtube, pornhub);
		siteTypeList.getItems().addAll(list);
		siteTypeList.setPromptText("Site Type");

		// 获取系统剪贴板
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

		tvColumn.setCellValueFactory(cellData -> cellData.getValue().getColumn());
		tvValue.setCellValueFactory(cellData -> cellData.getValue().getValue());
//        tvValue.setCellFactory(TextFieldTableCell.forTableColumn());
		tableView.setItems(dataList);
//        tableView.setEditable(true);
		tableView.setRowFactory(tv -> {
			TableRow<VideoInfo> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					VideoInfo rowData = row.getItem();
					// 封装文本内容
					Transferable trans = new StringSelection(rowData.getValue().get());
					// 把文本内容设置到系统剪贴板
					clipboard.setContents(trans, null);
					MessageUtil.showTimeLabel(5000, "已复制到剪贴板", notify);
				}
			});
			return row;
		});
	}

	public void onClickButtonClick(ActionEvent event) {
		reSet();
		String text = textParam.getText();
		if (text.isEmpty()) {
			dataList.add(new VideoInfo(TipType.WARNING.getName(), "输入栏请不要为空哦."));
			return;
		}
		if (!ValidatorUtil.isUrl(text)) {
			dataList.add(new VideoInfo(TipType.WARNING.getName(), "请输入有效的地址链接."));
			return;
		}

		// init urlUtil
		UrlUtil.proxy = isProxy.isSelected();
		UrlUtil.proxyHost = StringUtils.isEmpty(proxyHostText.getText()) ? defaultHost : proxyHostText.getText();
		UrlUtil.proxyPort = Integer
				.parseInt(StringUtils.isEmpty(proxyPortText.getText()) ? defaultPort : proxyPortText.getText());

		if (siteTypeList.getValue().equals(pornhub)) {
			try {
				ThreadUtil.executorService.submit(new Task<Boolean>() {
					@Override
					protected Boolean call() throws Exception {
						return dataList.addAll(Pornhub.doSpider(text));
					}
				});
			} catch (Exception e) {
				dataList.add(new VideoInfo(TipType.ERROR.getName(), e.getMessage()));
			}
		} else {
			dataList.add(new VideoInfo(TipType.WARNING.getName(), "你问我滋不滋磁呀, 那当然是不滋磁."));
		}
	}

	private void reSet() {
		dataList.clear();
		notify.setText("");
	}

	public void onMouseMoved(ActionEvent event) {
		notify.setText("");
	}

}
