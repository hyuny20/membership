package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	public static String global_user_id = null;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
			Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
			primaryStage.setTitle("로그인 모듈");
			primaryStage.getIcons().add(new Image("application/instagram.png"));
			primaryStage.setScene(new Scene(root, 600, 400));
			primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
