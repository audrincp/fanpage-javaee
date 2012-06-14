package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_album")
@SessionScoped
public class PageAlbumBean implements Serializable {
	public PageAlbumBean () {
	}
	
	private Album x;
	public Album getCurrentAlbum () {
		return x;
	}
	public String setCurrentAlbum (int album_id) {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_album WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, album_id + "");
			ResultSet result = prepareStatement.executeQuery();
			
                        result.next ();
                        x = new Album (result);
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in setCurrentAlbum: " + e);
		}
		return "album";
	}
}
