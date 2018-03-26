package model;

import java.math.BigDecimal;

public class AuthorBook {

	private Author author;
	private Book book;
	private BigDecimal royalty;
	private Boolean newRecord = true;
	
	public AuthorBook(Author author, Book book, BigDecimal royalty) {
		this.author = author;
		this.book = book;
		this.royalty = royalty.multiply(new BigDecimal(100000));
		
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
		return royalty;
	}
	
	@Override
	public String toString() {
		String s = "";
		for(int i = 0; i < (50 - author.toString().length()); i++) {
			s += " ";
		}
		return "Author: " + author + s + "Royalty: " + royalty + "%";
	}
}
