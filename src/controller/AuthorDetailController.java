package controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.Author;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AuthorDetailController implements Initializable {

	private static Logger logger = LogManager.getLogger();

	@FXML private Button saveAuthor;
	@FXML private TextField firstName;
	@FXML private TextField lastName;
	@FXML private TextField doB;
	@FXML private TextField website;
	@FXML private TextField gender;
	private Author author;

	public AuthorDetailController(Author author) {
		this.author = author;
    }
	
	@FXML
	void handleSaveButton(ActionEvent event) {
		logger.info("Save button clicked");
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		firstName.setText(author.getFirstName());
		lastName.setText(author.getLastName());
		
		SimpleDateFormat ft = new SimpleDateFormat("MM/dd/y");
		doB.setText(ft.format(author.getDoB()));
		
		website.setText(author.getWebsite());
		gender.setText(author.getGender());
	}

}
