package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import database.AuthorTableGateway;

public class AuthorBook {

	private Author author;
	private Book book;
	private int royalty;
	private Boolean newRecord = true;
	
	public AuthorBook(int authorId, Book book, BigDecimal royalty, Connection conn) {
		this.author = new AuthorTableGateway(conn).getAuthorById(authorId);
		this.book = book;
		this.royalty = 100000 * royalty;
		
		newRecord = false;
	}
	
	public AuthorBook() {
		
	}
}
