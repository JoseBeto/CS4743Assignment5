package model;

import database.PublisherTableGateway;
import javafx.beans.property.SimpleStringProperty;

public class Publisher {

	private int id;
	private SimpleStringProperty publisherName;
	
	private PublisherTableGateway gateway;
	
	public Publisher() {
		publisherName = new SimpleStringProperty();
		
		setName("");
	}
	
	public Publisher(String name) {
		publisherName = new SimpleStringProperty();
		
		setName(name);
	}

	public String getName() {
		return publisherName.get();
	}

	public void setName(String name) {
		this.publisherName.set(name);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public PublisherTableGateway getGateway() {
		return gateway;
	}

	public void setGateway(PublisherTableGateway gateway) {
		this.gateway = gateway;
	}
	
	public SimpleStringProperty nameProperty() {
		return publisherName;
	}

	@Override
	public String toString() {
		return publisherName.get();
	}
}
