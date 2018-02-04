package model;

import database.AuthorTableGateway;
import java.time.LocalDate;
import database.AppException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Author {

	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleObjectProperty<LocalDate> doB;
	private SimpleStringProperty gender;
	private SimpleStringProperty website;
	private int id;
	private AuthorTableGateway gateway;
	
	public Author() {
		
	}
	
	public Author(String fName, String lName, LocalDate doB, String gender, String website) {
		firstName = new SimpleStringProperty();
		lastName = new SimpleStringProperty();
		this.doB = new SimpleObjectProperty<LocalDate>();
		this.gender = new SimpleStringProperty();
		this.website = new SimpleStringProperty();
		
		if(!isValidName(fName))
			throw new IllegalArgumentException("First name must be between 1 and 100 characters!");
		setFirstName(fName);
		
		if(!isValidName(lName))
			throw new IllegalArgumentException("Last name must be between 1 and 100 characters!");
		setLastName(lName);
		
		if(!isValidDate(doB))
			throw new IllegalArgumentException("Date must be a date before the current date!");
		setDoB(doB);
		
		if(!isValidGender(gender))
			throw new IllegalArgumentException("Gender must be either male, female, or unknown!");
		setGender(gender);
		
		if(!isValidWebsite(website))
			throw new IllegalArgumentException("Website must be no more than 100 characters!");
		setWebsite(website);
	}
	
	public void save() throws AppException {
		gateway.updateAuthor(this);
	}

	//biz logic
	public boolean isValidName(String name) {
		//First and last name must be between 1 and 100 chars
		if(name.length() < 1 || name.length() > 100)
			return false;
		return true;
	}
	
	public boolean isValidDate(LocalDate doB2) {
		LocalDate newDate = LocalDate.now();
		if(doB2.isBefore(newDate))
			return true;
		return false;
	}
	
	public boolean isValidGender(String gender) {
		//Gender must be either male, female, or unknown
		if(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female") || gender.equalsIgnoreCase("unknown"))
			return true;
		return false;
	}
	
	public boolean isValidWebsite(String website) {
		if(website.length() > 100)
			return false;
		return true;
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String firstName) {
		this.firstName.set(firstName);
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String lastName) {
		this.lastName.set(lastName);
	}

	public LocalDate getDoB() {
		return doB.get();
	}

	public void setDoB(LocalDate doB2) {
		this.doB.set(doB2);
	}

	public String getGender() {
		return gender.get();
	}

	public void setGender(String gender) {
		this.gender.set(gender);
	}

	public String getWebsite() {
		return website.get();
	}

	public void setWebsite(String website) {
		this.website.set(website);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public AuthorTableGateway getGateway() {
		return gateway;
	}

	public void setGateway(AuthorTableGateway gateway) {
		this.gateway = gateway;
	}
	
	public SimpleStringProperty firstNameProperty() {
		return firstName;
	}
	public SimpleStringProperty lastNameProperty() {
		return lastName;
	}
	public SimpleObjectProperty<LocalDate> dateOfBirthProperty(){
		return doB;
	}
	public SimpleStringProperty genderProperty(){
		return gender;
	}
	public SimpleStringProperty websiteProperty(){
		return website;
	}

	@Override
	public String toString() {
		return firstName.get() + " " + lastName.get();
	}
}
