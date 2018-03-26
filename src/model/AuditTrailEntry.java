package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AuditTrailEntry {
	
	private int id;
	private Date dateAdded;
	private String message;

	public AuditTrailEntry(Date dateAdded, String message) {
		this.dateAdded = dateAdded;
		this.message = message;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		return formatter.format(dateAdded) + " " + message;
	}
}
