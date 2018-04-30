package controller;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.NumberStringConverter;
import model.AuthorBook;
import model.Book;
import model.Publisher;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import assignment5.AlertHelper;
import authentication.ABACPolicyAuth;
import authentication.AuthenticatorLocal;
import database.BookTableGateway;
import database.PublisherTableGateway;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class BookDetailController implements Initializable, MyController {

	private static Logger logger = LogManager.getLogger();

	@FXML private Button savePublisher;
    @FXML private TextField title;
    @FXML private TextArea summary;
    @FXML private TextField yearPublished;
    @FXML private TextField isbn;
    @FXML private ComboBox<Publisher> publisher;
    @FXML private TableView<AuthorBook> authorRoyaltyList;
    @FXML private TableColumn<AuthorBook, String> authorList;
    @FXML private TableColumn<AuthorBook, String> royaltyList;
    @FXML private Button deleteAuthorBook;
   	@FXML private Button addAuthorBook;
   	@FXML private Button saveAuthorBook;
    
	private Book book;
	private PublisherTableGateway pubGateway;
	AuthenticatorLocal auth;
	int sessionId;

	public BookDetailController(Book book, PublisherTableGateway pubGateway, BookTableGateway gateway, AuthenticatorLocal auth, int sessionId) {
		this.book = book;
		this.pubGateway = pubGateway;
		this.auth = auth;
		this.sessionId = sessionId;
    }
	
	void updateGUIAccess() {
		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_ABD))
			deleteAuthorBook.setDisable(false);
		else 
			deleteAuthorBook.setDisable(true);
		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_ABA))
			addAuthorBook.setDisable(false);
		else 
			addAuthorBook.setDisable(true);
		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_ABS))
			saveAuthorBook.setDisable(false);
		else 
			saveAuthorBook.setDisable(true);
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
    		
    		AlertHelper.showWarningMessage("Oops!", "Isbn is invalid", "Isbn cannot be greater "
    				+ "than 13 characters");
    		return;
    	} else if (!book.hasAuthors(book.getAuthors())){
    		logger.error("No authors entered");
    		
    		AlertHelper.showWarningMessage("Oops!", "No authors added", "A book must have "
    				+ "at least 1 author");
    		return;
    	}
    	
    	book.save();
	}
	
	@FXML
	void handleAuditTrailButton(ActionEvent event) {
		//Prevent books not saved into database to access audit trail
		if(book.getId() == 0) {
			logger.error("Book needs to be saved into database before accessing audit trail");
			AlertHelper.showWarningMessage("Oops!", "Book doesn't exist in database", "Book needs to be saved "
					+ "into database before accessing audit trail");
			return;
		}

		logger.info("Audit trail for " + book.getTitle() + " accessed.");
		AppController.getInstance().changeView(AppController.AUDIT_TRAIL, book);
	}
	
	@FXML
    void addAuthorClicked(ActionEvent event) {
		AppController.getInstance().changeView(AppController.AUTHOR_BOOK, book);
    }

    @FXML
    void deleteAuthorClicked(ActionEvent event) {
    	authorRoyaltyList.getItems().remove(authorRoyaltyList.getSelectionModel().getSelectedItem());
    }

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		title.textProperty().bindBidirectional(book.titleProperty());
		summary.textProperty().bindBidirectional(book.summaryProperty());
		royaltyList.setEditable(true);
		
		//Prevents user from entering a non digit
		yearPublished.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	yearPublished.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
		
		Bindings.bindBidirectional(yearPublished.textProperty(), book.yearPublishedProperty(), new NumberStringConverter()
	    {
			@Override
			public String toString(Number value) {
				if(value.equals(0))
					return "";
				return value.toString();
			}

	        @Override
	        public Integer fromString(String string) {
	        	if(string.equals(""))
	        		return null;
	        	return Integer.parseInt(string);
	        }
	    });
		publisher.valueProperty().bindBidirectional(book.publisherProperty());
		publisher.setItems(pubGateway.getPublishers());
		isbn.textProperty().bindBidirectional(book.isbnProperty());
		
		authorList.setCellValueFactory(new PropertyValueFactory<>("author"));
		royaltyList.setCellValueFactory(new PropertyValueFactory<>("royaltyPercent"));
		
		royaltyList.setCellFactory(TextFieldTableCell.forTableColumn());

    	royaltyList.setOnEditCommit(
    	    new EventHandler<CellEditEvent<AuthorBook, String>>() {
    	    	public void handle(CellEditEvent<AuthorBook, String> t) {
    	    		double x = 0.0;
    	    		try {
    	    			x = Double.parseDouble(t.getNewValue().toString().substring(0, t.getNewValue().length()));
    	    		} catch (NumberFormatException e) {
    	    			logger.error("Invalid Royalty: " + (t.getNewValue().toString().substring(0, t.getNewValue().length())));
    	    			AlertHelper.showWarningMessage("Oops!", "Royalty is invalid", "Royalty must not "
    	        				+ "contain any characters or symbols.");
    	    		}
    	    		
    	            x /= 100;
    	    		((AuthorBook) t.getTableView().getItems().get(
    	                t.getTablePosition().getRow())
    	                ).setRoyalty(new BigDecimal(x));
    	        }
    	    }
    	);
		
		authorRoyaltyList.setItems(book.getAuthors());
		updateGUIAccess();
	}
}
