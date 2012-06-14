package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_concerts")
@SessionScoped
public class PageConcertsBean implements Serializable {
	public PageConcertsBean () {
	}
	
	public Concert[] getConcerts () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_concert";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			ResultSet result = prepareStatement.executeQuery();
			
			ArrayList al = new ArrayList ();
			while (result.next ()) {
				al.add (new Concert (result));
			}
			
			Concert[] arr = new Concert[al.size ()];
			for (int i = 0; i < al.size (); ++ i)
				arr[i] = (Concert)al.get (i);
			
			conn.close ();
			return arr;
		}
		catch (Exception e) {
			System.out.println ("Error in getConcerts: " + e);
		}
		return null;
	}
}
