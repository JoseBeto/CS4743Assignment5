package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Item;

public class ItemTableGateway {
	private Connection conn;
	
	public ItemTableGateway(Connection conn) {
		this.conn = conn;
	}
	
	public ObservableList<Item> getItems() throws AppException {
		ObservableList<Item> items = FXCollections.observableArrayList();
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select * from item");
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				Item item = new Item(rs.getString("name"), rs.getDouble("price"), rs.getString("description"), rs.getString("image"));
				item.setGateway(this);
				item.setId(rs.getInt("id"));
				items.add(item);
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
		return items;
	}
}
