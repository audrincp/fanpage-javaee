package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_video")
@SessionScoped
public class PageVideoBean implements Serializable {
	public PageVideoBean () {
	}
	
	private Video x;
	public Video getCurrentVideo () {
		return x;
	}
	public String setCurrentTrack (int track_id) {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_video WHERE track_id=? AND ROWNUM <= 1";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, track_id + "");
			ResultSet result = prepareStatement.executeQuery();
			
                        result.next ();
                        x = new Video (result);
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in setCurrentTrack: " + e);
		}
		return "video";
	}
}
