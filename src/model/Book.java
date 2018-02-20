package model;

import database.BookTableGateway;
import database.PublisherTableGateway;

import java.time.LocalDate;
import database.AppException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

	private int id;
	private SimpleStringProperty title;
	private SimpleStringProperty summary;
	private SimpleIntegerProperty yearPublished;
	private SimpleObjectProperty<Publisher> publisher;
	private SimpleStringProperty isbn;
	private SimpleObjectProperty<LocalDate> dateAdded;
	private SimpleIntegerProperty publisherId;
	
	private BookTableGateway gateway;
	private PublisherTableGateway pubGateway;
	
	public Book() {
		this.title = new SimpleStringProperty();
		this.summary = new SimpleStringProperty();
		this.yearPublished = new SimpleIntegerProperty();
		this.publisher = new SimpleObjectProperty<Publisher>();
		this.publisherId = new SimpleIntegerProperty();
		this.isbn = new SimpleStringProperty();
		this.dateAdded = new SimpleObjectProperty<LocalDate>();
		
		setTitle("");
		setSummary("");
		setYearPublished(null);
		setIsbn("");
	}
	
	public Book(String title, String summary, int yearPublished, Integer publisherId, String isbn) {
		this.title = new SimpleStringProperty();
		this.summary = new SimpleStringProperty();
		this.yearPublished = new SimpleIntegerProperty();
		this.publisher = new SimpleObjectProperty<Publisher>();
		this.publisherId = new SimpleIntegerProperty();
		this.isbn = new SimpleStringProperty();
		this.dateAdded = new SimpleObjectProperty<LocalDate>();
		
		if(!isValidTitle(title))
			throw new IllegalArgumentException("Title must be between 1 and 255 characters!");
		setTitle(title);
		
		if(!isValidSummary(summary))
			throw new IllegalArgumentException("Summary must be less than 65536 characters!");
		setSummary(summary);
		
		if(!isValidYearPublished(yearPublished))
			throw new IllegalArgumentException("Year published must be a date before the current date!");
		setYearPublished(yearPublished);
		
		setPublisherId(publisherId);
		
		if(!isValidIsbn(isbn))
			throw new IllegalArgumentException("Isbn cannot be greater than 13 characters!");
		setIsbn(isbn);
	}
	
	public void save() throws AppException {
		/*if(id == 0)
			gateway.addAuthor(this);
		else
			gateway.updateAuthor(this);*/
	}
	
	public void delete() {
		//gateway.deleteAuthor(this);
	}

	//biz logic
	public boolean isValidTitle(String title) {
		//Title must be between 1 and 255 chars
		if(title.length() < 1 || title.length() > 255)
			return false;
		return true;
	}
	
	public boolean isValidSummary(String summary) {
		//Summary must be less than 65536 characters
		if(summary.length() < 65536)
			return true;
		return false;
	}
	
	public boolean isValidYearPublished(Integer year) {
		//Year published cannot be a date after the current date
		if(year == null)
			return false;
		if(year > LocalDate.now().getYear())
			return false;
		return true;
	}
	
	public boolean isValidIsbn(String isbn) {
		//Isbn cannot be greater than 13 characters
		if(isbn.length() > 13)
			return false;
		return true;
	}

	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {
		this.title.set(title);
	}

	public String getSummary() {
		return summary.get();
	}

	public void setSummary(String summary) {
		this.summary.set(summary);
	}

	public Integer getYearPublished() {
		return yearPublished.get();
	}

	public void setYearPublished(Integer yearPublished) {
		this.yearPublished.set(yearPublished);
	}

	public Publisher getPublisher() {
		return publisher.get();
	}

	public void setPublisher() {
		this.publisher.set(pubGateway.getPublisherById(getPublisherId()));
	}
	
	public int getPublisherId() {
		return publisherId.get();
	}
	
	public void setPublisherId(int publisherId) {
		this.publisherId.set(publisherId);
	}
	
	public String getIsbn() {
		return isbn.get();
	}
	
	public void setIsbn(String isbn) {
		this.isbn.set(isbn);
	}
	
	public LocalDate getDateAdded() {
		return dateAdded.get();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public BookTableGateway getGateway() {
		return gateway;
	}

	public void setGateway(BookTableGateway gateway) {
		this.gateway = gateway;
	}
	
	public void setPubGateway(PublisherTableGateway pubGateway) {
		this.pubGateway = pubGateway;
	}
	
	public SimpleStringProperty titleProperty() {
		return title;
	}
	public SimpleStringProperty summaryProperty() {
		return summary;
	}
	public SimpleIntegerProperty yearPublishedProperty() {
		return yearPublished;
	}
	public SimpleIntegerProperty publisherIdProperty(){
		return publisherId;
	}
	/*public SimpleObjectProperty<Publisher> publisherProperty(){
		return publisher;
	}*/
	public SimpleStringProperty isbnProperty(){
		return isbn;
	}
	public SimpleObjectProperty<LocalDate> dateAddedProperty(){
		return dateAdded;
	}

	@Override
	public String toString() {
		return title.get();
	}
}
