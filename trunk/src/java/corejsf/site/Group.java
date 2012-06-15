package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class Group {
	private int id;
	private String name;
	private String genre;
	private String beginDate;
	private String endDate;
	private String description;
	
	public Group (ResultSet result) {
		try {
			id = result.getInt ("id");
			name = result.getString ("name");
			genre = result.getString ("genre");
			beginDate = Utils.toNormDate (result.getString ("creation_date"));
			endDate = Utils.toNormDate (result.getString ("decay_date"));
			description = result.getString ("description");
		}
		catch (Exception e) {
			System.out.println ("Error in new Group: " + e);
		}
	}
	
	public String getId () {
		return id + "";
	}
	public String getName () {
		return name;
	}
	public String getGenre () {
		return genre;
	}
	public String getBeginDate () {
		return beginDate;
	}
	public String getEndDate () {
		return endDate.length() > 0 ? endDate : "настоящее время";
	}
	public String getDescription () {
		return description;
	}
	
	public String getImageSrc () {
		return UploadFiles.getSrcForImage("groups", id);
	}
	public boolean isExistsImage () {
		return new File (UploadFiles.getPathForImage("groups", id)).exists ();
	}
	
	public Performer[] getPerformers () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_group_performers WHERE group_id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, id + "");
			ResultSet result = prepareStatement.executeQuery();
			
			ArrayList al = new ArrayList ();
			while (result.next ()) {
				al.add (new Performer (result.getInt("performer_id"), result.getString("period")));
			}
			
			Performer[] arr = new Performer[al.size ()];
			for (int i = 0; i < al.size (); ++ i)
				arr[i] = (Performer)al.get (i);
			
			conn.close ();
			return arr;
		}
		catch (Exception e) {
			System.out.println ("Error in Group.getPerformers: " + e);
		}
		return null;
	}
	public Album[] getAlbums () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_album WHERE group_id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, id + "");
			ResultSet result = prepareStatement.executeQuery();
			
			ArrayList al = new ArrayList ();
			while (result.next ()) {
				al.add (new Album (result));
			}
			
			Album[] arr = new Album[al.size ()];
			for (int i = 0; i < al.size (); ++ i)
				arr[i] = (Album)al.get (i);
			
			conn.close ();
			return arr;
		}
		catch (Exception e) {
			System.out.println ("Error in Group.getAlbums: " + e);
		}
		return null;
	}
	public Prize[] getPrizes () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_prize WHERE id IN ( " + 
							"SELECT prize_id FROM fp_group_prizes WHERE group_id=?" + 
						 " )";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, id + "");
			ResultSet result = prepareStatement.executeQuery();
			
			ArrayList al = new ArrayList ();
			while (result.next ()) {
				al.add (new Prize (result));
			}
			
			Prize[] arr = new Prize[al.size ()];
			for (int i = 0; i < al.size (); ++ i)
				arr[i] = (Prize)al.get (i);
			
			conn.close ();
			return arr;
		}
		catch (Exception e) {
			System.out.println ("Error in Group.getPrizes: " + e);
		}
		return null;
	}
	public Concert[] getConcerts () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_concert WHERE id IN ( " + 
							"SELECT concert_id FROM fp_concert_tracks WHERE track_id IN ( " + 
								"SELECT id FROM fp_track WHERE album_id IN ( " + 
									"SELECT id FROM fp_album WHERE group_id=? " + 
								") " + 
							") " + 
						 ")";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, id + "");
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
			System.out.println ("Error in Group.getConcerts: " + e);
		}
		return null;
	}
}