package assignment2;

import java.util.Date;

public class Author {

	private String firstName;
	private String lastName;
	private Date doB;
	private String gender;
	private String website;
	
	public Author(String fName, String lName, Date doB, String gender) {
		setFirstName(fName);
		setLastName(lName);
		setDoB(doB);
		setGender(gender);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDoB() {
		return doB;
	}

	public void setDoB(Date doB) {
		this.doB = doB;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
}
