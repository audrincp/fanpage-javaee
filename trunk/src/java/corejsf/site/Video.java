package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class Video {
	private int id;
	private String name;
	private String duration;
	private String description;
	private String href;
	private int track_id;
	
	public Video (ResultSet result) {
		try {
			id = result.getInt ("id");
			name = result.getString ("name");
			href = result.getString ("href");
			duration = result.getString ("length_seconds");
			description = result.getString ("description");
			track_id = result.getInt ("track_id");
		}
		catch (Exception e) {
			System.out.println ("Error in new Video: " + e);
		}
	}
	
	public String getName () {
		return name;
	}
	public String getDuration () {
		return duration;
	}
	public String getDescription () {
		return description;
	}
	public String getHref () {
		return href;
	}
	
	public Track getTrack () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_track WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, track_id + "");
			ResultSet result = prepareStatement.executeQuery();
			
			conn.close ();
			return new Track (result);
		}
		catch (Exception e) {
			System.out.println ("Error in Video.getTrack: " + e);
		}
		return null;
	}
}