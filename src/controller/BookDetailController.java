package controller;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import model.Book;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import assignment3.AlertHelper;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class BookDetailController implements Initializable, MyController {

	private static Logger logger = LogManager.getLogger();

	@FXML private Button savePublisher;
    @FXML private TextField title;
    @FXML private TextField summary;
    @FXML private TextField yearPublished;
    @FXML private TextField isbn;
    @FXML private TextField publisherId;
    
	private Book book;

	public BookDetailController(Book book) {
		this.book = book;
    }
	
	@FXML
	void handleSaveButton(ActionEvent event) {
		logger.info("Model's name is " + book.getTitle());
    	
    	if(!book.isValidTitle(book.getTitle())) {
    		logger.error("Invalid Title: " + book.getTitle());
    		
    		AlertHelper.showWarningMessage("Oops!", "Title is invalid", "Titles cannot be "
    				+ "blank and must be no more than 255 characters.");
    		return;
    	} else if(!book.isValidSummary(book.getSummary())) {
    		logger.error("Invalid summary: " + book.getSummary());
    		
    		AlertHelper.showWarningMessage("Oops!", "Summary is invalid", "summaries must be "
    				+ "less than 65536 characters.");
    		return;
    	} else if(!book.isValidYearPublished(book.getYearPublished())) {
    		logger.error("Invalid year published: " + book.getYearPublished());
    		
    		AlertHelper.showWarningMessage("Oops!", "Year published is invalid", "Year published "
    				+ "cannot be greater than current year.");
    		return;
    	} else if(!book.isValidIsbn(book.getIsbn())) {
    		logger.error("Invalid isbn: " + book.getIsbn());
    		
    		AlertHelper.showWarningMessage("Oops!", "isbn is invalid", "isbn cannot be greater "
    				+ "than 13 characters");
    		return;
    	}
    	book.save();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.textProperty().bindBidirectional(book.titleProperty());
		summary.textProperty().bindBidirectional(book.summaryProperty());
		Bindings.bindBidirectional( yearPublished.textProperty(), book.yearPublishedProperty(), new NumberStringConverter()
	    {
			@Override
			public String toString(Number value) {
				return value.toString();
			}

	        @Override
	        public Integer fromString( String string )
	        {
	        	return Integer.parseInt(string);
	        }
	    });
		publisherId.textProperty().bindBidirectional(book.publisherIdProperty(), new NumberStringConverter());
		isbn.textProperty().bindBidirectional(book.isbnProperty());
	}
}
