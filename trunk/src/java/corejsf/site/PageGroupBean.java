package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_group")
@SessionScoped
public class PageGroupBean implements Serializable {
	public PageGroupBean () {
	}
	
	private Group x;
	public Group getCurrentGroup () {
		return x;
	}
	public String setCurrentGroup (int group_id) {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_group WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, group_id + "");
			ResultSet result = prepareStatement.executeQuery();
			
                        result.next ();
                        x = new Group (result);
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in setCurrentGroup: " + e);
		}
		return "group";
	}
}
