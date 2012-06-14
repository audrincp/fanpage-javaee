package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class Label {
	private int id;
	private String name;
	private String country;
	private String place;
	private String description;
	
	public Label (ResultSet result) {
		try {
			id = result.getInt ("id");
			name = result.getString ("name");
			country = result.getString ("country");
			place = result.getString ("place");
			description = result.getString ("description");
		}
		catch (Exception e) {
			System.out.println ("Error in new Label: " + e);
		}
	}
	
	public String getId () {
		return id + "";
	}
	public String getName () {
		return name;
	}
	public String getCountry () {
		return country;
	}
	public String getPlace () {
		return place;
	}
	public String getDescription () {
		return description;
	}
	
	public String getImageSrc () {
		return UploadFiles.getSrcForImage("labels", id);
	}
	public boolean isExistsImage () {
		return new File (UploadFiles.getPathForImage("labels", id)).exists ();
	}
}