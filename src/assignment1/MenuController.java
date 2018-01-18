package assignment1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;

public class MenuController implements Initializable {

	@FXML private MenuBar menuBar;
	@FXML private MenuItem menuItemAuthorList;
	@FXML private MenuItem menuItemQuit;
	@FXML private BorderPane rootPane;
	
	@FXML private void handleMenuAction(ActionEvent event) throws IOException {
		//provideAboutFunctionality();
		if(event.getSource() == menuItemAuthorList) {
			Parent pane = FXMLLoader.load(getClass().getResource("AuthorListView.fxml"));
			rootPane.setCenter(pane);
		} else if(event.getSource() == menuItemQuit) {
			//probably need our own method for shutting down, to close DB connections, etc.
			System.exit(0);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		menuBar.setFocusTraversable(true);
	}

}
