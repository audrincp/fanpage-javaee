package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class Concert {
	private int id;
	private String country;
	private String place;
	private String date;
	private String description;
	
	public Concert (ResultSet result) {
		try {
			id = result.getInt ("id");
			country = result.getString ("country");
			place = result.getString ("place");
			date = Utils.toNormDate (result.getString ("concert_date"));
			description = result.getString ("description");
		}
		catch (Exception e) {
			System.out.println ("Error in new Concert: " + e);
		}
	}
	
	public String getId () {
		return id + "";
	}
	public String getCountry () {
		return country;
	}
	public String getPlace () {
		return place;
	}
	public String getDate () {
		return date;
	}
	public String getDescription () {
		return description;
	}
	
	public String getImageSrc () {
		return UploadFiles.getSrcForImage("concert", id);
	}
	public boolean isExistsImage () {
		return new File (UploadFiles.getPathForImage("concert", id)).exists ();
	}
	
	public Track[] getTracks () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_track WHERE id IN ( " + 
							"SELECT track_id FROM fp_concert_tracks WHERE concert_id=?" + 
						 " )";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, id + "");
			ResultSet result = prepareStatement.executeQuery();
			
			ArrayList al = new ArrayList ();
			while (result.next ()) {
				al.add (new Track (result));
			}
			
			Track[] arr = new Track[al.size ()];
			for (int i = 0; i < al.size (); ++ i)
				arr[i] = (Track)al.get (i);
			
			conn.close ();
			return arr;
		}
		catch (Exception e) {
			System.out.println ("Error in Concert.getTracks: " + e);
		}
		return null;
	}
	public Group[] getGroups () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_group WHERE id IN ( " + 
							"SELECT DISTINCT group_id FROM fp_album WHERE id IN ( " + 
								"SELECT album_id FROM fp_track WHERE id IN ( " + 
									"SELECT track_id FROM fp_concert_tracks WHERE concert_id=?" + 
								" ) " + 
							" ) " + 
						 " )";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, id + "");
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
			System.out.println ("Error in Concert.getGroups: " + e);
		}
		return null;
	}
}