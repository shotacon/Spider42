package top.shotacon.application.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

@SuppressWarnings("restriction")
public class MessageUtil {

    private static ExecutorService taskThread;

    private static Object obj = new Object();

    /**
     * 标签提示
     * 
     * @param time    毫秒, 在n秒后清空
     * @param message 提示信息
     * @param label   标签类
     */
	public static void showTimeLabel(long time, String message, Label label) {
        label.setText(message);
        doTask(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    try {
                        obj.wait(time);
                        Platform.runLater(() -> label.setText(""));
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 提示框
     * 
     * @param time    毫秒
     * @param message 提示信息
     */
    public static void showTimedDialog(long time, String message) {

        Stage popup = new Stage();
        popup.setAlwaysOnTop(true);
        popup.initModality(Modality.APPLICATION_MODAL);
        Button closeBtn = new Button("知道了");
        closeBtn.setOnAction(e -> {
            popup.close();
        });
        VBox root = new VBox();
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.BASELINE_CENTER);
        root.setSpacing(20);
        root.getChildren().addAll(new Label(message), closeBtn);
        Scene scene = new Scene(root);
        popup.setScene(scene);
        popup.setTitle("提示信息");
        popup.show();

        doTask(new Runnable() {
            @Override
            public void run() {
                synchronized (obj) {
                    try {
                        obj.wait(time);
                        if (popup.isShowing()) {
                            Platform.runLater(() -> popup.close());
                        }
                    } catch (Exception exp) {
                        exp.printStackTrace();
                    }
                }
            }
        });
    }

    private static void doTask(Runnable runTask) {
        taskThread = Executors.newSingleThreadExecutor();
        taskThread.execute(runTask);
        taskThread.shutdown();
    }
}
