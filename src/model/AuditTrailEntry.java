package model;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;

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

	@Override
	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

		return formatter.format(dateAdded) + " " + message;
	}
}
