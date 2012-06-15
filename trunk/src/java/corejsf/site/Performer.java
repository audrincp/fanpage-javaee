package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class Performer {
	private int id;
	private String description;
	private String name;
	private String surname;
	private String period;
	private String birthDate;
	private String deathDate;
	private String country;
        private String place;
	
	public Performer (ResultSet result) {
		try {
			id = result.getInt ("id");
			name = result.getString ("name");
			surname = result.getString ("surname");
			birthDate = Utils.toNormDate (result.getString ("born_date"));
			deathDate = Utils.toNormDate (result.getString ("death"));
			country = result.getString ("country");
                        place = result.getString ("born_place");
			description = result.getString ("description");
		}
		catch (Exception e) {
			System.out.println ("Error in new Performer: " + e);
		}
	}
	
	public Performer (int performer_id, String period) {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_performer WHERE id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, performer_id + "");
			ResultSet result = prepareStatement.executeQuery();
                        
                        result.next ();
			id = result.getInt ("id");
			name = result.getString ("name");
			surname = result.getString ("surname");
			birthDate = Utils.toNormDate (result.getString ("born_date"));
			deathDate = Utils.toNormDate (result.getString ("death"));
			country = result.getString ("country");
			description = result.getString ("description");
			this.period = period;
			
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in new Performer: " + e);
		}
	}
	
	public String getId () {
		return id + "";
	}
	public String getDescription () {
		return description;
	}
	public String getFullName () {
		return surname + " " + name;
	}
	public String getPeriod () {
		return period;
	}
	public String getBirthDate () {
		return birthDate;
	}
	public String  getDeathDate () {
		return deathDate;
	}
	public String getCountry () {
		return country;
	}
        public String getPlace () {
		return place;
	}
	
	public String getImageSrc () {
		return UploadFiles.getSrcForImage("performer", id);
	}
	public boolean isExistsImage () {
		return new File (UploadFiles.getPathForImage("performer", id)).exists ();
	}
	
	public Group[] getGroups () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM fp_group WHERE id IN ( " + 
							"SELECT DISTINCT group_id FROM fp_group_performers WHERE performer_id=?" + 
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
			System.out.println ("Error in Performer.getGroups: " + e);
		}
		return null;
	}
}