package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("module_groups")
@SessionScoped
public class ModuleGroupsBean implements Serializable {
	
	private final String tableName = "fp_groups";

    public String[] getTableFields () {
		String[] arr = new String[3];
		arr[0] = "Название";
		arr[1] = "Жанр";
		arr[2] = "Дата создания";
		return arr;
    }

    public Item[] getTableItems () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM " + tableName;
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			ResultSet result = prepareStatement.executeQuery();
			
			ArrayList items = new ArrayList ();
			while (result.next ()) {
				Item item = new Item (result.getInt ("id"), 3, 5);
				item.setPublicValue (0, result.getString ("name"));
				item.setPublicValue (1, result.getString ("genre"));
				item.setPublicValue (2, Utils.toNormDate (result.getString ("creation_date")));
				item.setEditValue (0, result.getString ("name"));
				item.setEditValue (1, result.getString ("genre"));
				item.setEditValue (2, Utils.toNormDate (result.getString ("creation_date")));
				item.setEditValue (3, Utils.toNormDate (result.getString ("decay_date")));
				item.setEditValue (4, result.getString ("description"));
				items.add (item);
			}
			
			conn.close ();
			
			return Item.ObjectsToItems (items.toArray ());
		}
		catch (Exception e) {
			System.out.println ("Error in get table items: " + e);
		}
		return null;
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
    private String itemGenre;
    public String getEditGenre ()           {   return "";   }
    public void setEditGenre (String value) {   itemGenre = Utils.escapeQuotes (value);  }
    private String itemCreationDate;
    public String getEditCreationDate ()           {   return "";   }
    public void setEditCreationDate (String value) {   itemCreationDate = Utils.escapeQuotes (value);  }
    private String itemDecayDate;
    public String getEditDecayDate ()           {   return "";   }
    public void setEditDecayDate (String value) {   itemDecayDate = Utils.escapeQuotes (value);  }
    private String itemDescription;
    public String getEditDescription ()           {   return "";   }
    public void setEditDescription (String value) {   itemDescription = Utils.escapeQuotes (value);  }
    
    public ModuleGroupsBean() {
        itemId = 0;
		itemName = "";
		itemGenre = "";
		itemCreationDate = "";
		itemDecayDate = "";
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
						 " (name, genre, creation_date, decay_date, description) VALUES (?, ?, to_date(?,'DD.MM.YYYY'), to_date(?,'DD.MM.YYYY'), ?) ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemGenre);
			prepareStatement.setString (3, itemCreationDate);
			prepareStatement.setString (4, itemDecayDate);
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
						 " name=?, genre=?, creation_date=to_date(?,'DD.MM.YYYY'), decay_date=to_date(?,'DD.MM.YYYY'), description=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemGenre);
			prepareStatement.setString (3, itemCreationDate);
			prepareStatement.setString (4, itemDecayDate);
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
