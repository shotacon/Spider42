package top.shotacon.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import top.shotacon.application.utils.MessageUtil;
import top.shotacon.application.utils.ThreadUtil;

public class MainApp extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("MainScene.fxml"));
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("What's up");
			primaryStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/spider.png")));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stop() throws Exception {
		MessageUtil.shutdown();
		ThreadUtil.executorService.shutdown();
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
