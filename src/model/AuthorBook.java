package model;

import java.math.BigDecimal;

public class AuthorBook {

	private Author author;
	private Book book;
	private BigDecimal royalty;
	private String royaltyPercent;
	private Boolean newRecord = true;
	
	public AuthorBook(Author author, Book book, BigDecimal royalty) {
		this.author = author;
		this.book = book;
		setRoyalty(royalty);
	}
	
	public Author getAuthor() {
		return author;
	}
	
	
	public Book getBook() {
		return book;
	}
	
	public void setRoyalty(BigDecimal royalty) {
		this.royalty = royalty;
		
		BigDecimal rounded = royalty.multiply(new BigDecimal(100));
		this.royaltyPercent = String.format("%.2f%s", rounded, "%");
	}
	
	public BigDecimal getRoyalty() {
		return royalty;
	}
	
	public String getRoyaltyPercent() {
		return this.royaltyPercent;
	}
	
	public Boolean isNewRecord() {
		return newRecord;
	}
	
	public void setSaved() {
		this.newRecord = false;
	}
}
