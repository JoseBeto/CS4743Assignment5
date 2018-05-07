package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import assignment5.Launcher;
import authentication.LoginDialog;
import authentication.LoginException;
import authentication.ABACPolicyAuth;
import authentication.AuthBean;
import authentication.AuthBeanRemote;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import authentication.CryptoStuff;
import model.Author;
import model.Book;

public class AppController implements Initializable {
	private static Logger logger = LogManager.getLogger(Launcher.class);

	public static final int AUTHOR_LIST = 1;
	public static final int AUTHOR_DETAIL = 2;
	
	public static final int BOOK_LIST = 3;
	public static final int BOOK_DETAIL = 4;
	
	public static final int AUDIT_TRAIL = 5;
	
	public static final int AUTHOR_BOOK = 6;
	
	public static final int CREATE_REPORT = 7;
	
	public static final int HOME = 8;
	@FXML private MenuItem loginChoice, logoutChoice, authorList, addAuthor
	, bookList, addBook,  createReport;


	private static AppController myInstance = null;
	private BorderPane rootPane = null;
	private Connection conn;
	

	//private AuthBeanRemote bean;
	//NOTE: InitialContext MUST have same scope as the bean variable
	private InitialContext context;
	
	public int sessionId;
	private AuthBeanRemote auth;
	
	public AppController() {
		//create an authenticator
		//auth = new AuthBean();

		//default to no session
		sessionId = AuthBeanRemote.INVALID_SESSION;
		
		Properties props = new Properties();
		//use the jboss factory for context to lookup the EJB remote methods 
		props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
		//URL is the jboss server; port 8080 is jboss default for remote corba access 
		props.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
		//below statement triggers the creation of a EJBClientContext containing a EJBReceiver capable of handling the EJB invocations 		
		props.put("jboss.naming.client.ejb.context", "true");
		try {
			//create and save context as instance var
			context = new InitialContext(props);
			//grab ref to bean’s remote interface
			auth = (AuthBeanRemote) context.lookup("AuthenticationEJB/AuthBean!authentication.AuthBeanRemote");
		} catch (NamingException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}

	public void changeView(int viewType, Object arg) throws AppException {
		try {
			MyController controller = null;
			URL fxmlFile = null;
			switch(viewType) {
				case HOME:
					fxmlFile = this.getClass().getResource("/view/AppView.fxml");
				case AUTHOR_LIST:
					fxmlFile = this.getClass().getResource("/view/AuthorListView.fxml");
					controller = new AuthorListController(new AuthorTableGateway(conn), auth, sessionId);
					break;
				case AUTHOR_DETAIL:
					fxmlFile = this.getClass().getResource("/view/AuthorDetailView.fxml");
					controller = new AuthorDetailController((Author) arg, auth, sessionId);
					break;
				case BOOK_LIST:
					fxmlFile = this.getClass().getResource("/view/BookListView.fxml");
					controller = new BookListController(new BookTableGateway(conn), auth, sessionId);
					break;
				case BOOK_DETAIL:
					fxmlFile = this.getClass().getResource("/view/BookDetailView.fxml");
					controller = new BookDetailController((Book) arg, new PublisherTableGateway(conn),
							new BookTableGateway(conn), auth, sessionId );
					break;
				case AUDIT_TRAIL:
					fxmlFile = this.getClass().getResource("/view/AuditTrailView.fxml");
					controller = new AuditTrailController(arg, this);
					break;
				case AUTHOR_BOOK:
					fxmlFile = this.getClass().getResource("/view/AuthorBookView.fxml");
					controller = new AuthorBookController((Book) arg, new AuthorTableGateway(conn), auth, sessionId);
					break;
				case CREATE_REPORT:
					fxmlFile = this.getClass().getResource("/view/CreateReportView.fxml");
					controller = new CreateReportController(new PublisherTableGateway(conn), new BookTableGateway(conn));
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
	void clickMenuLogout() {
		logger.info("Logout menu item clicked");
		//log out without needing to change views
		doLogout();
	}

	private void doLogout() {
		sessionId = AuthBeanRemote.INVALID_SESSION;
		
		//restrict access to GUI controls based on current login session
		updateGUIAccess();
		rootPane.setCenter(null);	
	}

	private void updateGUIAccess() {
		//if logged in, login should be disabled
		if(sessionId == AuthBeanRemote.INVALID_SESSION)
			loginChoice.setDisable(false);
		else
			loginChoice.setDisable(true);

		//if not logged in, logout should be disabled
		if(sessionId == AuthBeanRemote.INVALID_SESSION) {
			logoutChoice.setDisable(true);
		}else
			logoutChoice.setDisable(false);	
		
		//update menu info based on current user's access privileges
		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_AL))
			authorList.setDisable(false);
		else 
			authorList.setDisable(true);
		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_AA))
			addAuthor.setDisable(false);
		else 
			addAuthor.setDisable(true);
		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_BL))
			bookList.setDisable(false);
		else 
			bookList.setDisable(true);
		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_AB))
			addBook.setDisable(false);
		else 
			addBook.setDisable(true);
		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_CR))
			createReport.setDisable(false);
		else 
			createReport.setDisable(true);

		
	}

	@FXML
	void clickMenuLogin(ActionEvent event) {
		logger.info("Login menu item clicked");
		doLogin();
		
	}

	private void doLogin() {
		//display login modal dialog. get login (username) and password
		//key is login, value is pw
		Pair<String, String> creds = LoginDialog.showLoginDialog();
		if(creds == null) //canceled
			return;

		String userName = creds.getKey();
		String pw = creds.getValue();

		logger.info("userName is " + userName + ", password is " + pw);

		//hash password
		String pwHash = CryptoStuff.sha256(pw);
		

		logger.info("sha256 hash of password is " + pwHash);

		//send login and hashed pw to authenticator
		try {
			//if get session id back, then replace current session
			sessionId = auth.loginSha256(userName, pwHash);

			logger.info("session id is " + sessionId);

		} catch (LoginException e) {
			//else display login failure
			Alert alert = new Alert(AlertType.WARNING);
			alert.getButtonTypes().clear();
			ButtonType buttonTypeOne = new ButtonType("OK");
			alert.getButtonTypes().setAll(buttonTypeOne);
			alert.setTitle("Login Failed");
			alert.setHeaderText("The user name and password you provided do not match stored credentials.");
			alert.showAndWait();

			return;
		}

		//restrict access to GUI controls based on current login session
		updateGUIAccess();
		rootPane.setCenter(null);

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
    void clickMenuCreateReport(ActionEvent event) {
		logger.info("Create Report menu item clicked");
		changeView(CREATE_REPORT, null);
    }
	
	@FXML
    void clickMenuQuit(ActionEvent event) {
		logger.info("Quit menu item clicked");
		Platform.exit();
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//restrict access to the GUI based on current session id (should be invalid session)
		updateGUIAccess();
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