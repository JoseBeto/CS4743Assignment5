package model;

import database.PublisherTableGateway;
import database.AppException;
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
	
	public void save() throws AppException {
		/*if(id == 0)
			gateway.addPublisher(this);
		else
			gateway.updatePublisher(this);*/
	}
	
	public void delete() {
		//gateway.deletePublisher(this);
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
		return "Publisher name: " + publisherName.get();
	}
}
