package controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

import database.AuthorTableGateway;
import database.BookTableGateway;
import javafx.collections.FXCollections;
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

	private BookTableGateway gateway;
	private AuthorTableGateway authorGateway;
	private Book book;

	public AuthorBookController(BookTableGateway gateway, AuthorTableGateway authorGateway, Book book) {
		this.gateway = gateway;
		this.authorGateway = authorGateway;
		this.book = book;
	}
	
	@FXML
    void handleSaveButton(ActionEvent event) {
		AuthorBook authorBook = new AuthorBook(authorBox.getSelectionModel().getSelectedItem(),
				book, BigDecimal.valueOf(Double.valueOf(royalty.getText())));
		gateway.addAuthorBook(authorBook);
		
		AppController.getInstance().changeView(AppController.BOOK_DETAIL, book);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Author> existingAuthors = FXCollections.observableArrayList();
		ObservableList<Author> authors = authorGateway.getAuthors();
		
		for(AuthorBook authorBook : gateway.getAuthorsForBook(book)) {
			existingAuthors.add(authorBook.getAuthor());
		}
		Boolean flag = true;
		for(Author author : authors) {
			for(Author author2 : existingAuthors) {
				if(author.getId() == author2.getId())
					flag = false;
			}
			if(flag)
				authorBox.getItems().add(author);
			flag = true;
		}
	}
}
