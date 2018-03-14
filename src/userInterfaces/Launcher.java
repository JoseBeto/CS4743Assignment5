package userInterfaces;

import java.net.URL;
import java.sql.Connection;
import controller.AppController;
import database.AppException;
import database.ConnectionFactory;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Launcher extends Application {
	private Connection conn;

	@Override
	public void start(Stage primaryStage) throws Exception {
		AppController controller = AppController.getInstance();
		controller.setConnection(conn);
		
		URL fxmlFile = this.getClass().getResource("/view/AppView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		Parent root = loader.load();
		controller.setRootPane((BorderPane) root);
		
		Scene scene = new Scene(root, 610, 400);
	    
		primaryStage.setTitle("userInterfaces");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		controller.changeView(controller.LIST, null);
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		try {
			conn = ConnectionFactory.createConnection();
		} catch(AppException e) {
			Platform.exit();
		}
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		conn.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
