package assignment1;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/* CS 4743 Assignment 1 by Jose Bocanegra */

public class Launcher extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//generate a scenegraph from the fxml file
		//note that the entire view will be laid out in the fxml file
		Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
		//build a scene with the scenegraph as its root node
		Scene scene = new Scene(root);

		primaryStage.setTitle("assignment1");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
