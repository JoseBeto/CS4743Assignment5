package controller;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import model.Author;

public class MenuController implements Initializable {

	@FXML private MenuBar menuBar;
	@FXML private MenuItem menuItemAuthorList;
	@FXML private MenuItem menuItemQuit;
	@FXML private BorderPane rootPane;
	private ObservableList<Author> authors;
	private static Logger logger = LogManager.getLogger();
	
	@FXML private void handleMenuAction(ActionEvent event) throws IOException {
		if(event.getSource() == menuItemAuthorList) {
			createAuthors();
			
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/AuthorListView.fxml"));
			loader.setController(new AuthorListController(authors, rootPane));
			Parent pane = loader.load();
			rootPane.setCenter(pane);
		} else if(event.getSource() == menuItemQuit) {
			System.exit(0);
		}
	}
	
	public void createAuthors() {
		authors = FXCollections.observableArrayList();
		//DateFormat dateFormat = new SimpleDateFormat("MM/dd/y");
		String fredDob = "13-Dec-1978";
		String wendyDob = "27-Jul-1985";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		LocalDate fredDate = LocalDate.parse(fredDob, formatter);
		authors.add(new Author("Fred", "Carrier", fredDate, "Male"));
		authors.get(0).setWebsite("www.google.com");
		
		LocalDate wendyDate = LocalDate.parse(wendyDob, formatter);
		authors.add(new Author("Wendy", "Lehner", wendyDate, "Female"));
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		menuBar.setFocusTraversable(true);
	}
}
