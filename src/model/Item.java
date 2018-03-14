package model;

import database.ItemTableGateway;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class Item {

	private int id;
	private SimpleStringProperty name;
	private SimpleDoubleProperty price;
	private SimpleStringProperty image;
	private SimpleStringProperty desc;
	
	private ItemTableGateway gateway;
	
	public Item(String name, Double price, String desc, String image) {
		this.name = new SimpleStringProperty();
		this.price = new SimpleDoubleProperty();
		this.image = new SimpleStringProperty();
		this.desc = new SimpleStringProperty();
		
		setName(name);
		setPrice(price);
		setImage(image);
		setDesc(desc);
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public Double getPrice() {
		return price.get();
	}

	public void setPrice(double price) {
		this.price.set(price);
	}
	
	public Image getImage() {
		return (new Image(image.get()));
	}

	public void setImage(String image) {
		this.image.set(image);
	}
	
	public String getDesc() {
		return desc.get();
	}

	public void setDesc(String desc) {
		this.desc.set(desc);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public ItemTableGateway getGateway() {
		return gateway;
	}

	public void setGateway(ItemTableGateway gateway) {
		this.gateway = gateway;
	}
	
	public SimpleStringProperty nameProperty() {
		return name;
	}
	public SimpleDoubleProperty priceProperty() {
		return price;
	}

	@Override
	public String toString() {
		return name.get();
	}
}
