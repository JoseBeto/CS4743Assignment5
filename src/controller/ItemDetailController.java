package controller;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.Item;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ItemDetailController implements Initializable, MyController {
	@FXML private Label nameLabel;
    @FXML private Label priceLabel;
    @FXML private ImageView itemImage;
    @FXML private Label descLabel;
    
	private Item item;

	public ItemDetailController(Item item) {
		this.item = item;
    }
	
	@FXML
    void backButtonClicked(MouseEvent event) {
		AppController.getInstance().changeView(AppController.LIST, null);
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameLabel.setText(item.getName());
		priceLabel.setText("$" + item.getPrice());
		itemImage.setImage(item.getImage());
		descLabel.setText(item.getDesc());
	}
}
