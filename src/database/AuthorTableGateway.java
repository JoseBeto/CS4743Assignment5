package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Author;

public class AuthorTableGateway {
	private Connection conn;
	
	public AuthorTableGateway(Connection conn) {
		this.conn = conn;
	}
	
	public void updateAuthor(Author author) throws AppException {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update author set first_name = ?, last_name = ?, dob = ?, "
					+ "gender = ?, web_site = ? where id = ?");
			st.setString(1, author.getFirstName());
			st.setString(2, author.getLastName());
			st.setDate(3, Date.valueOf(author.getDoB()));
			st.setString(4, author.getGender());
			st.setString(5, author.getWebsite());
			st.setInt(6, author.getId());
			
			if(!author.getLastModified().isEqual(getLastModified(author)))
				throw new AppException("not in sync");
			
			st.executeUpdate();
			
			author.setLastModified(getLastModified(author));
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
	
	public LocalDateTime getLastModified(Author author) {
		LocalDateTime lastModified = null;
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author where id = ?");
			st.setInt(1, author.getId());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				lastModified = rs.getTimestamp("last_modified").toLocalDateTime();
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
		
		return lastModified;
	}

	public void addAuthor(Author author) throws AppException {
		PreparedStatement st = null;
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
		}
	}
	
	public void deleteAuthor(Author author) throws AppException {
		PreparedStatement st = null;
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
		}
	}
	
	public ObservableList<Author> getAuthors() throws AppException {
		ObservableList<Author> authors = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from author order by first_name");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Author author = new Author(rs.getString("first_name"), rs.getString("last_name"),
						rs.getDate("dob").toLocalDate(), rs.getString("gender"), 
						rs.getString("web_site"), rs.getTimestamp("last_modified").toLocalDateTime());
				author.setGateway(this);
				author.setId(rs.getInt("id"));
				authors.add(author);
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
		return authors;
	}
}
