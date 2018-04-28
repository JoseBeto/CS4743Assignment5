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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Book;

public class BookListController implements Initializable, MyController {

	private static Logger logger = LogManager.getLogger();
	@FXML private ListView<Book> bookList;
	@FXML private TextField searchBook;
	@FXML private Label fetchedLabel;
	private ObservableList<Book> books;
	private BookTableGateway gateway;
	private int page = 1;
	private int totalCount;

	public BookListController(BookTableGateway gateway) {
    	this.gateway = gateway;
    }
	
	public BookListController(ObservableList<Book> books) {
    	this.books = books;
    }
	
	public int getOffset() {
		return (page - 1) * 50;
	}
	
	void updateLabel() {
		this.totalCount = gateway.getTotalCount(searchBook.getText());
		int numberOfBooks = (50 * (page - 1)) + books.size();
		fetchedLabel.setText("Fetched records " + (getOffset() + 1) 
				+ " to " + numberOfBooks + " out of " + totalCount);
	}
	
	void updateBooks() {
		books = this.gateway.getBooks(searchBook.getText(), getOffset());
		this.bookList.setItems(books);
		updateLabel();
		bookList.scrollTo(0);
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
    void firstButtonClicked(ActionEvent event) {
		if(page != 1) {
			page = 1;
			updateBooks();
		}
    }
	
	@FXML
    void lastButtonClicked(ActionEvent event) {
		if(page != ((totalCount / 50) + 1)) {
			page = (totalCount / 50) + 1;
			updateBooks();
		}
    }

    @FXML
    void nextButtonClicked(ActionEvent event) {
    	if((page * 50) < totalCount) {
    		page++;
    		updateBooks();
    	}
    }

    @FXML
    void prevButtonClicked(ActionEvent event) {
    	if(page > 1) {
    		page--;
    		updateBooks();
    	}
    }
	
	@FXML
    void handleSearchBook(ActionEvent event) {
		page = 1;
		updateBooks();
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
    	books = this.gateway.getBooks(searchBook.getText(), getOffset());
		this.bookList.setItems(books);
		updateLabel();
	}
}