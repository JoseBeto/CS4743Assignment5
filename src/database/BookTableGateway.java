package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.AuditTrailEntry;
import model.Book;

public class BookTableGateway {
	private Connection conn;
	private PublisherTableGateway pubGateway;
	
	public BookTableGateway(Connection conn) {
		this.conn = conn;
		pubGateway = new PublisherTableGateway(conn);
	}
	
	public void updateBook(Book book) throws AppException {
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
			st = conn.prepareStatement("insert into book (id, title, summary, "
					+ "year_published, publisher_id, isbn) values (?, ?, ?, ?, ?, ?)");
			st.setInt(1, book.getId());
			st.setString(2, book.getTitle());
			st.setString(3, book.getSummary());
			st.setInt(4, book.getYearPublished());
			st.setInt(5, book.getPublisher().getId());
			st.setString(6, book.getIsbn());
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
	
	public ObservableList<Book> getBooks() throws AppException {
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book order by title");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book(pubGateway, rs.getString("title"), rs.getString("summary"),
						rs.getInt("year_published"), rs.getInt("publisher_id"), rs.getString("isbn"));
				book.setGateway(this);
				book.setId(rs.getInt("id"));
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
	
	public ObservableList<Book> getBooks(String search) throws AppException {
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book where title like ?");
			st.setString(1, "%" + search + "%");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book(pubGateway, rs.getString("title"), rs.getString("summary"),
						rs.getInt("year_published"), rs.getInt("publisher_id"), rs.getString("isbn"));
				book.setGateway(this);
				book.setId(rs.getInt("id"));
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
	
	public ObservableList<AuditTrailEntry> getAuditTrails(Book book) throws AppException {
		ObservableList<AuditTrailEntry> auditTrailEntries = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select book_audit_trail order by date_added where book_id = ?");
			st.setInt(1, book.getId());
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				AuditTrailEntry auditTrailEntry = new AuditTrailEntry(rs.getInt("book_id"), rs.getDate("date_added"),
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
}
