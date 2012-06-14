package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_concert")
@SessionScoped
public class PageConcertBean implements Serializable {
	public PageConcertBean () {
	}
	
	private Concert x;
	public Concert getCurrentConcert () {
		return x;
	}
	public String setCurrentConcert (int concert_id) {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_concert WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, concert_id + "");
			ResultSet result = prepareStatement.executeQuery();
			
                        result.next ();
                        x = new Concert (result);
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in setCurrentConcert: " + e);
		}
		return "concert";
	}
}
