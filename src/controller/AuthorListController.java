package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import assignment5.AlertHelper;
import authentication.ABACPolicyAuth;
import authentication.AuthBeanRemote;
import database.AuthorTableGateway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Author;

public class AuthorListController implements Initializable, MyController {

	private static Logger logger = LogManager.getLogger();
	@FXML private ListView<Author> authorList;
	@FXML private Button deleteAuthor;
	private ObservableList<Author> authors;
	private AuthorTableGateway gateway;

	AuthBeanRemote auth;
	int sessionId;

	public AuthorListController(AuthorTableGateway gateway, AuthBeanRemote auth, int sessionId) {
		this.gateway = gateway;
		authors = this.gateway.getAuthors();
		this.auth = auth;
		//default to no session
		this.sessionId = sessionId;
	}

	public AuthorListController(ObservableList<Author> authors) {
		this.authors = authors;

	}

	void updateGUIAccess() {

		if(auth.hasAccess(sessionId, ABACPolicyAuth.CAN_ACCESS_CHOICE_AD))
			deleteAuthor.setDisable(false);
		else 
			deleteAuthor.setDisable(true);
		

	}


	@FXML
	void authorListClicked(MouseEvent event) {
		if(event.getClickCount() > 1) {
			Author author = authorList.getSelectionModel().getSelectedItem();
			if(author != null) {
				AppController.getInstance().changeView(AppController.AUTHOR_DETAIL, author);
				logger.info(author.getFirstName() + " clicked");
			}
		}
	}

	@FXML void handleDeleteButton(ActionEvent event) {
		Author author = authorList.getSelectionModel().getSelectedItem();
		if(author != null) {
			String title = "Warning";
			String message = "Are you sure you want to delete author named " + author.getFirstName() + " ?";
			int reply = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION);
			if(reply == JOptionPane.YES_OPTION){
				if(author.delete()) { //Author deleted successfully
					logger.info(author.getFirstName() + " deleted");
					authors.remove(author);
				} else { //Author cannot be deleted since it is associated with a book
					AlertHelper.showWarningMessage("Oops!", "Cannot delete author!", "This author "
							+ "cannot be deleted because it is associated with a book!");
				}

			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.authorList.setItems(authors);
		updateGUIAccess();

	}

}