package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import assignment5.AlertHelper;
import database.AuthorTableGateway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Author;
import model.AuthorBook;
import model.Book;

public class AuthorBookController implements Initializable, MyController {
	
	@FXML private TextField royalty;
    @FXML private ComboBox<Author> authorBox;

	private Book book;
	private AuthorTableGateway authorGateway;

	public AuthorBookController(Book book, AuthorTableGateway authorGateway) {
		this.book = book;
		this.authorGateway = authorGateway;
	}
	
	@FXML
    void handleSaveButton(ActionEvent event) {
		if(royalty.getText().equals("")) {
			AlertHelper.showWarningMessage("Oops!", "Royalty is invalid", "Royalty must be "
    				+ "between 0.0 and 1.0");
			return;
		}
		
		double x = Double.valueOf(royalty.getText());
		if(x <= 0.0 || x > 1.0){
			AlertHelper.showWarningMessage("Oops!", "Royalty is invalid", "Royalty must be "
    				+ "between 0.0 and 1.0");
			return;
		}
		AuthorBook authorBook = new AuthorBook(authorBox.getSelectionModel().getSelectedItem(),
				book, BigDecimal.valueOf(Double.valueOf(royalty.getText())));
		book.addAuthor(authorBook);
		
		AppController.getInstance().changeView(AppController.BOOK_DETAIL, book);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<AuthorBook> existingAuthors = book.getAuthors();
		ObservableList<Author> authors = authorGateway.getAuthors();
		
		for(Author author : authors) {
			Boolean flag = true;
			for(AuthorBook authorBook : existingAuthors) {
				if(author.getId() == authorBook.getAuthor().getId())
					flag = false;
			}
			if(flag)
				authorBox.getItems().add(author);
		}
	}
}
