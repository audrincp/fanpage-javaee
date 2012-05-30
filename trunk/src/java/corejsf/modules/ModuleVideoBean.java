package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;
import javax.faces.model.SelectItem;

@Named("module_video")
@SessionScoped
public class ModuleVideoBean implements Serializable {
	
	private final String tableName = "fp_video";
	private final String trackTableName = "fp_track";
	private final String groupTableName = "fp_group";
	private final String albumTableName = "fp_album";

    public String[] getTableFields () {
		String[] arr = new String[3];
		arr[0] = "Название";
		arr[1] = "Композиция";
		arr[2] = "Длительность";
		return arr;
    }

    public Item[] getTableItems () {
		try {
			if (groupId > 0) {
				Connection conn = DBController.getConnection();
				
				String sql = " SELECT * FROM " + tableName + 
								" WHERE track_id IN (" + 
									" SELECT id FROM " + trackTableName + " WHERE album_id IN ( " + 
										" SELECT id FROM " + albumTableName + " WHERE group_id=? " + 
									" ) " + 
								" ) ";
				PreparedStatement prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, groupId + "");
				ResultSet result = prepareStatement.executeQuery();
				
				ArrayList items = new ArrayList ();
				while (result.next ()) {
					Item item = new Item (result.getInt ("id"), 3, 5);
					item.setPublicValue (0, result.getString ("name"));
					item.setPublicValue (1, DBCore.getTrackName (result.getString ("track_id")));
					item.setPublicValue (2, DBCore.convertSecondsToMMSS (result.getString ("length_seconds")));
					item.setEditValue (0, result.getString ("name"));
					item.setEditValue (1, result.getString ("track_id"));
					item.setEditValue (2, DBCore.convertSecondsToMMSS (result.getString ("length_seconds")));
					item.setEditValue (3, result.getString ("href"));
					item.setEditValue (4, result.getString ("description"));
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

    private String itemName;
    public String getEditName ()           {   return "";   }
    public void setEditName (String value) {   itemName = Utils.escapeQuotes (value);  }
	private String itemTrackId;
	public String getEditTrackId ()           {   return "";   }
    public void setEditTrackId (String value) {   itemTrackId = Utils.escapeQuotes (value);  }
    private String itemLengthSeconds;
    public String getEditLengthSeconds ()           {   return "";   }
    public void setEditLengthSeconds (String value) {   itemLengthSeconds = Utils.escapeQuotes (value);  }
    private String itemHref;
    public String getEditHref ()           {   return "";   }
    public void setEditHref (String value) {   itemHref = Utils.escapeQuotes (value);  }
    private String itemDescription;
    public String getEditDescription ()           {   return "";   }
    public void setEditDescription (String value) {   itemDescription = Utils.escapeQuotes (value);  }
    
    public ModuleVideoBean() {
        itemId = 0;
		itemName = "";
		itemLengthSeconds = "";
		itemHref = "";
		itemDescription = "";
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
			Connection conn = DBController.getConnection();
			String sql = " INSERT INTO " + tableName + 
						 " (name, track_id, length_seconds, href, description) VALUES (?, ?, ?, ?, ?) ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemTrackId);
			prepareStatement.setString (3, DBCore.convertMMSSToSeconds (itemLengthSeconds));
			prepareStatement.setString (4, itemHref);
			prepareStatement.setString (5, itemDescription);
			ResultSet result = prepareStatement.executeQuery();
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in add item: " + e);
		}
	}
	private void updateItem () {
		try {
			Connection conn = DBController.getConnection();
			String sql = " UPDATE " + tableName + " SET " + 
						 " name=?, track_id=?, length_seconds=?, href=?, description=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemTrackId + "");
			prepareStatement.setString (3, DBCore.convertMMSSToSeconds (itemLengthSeconds));
			prepareStatement.setString (4, itemHref);
			prepareStatement.setString (5, itemDescription);
			prepareStatement.setString (6, itemId + "");
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
