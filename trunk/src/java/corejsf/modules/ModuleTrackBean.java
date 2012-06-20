package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;
import javax.faces.model.SelectItem;

@Named("module_track")
@SessionScoped
public class ModuleTrackBean implements Serializable {
	
	private final String tableName = "fp_track";

    public String[] getTableFields () {
		String[] arr = new String[2];
		arr[0] = "Название";
		arr[1] = "Длительность (секунд)";
		return arr;
    }

    public Item[] getTableItems () {
		try {
			if (groupId > 0 && albumId > 0) {
				Connection conn = DBController.getConnection();
				
				String sql = "SELECT * FROM " + tableName + " WHERE album_id=?";
				PreparedStatement prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, albumId + "");
				ResultSet result = prepareStatement.executeQuery();
				
				ArrayList items = new ArrayList ();
				while (result.next ()) {
					Item item = new Item (result.getInt ("id"), 2, 3);
					
					int length_seconds = Integer.parseInt (result.getString ("length_seconds"));
					int m = length_seconds / 60;
					int s = length_seconds % 60;
					String mmhh = (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
					
					item.setPublicValue (0, result.getString ("name"));
					item.setPublicValue (1, mmhh);
					item.setEditValue (0, result.getString ("name"));
					item.setEditValue (1, mmhh);
					item.setEditValue (2, result.getString ("label_id"));
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
			if (groupId == 0)
				albumId = 0;
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
			albumId = Integer.parseInt (value);
			if (groupId == 0)
				albumId = 0;
		}
		catch (Exception e) {
			System.out.println ("Error in set group: " + e);
		}
	}
	public SelectItem[] getAlbums () {
		return DBCore.getAlbums (groupId);
	}
	public SelectItem[] getLabels () {
		return DBCore.getLabels ();
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
    private String itemLengthSeconds;
    public String getEditLengthSeconds ()           {   return "";   }
    public void setEditLengthSeconds (String value) {
		String[] arr = Utils.escapeQuotes (value).split (":");
		if (arr.length == 2) {
			int m = Integer.parseInt (arr[0]);
			int s = Integer.parseInt (arr[1]);
			itemLengthSeconds = (m * 60 + s) + "";
		}
		else {
			itemLengthSeconds = Utils.escapeQuotes (value);
		}
	}
	private String itemLabelId;
    public String getEditLabelId ()           {   return "";   }
    public void setEditLabelId (String value) {   itemLabelId = Utils.escapeQuotes (value);  }
    
    public ModuleTrackBean() {
        itemId = 0;
		itemName = "";
		itemLengthSeconds = "";
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
			if (groupId > 0 && albumId > 0) {
				Connection conn = DBController.getConnection();
				String sql = " INSERT INTO " + tableName + 
							 " (name, length_seconds, album_id, label_id) VALUES (?, ?, ?, ?) ";
				PreparedStatement prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, itemName);
				prepareStatement.setString (2, itemLengthSeconds);
				prepareStatement.setString (3, albumId + "");
				prepareStatement.setString (4, itemLabelId);
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
						 " name=?, length_seconds=?, label_id=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemLengthSeconds);
			prepareStatement.setString (3, itemLabelId);
			prepareStatement.setString (4, itemId + "");
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
