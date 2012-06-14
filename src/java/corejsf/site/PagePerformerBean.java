package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_performer")
@SessionScoped
public class PagePerformerBean implements Serializable {
	public PagePerformerBean () {
	}
	
	private Performer x;
	public Performer getCurrentPerformer () {
		return x;
	}
	public String setCurrentPerformer (int performer_id) {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_performer WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, performer_id + "");
			ResultSet result = prepareStatement.executeQuery();
			
                        result.next ();
                        x = new Performer (result);
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in setCurrentPerformer: " + e);
		}
		return "performer";
	}
}
