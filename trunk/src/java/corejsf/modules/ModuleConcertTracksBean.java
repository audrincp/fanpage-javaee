package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import javax.faces.model.SelectItem;
import java.util.ArrayList;

@Named("module_concert_tracks")
@SessionScoped
public class ModuleConcertTracksBean implements Serializable {
	
	private final String tableName = "fp_concert_tracks";
	private final String trackTableName = "fp_track";
	private final String groupTableName = "fp_group";
	private final String albumTableName = "fp_album";

    public String[] getTableFields () {
		String[] arr = new String[3];
		arr[0] = "Группа";
		arr[1] = "Название";
		arr[2] = "Длительность";
		return arr;
    }

    public Item[] getTableItems () {
		try {
			if (concertId > 0) {
				Connection conn = DBController.getConnection();
				
				String sql = "SELECT * FROM " + tableName + " WHERE concert_id=?";
				PreparedStatement prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, concertId + "");
				ResultSet result = prepareStatement.executeQuery();
				
				sql = "SELECT * FROM " + trackTableName + " WHERE id=?";
				PreparedStatement psmtSelectTrack = conn.prepareStatement (sql);
				
				sql = 	" SELECT * FROM " + groupTableName + 
						" WHERE id IN ( SELECT group_id FROM " + albumTableName + " WHERE id=? ) ";
				PreparedStatement psmtSelectGroup = conn.prepareStatement (sql);
				
				ArrayList items = new ArrayList ();
				while (result.next ()) {
					Item item = new Item (result.getInt ("id"), 3, 1);
					
					psmtSelectTrack.setString (1, result.getInt ("track_id") + "");
					ResultSet resultTrack = psmtSelectTrack.executeQuery();
					resultTrack.next ();
					
					item.setPublicValue (1, resultTrack.getString ("name"));
					
					int length_seconds = Integer.parseInt (resultTrack.getString ("length_seconds"));
					int m = length_seconds / 60;
					int s = length_seconds % 60;
					String mmhh = (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
					
					item.setPublicValue (2, mmhh);
					
					psmtSelectGroup.setString (1, resultTrack.getString ("album_id"));
					ResultSet resultGroup = psmtSelectGroup.executeQuery();
					resultGroup.next ();
					item.setPublicValue (0, resultGroup.getString ("name"));
					
					item.setEditValue (0, result.getString ("track_id"));
					items.add (item);
				}
				
				conn.close ();
			
				return Item.ObjectsToItems (items.toArray ());
			}
		}
		catch (Exception e) {
			System.out.println ("Error in get table items: " + e);
		}
		return null;
    }
	
	private int concertId = 0;
	public String getConcert () {
		return concertId + "";
	}
	public void setConcert (String value) {
		try {
			concertId = Integer.parseInt (value);
		}
		catch (Exception e) {
			System.out.println ("Error in set concert: " + e);
		}
	}
	public SelectItem[] getConcerts () {
		return DBCore.getConcerts ();
	}
	
	private int groupId = 0;
	public String getGroup () {
		return groupId + "";
	}
	public void setGroup (String value) {
		try {
			groupId = Integer.parseInt (value);
		}
		catch (Exception e) {
			System.out.println ("Error in set group: " + e);
		}
	}
	public SelectItem[] getGroups () {
		return DBCore.getGroups ();
	}
	
	private int albumId = 0;
	public String getAlbum () {
		if (groupId == 0)
			albumId = 0;
		return albumId + "";
	}
	public void setAlbum (String value) {
		try {
			if (groupId == 0)
				albumId = 0;
			else
				albumId = Integer.parseInt (value);
		}
		catch (Exception e) {
			System.out.println ("Error in set album: " + e);
		}
	}
	public SelectItem[] getAlbums () {
		return DBCore.getAlbums (groupId);
	}
	public SelectItem[] getTracks () {
		return DBCore.getTracks (albumId);
	}

    private int itemId;
    public String getEditId () { return ""; }
    public void setEditId (String value) { 
        try {
            itemId = Integer.parseInt (value);
        }
        catch (Exception e) {
            itemId = 0;
        }
    }

    private String itemTrackId;
    public String getEditTrackId ()           {   return "";   }
    public void setEditTrackId (String value) {   itemTrackId = Utils.escapeQuotes (value);  }
    
    public ModuleConcertTracksBean() {
        itemId = 0;
		itemTrackId = "";
    }
	
    public String remove (int id) {
		try {
			Connection conn = DBController.getConnection();
			String sql = " DELETE FROM " + tableName + " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, id + "");
			ResultSet result = prepareStatement.executeQuery();
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in remove: " + e);
		}
        return null;
    }
	private void addItem () {
		try {
			if (groupId > 0) {
				Connection conn = DBController.getConnection();
				String sql = " INSERT INTO " + tableName + 
							 " (concert_id, track_id) VALUES (?, ?) ";
				PreparedStatement prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, concertId + "");
				prepareStatement.setString (2, itemTrackId);
				ResultSet result = prepareStatement.executeQuery();
				conn.close ();
			}
		}
		catch (Exception e) {
			System.out.println ("Error in add item: " + e);
		}
	}
	private void updateItem () {
		try {
			Connection conn = DBController.getConnection();
			String sql = " UPDATE " + tableName + " SET " + 
						 " track_id=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemTrackId);
			prepareStatement.setString (2, itemId + "");
			ResultSet result = prepareStatement.executeQuery();
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in update item: " + e);
		}
	}
    public String save () {
		if (itemId == 0)
			addItem ();
		else
			updateItem ();
		
        return null;
    }
}
