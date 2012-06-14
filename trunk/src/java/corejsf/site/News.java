package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class News {
	private int id;
	private String title;
	private String publicationDate;
	private String shortText;
	private String text;
	public News (ResultSet result) {
		try {
			id = result.getInt ("id");
			title = result.getString ("title");
			publicationDate = Utils.toNormDate (result.getString ("news_date"));
			shortText = result.getString ("short");
			text = result.getString ("full");
		}
		catch (Exception e) {
			System.out.println ("Error in new News: " + e);
		}
	}
	public String getId () {
		return id + "";
	}
	public String getImageSrc () {
		return UploadFiles.getSrcForImage("news", id);
	}
	public boolean isExistsImage () {
		return new File (UploadFiles.getPathForImage("news", id)).exists ();
	}
	public String getPublicationDate () {
		return publicationDate;
	}
	public String getTitle () {
		return title;
	}
	public String getShortText () {
		return shortText;
	}
	public String getText () {
		return text;
	}
	public static int getCount () {
		try {
			Connection conn = DBController.getConnection ();
			
			String sql = "SELECT COUNT(*) FROM fp_news";
			PreparedStatement ps = conn.prepareStatement (sql);
			ResultSet result = ps.executeQuery ();
			result.next ();
			int count = result.getInt (1);
			
			conn.close ();
			
			return count;
		}
		catch (Exception e) {
			System.out.println ("Error in News.getCount: " + e);
		}
		return 0;
	}
}