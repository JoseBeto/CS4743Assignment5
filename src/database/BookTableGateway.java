package database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AuditTrailEntry;
import model.Author;
import model.AuthorBook;
import model.Book;

public class BookTableGateway {
	private Connection conn;
	private PublisherTableGateway pubGateway;
	private AuthorTableGateway authorGateway;
	
	public BookTableGateway(Connection conn) {
		this.conn = conn;
		this.pubGateway = new PublisherTableGateway(conn);
		this.authorGateway = new AuthorTableGateway(conn);
	}
	
	public void updateBook(Book book) throws AppException {
		createBookAuditTrails(book);
		createAuthorBookAuditTrails(book);
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update book set title = ?, summary = ?, year_published = ?, "
					+ "publisher_id = ?, isbn = ? where id = ?");
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPublished());
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getIsbn());
			st.setInt(6, book.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	
	public void addBook(Book book) throws AppException {
		PreparedStatement st = null;
		try {
			String statement = "insert into book (title, summary, "
					+ "year_published, publisher_id, isbn) values (?, ?, ?, ?, ?)";
			st = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
			st.setString(1, book.getTitle());
			st.setString(2, book.getSummary());
			st.setInt(3, book.getYearPublished());
			st.setInt(4, book.getPublisher().getId());
			st.setString(5, book.getIsbn());
			st.executeUpdate();
			
			ResultSet rs = st.getGeneratedKeys();
			rs.next();
			addAuditEntry(getBookById(rs.getInt(1)), "Book added");
			book.setId(rs.getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	
	public void deleteBook(Book book) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from book where id = ?");
			st.setInt(1, book.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	
	public int getTotalCount(String search) throws AppException {
		int count = 0;
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select count(*) as count from book where title like ?");
			st.setString(1, "%" + search + "%");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				count = rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return count;
	}
	
	public ObservableList<Book> getBooks(String search, int offset) throws AppException {
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book where title like ? order by title LIMIT 50 OFFSET ?");
			st.setString(1, "%" + search + "%");
			st.setInt(2,  offset);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book(pubGateway, rs.getString("title"), rs.getString("summary"),
						rs.getInt("year_published"), rs.getInt("publisher_id"), rs.getString("isbn"), rs.getDate("date_added").toLocalDate());
				book.setGateway(this);
				book.setId(rs.getInt("id"));
				book.setAuthors(getAuthorsForBook(book));
				books.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return books;
	}
	
	public ObservableList<Book> getBooksByPublisher(int id) throws AppException {
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book where publisher_id = ? order by title");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book(pubGateway, rs.getString("title"), rs.getString("summary"),
						rs.getInt("year_published"), rs.getInt("publisher_id"), rs.getString("isbn"), rs.getDate("date_added").toLocalDate());
				book.setGateway(this);
				book.setId(rs.getInt("id"));
				book.setAuthors(getAuthorsForBook(book));
				books.add(book);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return books;
	}
	
	public Book getBookById(int id) {
		PreparedStatement st = null;
		Book book = null;
		
		try {
			st = conn.prepareStatement("select * from book where id = ?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				book = new Book(pubGateway, rs.getString("title"), rs.getString("summary"),
						rs.getInt("year_published"), rs.getInt("publisher_id"), 
						rs.getString("isbn"), rs.getDate("date_added").toLocalDate());
				book.setGateway(this);
				book.setId(rs.getInt("id"));
				book.setAuthors(getAuthorsForBook(book));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return book;
	}
	
	public void createBookAuditTrails(Book book) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book where id = ?");
			st.setInt(1, book.getId());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(!book.getTitle().equals(rs.getString("title")))
					addAuditEntry(book, "Title changed from " + rs.getString("title") + " to " + book.getTitle());
				if(!book.getSummary().equals(rs.getString("summary")))
					addAuditEntry(book, "Summary changed from " + rs.getString("summary") + " to " + book.getSummary());
				if(book.getYearPublished() != rs.getInt("year_published"))
					addAuditEntry(book, "Year published changed from " + rs.getInt("year_published") + " to " + book.getYearPublished());
				if(book.getPublisher().getId() != rs.getInt("publisher_id"))
					addAuditEntry(book, "Publisher changed from " + pubGateway.getPublisherById(rs.getInt("publisher_id")) 
						+ " to " + book.getPublisher());
				if(!book.getIsbn().equals(rs.getString("isbn")))
					addAuditEntry(book, "Isbn changed from " + rs.getInt("isbn") + " to " + book.getIsbn());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	
	public void createAuthorBookAuditTrails(Book book) throws AppException {
		ObservableList<AuthorBook> localAuthorBooks = book.getAuthors();
		ObservableList<AuthorBook> dbAuthorBooks = getAuthorsForBook(book);
		
		for(AuthorBook dbAuthorBook : dbAuthorBooks) {
			int index = dbAuthorBooks.indexOf(dbAuthorBook);
			AuthorBook localAuthorBook = null;
			
			try {
				localAuthorBook = localAuthorBooks.get(index);
			} catch(IndexOutOfBoundsException e) {
				//This means dbAuthorBook was deleted from localAuthorBook
				continue;
			}
			
			if(!dbAuthorBook.getRoyalty().equals(localAuthorBook.getRoyalty())) {
				BigDecimal rounded = dbAuthorBook.getRoyalty().setScale(2);
				String dbRoyaltyPercent = rounded.multiply(new BigDecimal(100)) + "%";
				
				addAuditEntry(book, "Author " + dbAuthorBook.getAuthor().toString() + 
						" royalties changed from " + dbRoyaltyPercent + " to "
						+ localAuthorBook.getRoyaltyPercent());
			}
		}
	}
	
	public void addAuditEntry(Book book, String message) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into book_audit_trail (book_id, entry_msg)"
					+ " values (?, ?)");
			st.setInt(1, book.getId());
			st.setString(2, message);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	
	public ObservableList<AuditTrailEntry> getAuditTrails(Book book) throws AppException {
		ObservableList<AuditTrailEntry> auditTrailEntries = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book_audit_trail where book_id = ? order by date_added");
			st.setInt(1, book.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				AuditTrailEntry auditTrailEntry = new AuditTrailEntry(rs.getTimestamp("date_added"),
						rs.getString("entry_msg"));
				auditTrailEntry.setId(rs.getInt("id"));
				auditTrailEntries.add(auditTrailEntry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return auditTrailEntries;
	}
	
	public void addAuthorBook(AuthorBook authorBook) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into author_book (author_id, book_id, royalty)"
					+ " values (?, ?, ?)");
			st.setInt(1, authorBook.getAuthor().getId());
			st.setInt(2, authorBook.getBook().getId());
			st.setBigDecimal(3, authorBook.getRoyalty());
			st.executeUpdate();
			
			addAuditEntry(authorBook.getBook(), "Author " 
					+ authorGateway.getAuthorById(authorBook.getAuthor().getId())+" added");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	
	public void updateAuthorBook(AuthorBook authorBook) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update author_book set author_id = ?, royalty = ? "
					+ "where book_id = ? and author_id = ?");
			st.setInt(1, authorBook.getAuthor().getId());
			st.setBigDecimal(2, authorBook.getRoyalty());
			st.setInt(3, authorBook.getBook().getId());
			st.setInt(4, authorBook.getAuthor().getId());
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}
	
	public void deleteAuthorBook(AuthorBook authorBook) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from author_book where book_id = ? and author_id = ?");
			st.setInt(1, authorBook.getBook().getId());
			st.setInt(2, authorBook.getAuthor().getId());
			st.executeUpdate();
			
			addAuditEntry(authorBook.getBook(), "Author " 
					+ authorGateway.getAuthorById(authorBook.getAuthor().getId())+" deleted");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
	}

	public ObservableList<AuthorBook> getAuthorsForBook(Book book) {
		ObservableList<AuthorBook> authorBooks = FXCollections.observableArrayList();

		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author_book where book_id = ?");
			st.setInt(1, book.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Author author = new AuthorTableGateway(conn).getAuthorById(rs.getInt("author_id"));
				
				AuthorBook authorBook = new AuthorBook(author, book
						, rs.getBigDecimal("royalty"));
				authorBooks.add(authorBook);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AppException(e);
		} finally {
			try {
				if(st != null)
					st.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		return authorBooks;
	}
}
