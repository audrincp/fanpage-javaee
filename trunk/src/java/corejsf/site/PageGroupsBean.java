package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_groups")
@SessionScoped
public class PageGroupsBean implements Serializable {
	public PageGroupsBean () {
	}
	
	public Group[] getGroups () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_group";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			ResultSet result = prepareStatement.executeQuery();
			
			ArrayList al = new ArrayList ();
			while (result.next ()) {
				al.add (new Group (result));
			}
			
			Group[] arr = new Group[al.size ()];
			for (int i = 0; i < al.size (); ++ i)
				arr[i] = (Group)al.get (i);
			
			conn.close ();
			return arr;
		}
		catch (Exception e) {
			System.out.println ("Error in getGroups: " + e);
		}
		return null;
	}
}
