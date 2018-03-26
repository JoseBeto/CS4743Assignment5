package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;

import database.AuthorTableGateway;

public class AuthorBook {

	private Author author;
	private Book book;
	private int royalty;
	private BigDecimal royaltyDec;
	private Boolean newRecord = true;
	
	public AuthorBook(Author author, Book book, BigDecimal royalty) {
		this.author = author;
		this.book = book;
		//this.royalty = 100000 * royalty;
		//this.royalty = 1;
		this.royaltyDec = royalty;
		
		newRecord = false;
	}
	
	public AuthorBook() {
		
	}
	
	public Author getAuthor() {
		return author;
	}
	
	public Book getBook() {
		return book;
	}
	
	public BigDecimal getRoyalty() {
		return royaltyDec;
	}
	
	@Override
	public String toString() {
		return "Author: " + author + "\t\t\t\tRoyalty: " + royaltyDec + "%";
	}
}
