package controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.converter.LocalDateStringConverter;
import model.Author;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import assignment2.AlertHelper;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AuthorDetailController implements Initializable, MyController {

	private static Logger logger = LogManager.getLogger();

	@FXML private Button saveAuthor;
	@FXML private Button deleteAuthor; 
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
		logger.info("Model's name is " + author.getFirstName());
    	
    	if(!author.isValidName(author.getFirstName())) {
    		logger.error("Invalid first name: " + author.getFirstName());
    		
    		AlertHelper.showWarningMessage("Oops!", "First name is invalid", "Names cannot be "
    				+ "blank and must be no more than 100 characters.");
    		return;
    	} else if(!author.isValidName(author.getLastName())) {
    		logger.error("Invalid last name: " + author.getLastName());
    		
    		AlertHelper.showWarningMessage("Oops!", "Last name is invalid", "Names cannot be "
    				+ "blank and must be no more than 100 characters.");
    		return;
    	} else if(!author.isValidDate(author.getDoB())) {
    		logger.error("Invalid date: " + author.getDoB());
    		
    		AlertHelper.showWarningMessage("Oops!", "Date is invalid", "Date of birth must be "
    				+ "a date before the current system date");
    		return;
    	} else if(!author.isValidGender(author.getGender())) {
    		logger.error("Invalid gender: " + author.getGender());
    		
    		AlertHelper.showWarningMessage("Oops!", "Gender is invalid", "Gender must be Male, Female, or Unknown");
    		return;
    	} else if(!author.isValidWebsite(author.getWebsite())) {
    		logger.error("Invalid website: " + author.getWebsite());
    		
    		AlertHelper.showWarningMessage("Oops!", "Website is invalid", "Websites must be no more than 100 characters");
    		return;
    	}
    	author.save();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		firstName.textProperty().bindBidirectional(author.firstNameProperty());
		lastName.textProperty().bindBidirectional(author.lastNameProperty());
		
		Bindings.bindBidirectional( doB.textProperty(), author.dateOfBirthProperty(), new LocalDateStringConverter()
	    {
			@Override
			public String toString(LocalDate object) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
				return object == null ? "" : object.format(dtf);
			}

	        @Override
	        public LocalDate fromString( String string )
	        {
	        	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	        	LocalDate date;
	        	try {
	        		date = LocalDate.parse(string, dtf);
	        	}
	        	catch(DateTimeParseException e) {
	        		date = null;
	        	}
	        	return date;
	        }
	    } );
		
		website.textProperty().bindBidirectional(author.websiteProperty());
		gender.textProperty().bindBidirectional(author.genderProperty());
	}
}
