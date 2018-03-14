package controller;

import java.net.URL;
import java.util.ResourceBundle;
import database.ItemTableGateway;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import model.Item;

public class ItemListController implements Initializable, MyController {
	@FXML private ListView<Item> itemList;
	private ObservableList<Item> items;
	private ItemTableGateway gateway;

	public ItemListController(ItemTableGateway gateway) {
    	this.gateway = gateway;
    	items = this.gateway.getItems();
    }
	
	public ItemListController(ObservableList<Item> items) {
    	this.items = items;
    }
	
    @FXML
    void ItemClicked(MouseEvent event) {
    	if(event.getClickCount() > 1) {
    		Item item = itemList.getSelectionModel().getSelectedItem();
   			if(item != null)
    			AppController.getInstance().changeView(AppController.ITEM_DETAIL, item);
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.itemList.setItems(items);
	}

}