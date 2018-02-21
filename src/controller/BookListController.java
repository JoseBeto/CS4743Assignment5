package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.TextField;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import database.BookTableGateway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Book;

public class BookListController implements Initializable, MyController {

	private static Logger logger = LogManager.getLogger();
	@FXML private ListView<Book> bookList;
	@FXML private TextField searchBook;
	private ObservableList<Book> books;
	private BookTableGateway gateway;

	public BookListController(BookTableGateway gateway) {
    	this.gateway = gateway;
    	books = this.gateway.getBooks();
    }
	
	public BookListController(ObservableList<Book> books) {
    	this.books = books;
    }
	
    @FXML
    void bookListClicked(MouseEvent event) {
    	if(event.getClickCount() > 1) {
    		Book book = bookList.getSelectionModel().getSelectedItem();
   			if(book != null) {
    			AppController.getInstance().changeView(AppController.BOOK_DETAIL, book);
        		logger.info(book.getTitle() + " clicked");
    		}
    	}
    }
	
	@FXML void handleDeleteButton(ActionEvent event) {
		Book book = bookList.getSelectionModel().getSelectedItem();
		if(book != null) {
			String title = "Warning";
			String message = "Are you sure you want to delete book titled " + book.getTitle() + " ?";
			int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
			if(reply == JOptionPane.YES_OPTION){
				book.delete();
				logger.info(book.getTitle() + " deleted");
				books.remove(book);
			}
		}
	}
	
	@FXML
    void handleSearchBook(ActionEvent event) {
		System.out.println("Searching for " + searchBook.getText());
		ObservableList<Book> books = gateway.getBooks(searchBook.getText());
		this.bookList.setItems(books);
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.bookList.setItems(books);
	}

}