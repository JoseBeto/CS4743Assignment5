package model;

import java.time.LocalDate;
import java.util.Date;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Author {

	private SimpleStringProperty firstName;
	private SimpleStringProperty lastName;
	private SimpleObjectProperty<LocalDate> doB;
	private SimpleStringProperty gender;
	private SimpleStringProperty website;
	
	public Author() {
		
	}
	
	public Author(String fName, String lName, LocalDate doB, String gender) {
		firstName = new SimpleStringProperty();
		lastName = new SimpleStringProperty();
		this.doB = new SimpleObjectProperty<LocalDate>();
		this.gender = new SimpleStringProperty();
		website = new SimpleStringProperty();
		setFirstName(fName);
		setLastName(lName);
		setDoB(doB);
		setGender(gender);
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
	
	public SimpleStringProperty firstNameProperty() {
		return firstName;
	}
	public SimpleStringProperty lastNameProperty() {
		return lastName;
	}
	public SimpleObjectProperty<LocalDate> dateOfBirthProperty(){
		return doB;
	}
	public SimpleStringProperty websiteProperty(){
		return website;
	}

	@Override
	public String toString() {
		return firstName.get() + " " + lastName.get();
	}
}
