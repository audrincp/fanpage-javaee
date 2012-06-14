package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class Album {
	private int id;
	private String name;
	private String date;
	private String description;
	private int group_id;
	
	public Album (ResultSet result) {
		try {
			id = result.getInt ("id");
			name = result.getString ("name");
			date = Utils.toNormDate (result.getString ("released"));
			description = result.getString ("description");
			group_id = result.getInt ("group_id");
		}
		catch (Exception e) {
			System.out.println ("Error in new Album: " + e);
		}
	}
	
	public String getId () {
		return id + "";
	}
	public String getName () {
		return name;
	}
	public String getDate () {
		return date;
	}
	public String getDescription () {
		return description;
	}
	
	public String getImageSrc () {
		return UploadFiles.getSrcForImage("albums", id);
	}
	public boolean isExistsImage () {
		return new File (UploadFiles.getPathForImage("albums", id)).exists ();
	}
	
	public Track[] getTracks () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_track WHERE album_id=?";
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
			System.out.println ("Error in Album.getTracks: " + e);
		}
		return null;
	}
	public Group getGroup () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_group WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, group_id + "");
			ResultSet result = prepareStatement.executeQuery();
                        result.next ();
                        
                        Group g = new Group (result);
			
			conn.close ();
			return g;
		}
		catch (Exception e) {
			System.out.println ("Error in Album.getGroup: " + e);
		}
		return null;
	}
}