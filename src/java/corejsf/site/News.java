package corejsf.site;

import java.sql.*;
import corejsf.*;

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
}