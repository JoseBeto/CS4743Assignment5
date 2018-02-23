package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import model.AuditTrailEntry;
import model.Book;

public class AuditTrailController implements Initializable, MyController {

	@FXML private Label auditTrailTitle;
    @FXML private ListView<AuditTrailEntry> auditTrailList;
    
    private Book book;
    private AppController controller;
    
    public AuditTrailController(Book book, AppController controller) {
    	this.book = book;
    	this.controller = controller;
    	
    	//auditTrailTitle.setText("Audit Trail for " + book.getTitle());
    }
    
    @FXML
    void handleBackButton(ActionEvent event) {
    	AppController controller = new AppController().getInstance();
    	controller.changeView(controller.BOOK_DETAIL, book);
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		auditTrailTitle.setText("Audit Trail for " + book.getTitle());
		
		auditTrailList.setItems(book.getAuditTrail());
	}
}
