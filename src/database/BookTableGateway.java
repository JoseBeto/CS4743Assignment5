package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Book;

public class BookTableGateway {
	private Connection conn;
	private PublisherTableGateway pubGateway;
	
	public BookTableGateway(Connection conn) {
		this.conn = conn;
		pubGateway = new PublisherTableGateway(conn);
	}
	
	public void updateBook(Book book) throws AppException {
		/*PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update author set first_name = ?, last_name = ?, dob = ?, "
					+ "gender = ?, web_site = ? where id = ?");
			st.setString(1, author.getFirstName());
			st.setString(2, author.getLastName());
			st.setDate(3, Date.valueOf(author.getDoB()));
			st.setString(4, author.getGender());
			st.setString(5, author.getWebsite());
			st.setInt(6, author.getId());
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
		}*/
	}
	
	public void addBook(Book book) throws AppException {
		/*PreparedStatement st = null;
		try {
			st = conn.prepareStatement("insert into author (id, first_name, last_name, "
					+ "dob, gender, web_site) values (?, ?, ?, ?, ?, ?)");
			st.setInt(1, author.getId());
			st.setString(2, author.getFirstName());
			st.setString(3, author.getLastName());
			st.setDate(4, Date.valueOf(author.getDoB()));
			st.setString(5, author.getGender());
			st.setString(6, author.getWebsite());
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
		}*/
	}
	
	public void deleteBook(Book book) throws AppException {
		/*PreparedStatement st = null;
		try {
			st = conn.prepareStatement("delete from author where id = ?");
			st.setInt(1, author.getId());
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
		}*/
	}
	
	public ObservableList<Book> getBooks() throws AppException {
		ObservableList<Book> books = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from book order by title");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Book book = new Book(rs.getString("title"), rs.getString("summary"),
						rs.getInt("year_published"), rs.getInt("publisher_id"), rs.getString("isbn"));
				book.setGateway(this);
				book.setPubGateway(pubGateway);
				book.setPublisher();
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
}
