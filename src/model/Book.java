package model;

import database.BookTableGateway;
import database.PublisherTableGateway;

import java.time.LocalDate;

import controller.AppController;
import database.AppException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Book {

	private int id;
	private SimpleStringProperty title;
	private SimpleStringProperty summary;
	private SimpleIntegerProperty yearPublished;
	private SimpleObjectProperty<Publisher> publisher;
	private SimpleStringProperty isbn;
	private SimpleObjectProperty<LocalDate> dateAdded;
	private ObservableList<AuthorBook> authorBooks;
	
	private BookTableGateway gateway;
	private PublisherTableGateway pubGateway;
	
	public Book(PublisherTableGateway pubGateway) {
		this.pubGateway = pubGateway;
		
		this.title = new SimpleStringProperty();
		this.summary = new SimpleStringProperty();
		this.yearPublished = new SimpleIntegerProperty();
		this.publisher = new SimpleObjectProperty<Publisher>();
		this.isbn = new SimpleStringProperty();
		this.dateAdded = new SimpleObjectProperty<LocalDate>();
		this.authorBooks = FXCollections.observableArrayList();
		
		setTitle("");
		setSummary("");
		setYearPublished(0);
		setPublisher(1);
		setIsbn("");
	}
	
	public Book(PublisherTableGateway pubGateway, String title, String summary, int yearPublished, 
			Integer publisherId, String isbn, LocalDate dateAdded) {
		this.pubGateway = pubGateway;
		
		this.title = new SimpleStringProperty();
		this.summary = new SimpleStringProperty();
		this.yearPublished = new SimpleIntegerProperty();
		this.publisher = new SimpleObjectProperty<Publisher>();
		this.isbn = new SimpleStringProperty();
		this.dateAdded = new SimpleObjectProperty<LocalDate>();
		this.authorBooks = FXCollections.observableArrayList();
		
		if(!isValidTitle(title))
			throw new IllegalArgumentException("Title must be between 1 and 255 characters!");
		setTitle(title);
		
		if(!isValidSummary(summary))
			throw new IllegalArgumentException("Summary must be less than 65536 characters!");
		setSummary(summary);
		
		if(!isValidYearPublished(yearPublished))
			throw new IllegalArgumentException("Year published must be a date before the current date!");
		setYearPublished(yearPublished);
		
		setPublisher(publisherId);
		
		if(!isValidIsbn(isbn))
			throw new IllegalArgumentException("Isbn cannot be greater than 13 characters!");
		setIsbn(isbn);
		
		setDateAdded(dateAdded);
	}

	public void save() throws AppException {
		if(id == 0)
			gateway.addBook(this);
		else 
			gateway.updateBook(this);
		
		updateAuthors();

		AppController.getInstance().changeView(AppController.BOOK_LIST, null);
	}
	
	private void updateAuthors() {
		ObservableList<AuthorBook> AuthorsInDB = gateway.getAuthorsForBook(this);
		for(AuthorBook localAuthorBook : getAuthors()) {
			Boolean foundInDB = false;
			for(AuthorBook authorInDB : AuthorsInDB) {
				if(isEqual(authorInDB, localAuthorBook)) {
					foundInDB = true;
					gateway.updateAuthorBook(localAuthorBook);
					break;
				}
			}
			if(!foundInDB)
				gateway.addAuthorBook(localAuthorBook);
		}
		
		for(AuthorBook authorInDB : AuthorsInDB) {
			Boolean localFound = false;
			for(AuthorBook localAuthorBook : getAuthors()) {
				if(isEqual(authorInDB, localAuthorBook)) {
					localFound = true;
					break;
				}
			}
			if(!localFound)
				gateway.deleteAuthorBook(authorInDB);
		}
	}
	
	private Boolean isEqual(AuthorBook book1, AuthorBook book2) {
		if(book1.getAuthor().getId() != book2.getAuthor().getId())
			return false;
		if(book1.getBook().getId() != book2.getBook().getId())
			return false;
		return true;
	}

	public void delete() {
		gateway.deleteBook(this);
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
		if(summary == null || summary.length() < 65536)
			return true;
		return false;
	}
	
	public boolean isValidYearPublished(Integer year) {
		//Year published cannot be a date after the current date
		if(year == null)
			return true;
		if(year > LocalDate.now().getYear())
			return false;
		return true;
	}
	
	public boolean isValidIsbn(String isbn) {
		if(isbn == null)
			return true;
		//Isbn cannot be greater than 13 characters
		if(isbn.length() > 13)
			return false;
		return true;
	}
	
	public boolean hasAuthors(ObservableList<AuthorBook> authors) {
		if(authors.size() < 1)
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

	public void setPublisher(int id) {
		this.publisher.set(pubGateway.getPublisherById(id));
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
	
	public void setDateAdded(LocalDate dateAdded) {
		this.dateAdded.set(dateAdded);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public ObservableList<AuditTrailEntry> getAuditTrail() {
		return gateway.getAuditTrails(this);
	}
	
	public void addAuditEntry(String message) {
		gateway.addAuditEntry(this, message);
	}
	
	public ObservableList<AuthorBook> getAuthors() {
		return authorBooks;
	}
	
	public void setAuthors(ObservableList<AuthorBook> authorBooks) {
		this.authorBooks.setAll(authorBooks);
	}
	
	public void addAuthor(AuthorBook authorBook) {
		this.authorBooks.add(authorBook);
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
	public SimpleObjectProperty<Publisher> publisherProperty(){
		return publisher;
	}
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
