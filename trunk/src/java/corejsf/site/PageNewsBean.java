package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_news")
@SessionScoped
public class PageNewsBean implements Serializable {
	public PageNewsBean () {
	}
	
	private News x;
	public News getCurrentNews () {
		return x;
	}
	public String setCurrentNews (int news_id) {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_news WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, news_id + "");
			ResultSet result = prepareStatement.executeQuery();
			
                        result.next ();
                        x = new News (result);
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in setCurrentNews: " + e);
		}
		return "news";
	}
}
