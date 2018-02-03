package assignment2;

import java.net.URL;
import java.sql.Connection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import controller.AppController;
import database.AppException;
import database.ConnectionFactory;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/* CS 4743 Assignment 2 by Jose Bocanegra and David Peek */

public class Launcher extends Application {

	private static Logger logger = LogManager.getLogger();
	private Connection conn;

	@Override
	public void start(Stage primaryStage) throws Exception {
		AppController controller = AppController.getInstance();
		controller.setConnection(conn);
		
		//TODO: move this to a singleton switchView method OR a view builder
		URL fxmlFile = this.getClass().getResource("/view/AppView.fxml");
		FXMLLoader loader = new FXMLLoader(fxmlFile);
		
		loader.setController(controller);
		
		Parent root = loader.load();
		controller.setRootPane((BorderPane) root);
		
		Scene scene = new Scene(root, 610, 400);
	    
		primaryStage.setTitle("assignment1");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@Override
	public void init() throws Exception {
		super.init();
		
		logger.info("Creating connection...");
		
		try {
			conn = ConnectionFactory.createConnection();
		} catch(AppException e) {
			logger.fatal("Cannot connect to db");
			Platform.exit();
		}
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		
		//TODO: find out how to attach to shutdown hook
		logger.info("Closing connection...");
		
		conn.close();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
