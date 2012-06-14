package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class Track {
	private int id;
	private String name;
	private String duration;
	private int label_id;
	private int album_id;
	
	public Track (ResultSet result) {
		try {
			id = result.getInt ("id");
			name = result.getString ("name");
			duration = result.getString ("length_seconds");
			label_id = result.getInt ("label_id");
			album_id = result.getInt ("album_id");
		}
		catch (Exception e) {
			System.out.println ("Error in new Track: " + e);
		}
	}
	
	public String getId () {
		return id + "";
	}
	public String getName () {
		return name;
	}
	public String getDuration () {
		return duration;
	}
	
	public String getLabel () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_label WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, label_id + "");
			ResultSet result = prepareStatement.executeQuery();
			
                        result.next ();
			String label = result.getString ("name");
			
			conn.close ();
			return label;
		}
		catch (Exception e) {
			System.out.println ("Error in Track.getLabel: " + e);
		}
		return null;
	}
	public Group getGroup () {
		return this.getAlbum ().getGroup ();
	}
	public Album getAlbum () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_album WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, album_id + "");
			ResultSet result = prepareStatement.executeQuery();
                        result.next ();
                        
                        Album a = new Album (result);
			
			conn.close ();
			return a;
		}
		catch (Exception e) {
			System.out.println ("Error in Track.getAlbum: " + e);
		}
		return null;
	}
}