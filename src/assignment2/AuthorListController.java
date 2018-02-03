package assignment2;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class AuthorListController implements Initializable {

	private static Logger logger = LogManager.getLogger();
	@FXML private ListView<Author> authorList;
	private BorderPane rootPane;
	private ObservableList<Author> authors;

	public AuthorListController(ObservableList<Author> authors, BorderPane rootPane) {
    	this.authors = authors;
    	this.rootPane = rootPane;
    }
	
    @FXML
    void authorListClicked(MouseEvent event) {
    	if(event.getClickCount() != 2)
    		return;
    	try {
    		Author author = authorList.getSelectionModel().getSelectedItem();
    		if(author != null) {
    			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("AuthorDetailView.fxml"));
    			loader.setController(new AuthorDetailController(author));
    			Parent pane = loader.load();
    			rootPane.setCenter(pane);
    			
    			logger.info("Author \"" + author.getFirstName() + " " + author.getLastName() + "\" double clicked");
    		}
    	} catch (IOException e) {
    		logger.error("ERROR: IO Exception");
    	}
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.authorList.setItems(authors);
	}

}