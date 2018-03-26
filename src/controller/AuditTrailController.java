package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.AuditTrailEntry;
import model.Author;
import model.Book;

public class AuditTrailController implements Initializable, MyController {

	@FXML private Label auditTrailTitle;
    @FXML private ListView<AuditTrailEntry> auditTrailList;
    
    private Book book;
    private Author author;
    private AppController controller;
    
    public AuditTrailController(Object arg, AppController controller) {
    	if(arg.getClass().getTypeName().equals("model.Book"))
    		this.book = (Book) arg;
    	else if(arg.getClass().getTypeName().equals("model.Author"))
    		this.author = (Author) arg;
    	
    	this.controller = controller;
    }
    
    @FXML
    void handleBackButton(ActionEvent event) {
    	if(book != null)
    		controller.changeView(AppController.BOOK_DETAIL, book);
    	else if(author != null)
    		controller.changeView(AppController.AUTHOR_DETAIL, author);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(book != null) {
			auditTrailTitle.setText("Audit Trail for " + book.getTitle());
			auditTrailList.setItems(book.getAuditTrail());
		}
    	else if(author != null) {
    		auditTrailTitle.setText("Audit Trail for " + author.toString());
    		auditTrailList.setItems(author.getAuditTrail());
    	}
	}
}
