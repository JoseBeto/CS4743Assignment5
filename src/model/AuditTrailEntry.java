package model;

import java.sql.Date;

public class AuditTrailEntry {
	
	private int id;
	private Date dateAdded;
	private String message;

	public AuditTrailEntry(int bookId, Date dateAdded, String message) {
		this.dateAdded = dateAdded;
		this.message = message;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
}
