package assignment5;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public class AlertHelper {
	
	public static void showWarningMessage(String title, String header, String content) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	public static boolean showDecisionMessage(String title, String header) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.getButtonTypes().remove(0);
		alert.getButtonTypes().add(ButtonType.YES);
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES){
		    return true;
		} else {
		    // ... user chose CANCEL or closed the dialog
			return false;
		}
	}
}
