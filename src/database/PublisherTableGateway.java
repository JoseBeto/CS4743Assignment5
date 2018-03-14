package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Publisher;

public class PublisherTableGateway {
	private Connection conn;
	
	public PublisherTableGateway(Connection conn) {
		this.conn = conn;
	}
	
	public ObservableList<Publisher> getPublishers() throws AppException {
		ObservableList<Publisher> publishers = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from publisher order by publisher_name");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Publisher publisher = new Publisher(rs.getString("publisher_name"));
				publisher.setGateway(this);
				publisher.setId(rs.getInt("id"));
				publishers.add(publisher);
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
		return publishers;
	}
	
	public Publisher getPublisherById(int publisherId) {
		ObservableList<Publisher> publishers = getPublishers();
		
		for(int i = 0; i < publishers.size(); i++) {
			if(publishers.get(i).getId() == publisherId)
				return publishers.get(i);
		}
		return null;
	}
}
