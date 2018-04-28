package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import assignment5.AlertHelper;
import database.BookTableGateway;
import database.PublisherTableGateway;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import model.Book;
import model.Publisher;
import report.ExcelReport;

public class CreateReportController implements MyController, Initializable {

	private static Logger logger = LogManager.getLogger();
	@FXML private ComboBox<Publisher> publisher;
    @FXML private Label pathLabel;
    private PublisherTableGateway pGateway;
    private BookTableGateway bGateway;

    public CreateReportController(PublisherTableGateway pGateway, BookTableGateway bGateway) {
    	this.pGateway = pGateway;
    	this.bGateway = bGateway;
    }
    
    @FXML
    void saveToPathClicked(ActionEvent event) {
    	while(true){ //Escapes by file existing or user selecting no/close option in message box/filechooser
			JFileChooser fileChooser = new JFileChooser(".xls"); 
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
			FileFilter excelFilter = new FileNameExtensionFilter(
				    "Excel files", "xls", "excel");
			fileChooser.addChoosableFileFilter(excelFilter);
		    fileChooser.setAcceptAllFileFilterUsed(false);
			
		    int ret = fileChooser.showSaveDialog(null);
			
			if (ret == JFileChooser.CANCEL_OPTION)
				return;
			
			File file = fileChooser.getSelectedFile();
			if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".xls")){
				file = new File(fileChooser.getSelectedFile() + ".xls");
			}
			pathLabel.setText(file.getAbsolutePath());
			return;
		}
    }

    @FXML
    void handleGenerateReport(ActionEvent event) {
    	if(publisher.getSelectionModel().getSelectedItem() == null) {
    		logger.error("No publisher selected");
    		AlertHelper.showWarningMessage("Oops!", "No publisher selected", "You must select a publisher"
    				+ " to generate a report!");
    		return;
    	} else if(pathLabel.getText().equals("")) {
    		logger.error("No save path selected");
    		AlertHelper.showWarningMessage("Oops!", "No save path selected", "You must specify a path"
    				+ " to generate a report!");
    		return;
    	}
    	logger.info("Generating report...");
    	
    	ObservableList<Book> books = bGateway.getBooksByPublisher(publisher.getSelectionModel().getSelectedItem().getId());
    	
    	new ExcelReport(pathLabel.getText(), books, bGateway, publisher.getSelectionModel().getSelectedItem().getName());
    	
    	AlertHelper.showWarningMessage("Success!", "Report generated!", "");
    	logger.info("Report generated");
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		publisher.setItems(pGateway.getPublishers());
	}
}
