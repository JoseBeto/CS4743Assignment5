package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import assignment3.Launcher;
import database.AppException;
import database.AuthorTableGateway;
import database.BookTableGateway;
import database.PublisherTableGateway;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import model.Author;
import model.Book;

public class AppController implements Initializable {
	private static Logger logger = LogManager.getLogger(Launcher.class);

	public static final int AUTHOR_LIST = 1;
	public static final int AUTHOR_DETAIL = 2;
	
	public static final int BOOK_LIST = 3;
	public static final int BOOK_DETAIL = 4;
	
	public static final int AUDIT_TRAIL = 5;

	private static AppController myInstance = null;
	private BorderPane rootPane = null;
	private Connection conn;
	
	public AppController() {
		//TODO: instantiate models
	}
	
	public void changeView(int viewType, Object arg) throws AppException {
		try {
			MyController controller = null;
			URL fxmlFile = null;
			switch(viewType) {
				case AUTHOR_LIST:
					fxmlFile = this.getClass().getResource("/view/AuthorListView.fxml");
					controller = new AuthorListController(new AuthorTableGateway(conn));
					break;
				case AUTHOR_DETAIL:
					fxmlFile = this.getClass().getResource("/view/AuthorDetailView.fxml");
					controller = new AuthorDetailController((Author) arg);
					break;
				case BOOK_LIST:
					fxmlFile = this.getClass().getResource("/view/BookListView.fxml");
					controller = new BookListController(new BookTableGateway(conn));
					break;
				case BOOK_DETAIL:
					fxmlFile = this.getClass().getResource("/view/BookDetailView.fxml");
					controller = new BookDetailController((Book) arg, new PublisherTableGateway(conn));
					break;
				case AUDIT_TRAIL:
					fxmlFile = this.getClass().getResource("/view/AuditTrailView.fxml");
					controller = new AuditTrailController((Book) arg, this);
					break;
			}
		
			FXMLLoader loader = new FXMLLoader(fxmlFile);
			loader.setController(controller);
		
			Parent viewNode = loader.load();
			rootPane.setCenter(viewNode);
		} catch (IOException e) {
			throw new AppException(e);
		}
	}

	@FXML
    void clickMenuAuthorList(ActionEvent event) {
		logger.info("Author list menu item clicked");
		changeView(AUTHOR_LIST, null);
    }
	
	@FXML
    void clickMenuAddAuthor(ActionEvent event) {
		logger.info("Add Author menu item clicked");
		Author author = new Author();
		author.setGateway(new AuthorTableGateway(conn));
		changeView(AUTHOR_DETAIL, author);
    }
	
	@FXML
    void clickMenuBookList(ActionEvent event) {
		logger.info("Book list menu item clicked");
		changeView(BOOK_LIST, null);
    }
	
	@FXML
    void clickMenuAddBook(ActionEvent event) {
		logger.info("Add Book menu item clicked");
		Book book = new Book(new PublisherTableGateway(conn));
		book.setGateway(new BookTableGateway(conn));
		changeView(BOOK_DETAIL, book);
    }
	
	@FXML
    void clickMenuQuit(ActionEvent event) {
		logger.info("Quit menu item clicked");
		Platform.exit();
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
	
	public static AppController getInstance() {
		if(myInstance == null)
			myInstance = new AppController();
		return myInstance;
	}
	
	public BorderPane getRootPane() {
		return rootPane;
	}

	public void setRootPane(BorderPane rootPane) {
		this.rootPane = rootPane;
	}

	public Connection getConnection() {
		return conn;
	}

	public void setConnection(Connection conn) {
		this.conn = conn;
	}
	
}