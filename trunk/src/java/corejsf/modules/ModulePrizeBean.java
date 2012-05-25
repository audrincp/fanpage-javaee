package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("module_prize")
@SessionScoped
public class ModulePrizeBean implements Serializable {
	
	private final String tableName = "fp_prize";

    public String[] getTableFields () {
		String[] arr = new String[3];
		arr[0] = "Название";
		arr[1] = "Дата награждения";
		arr[2] = "Место награждения";
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
				Item item = new Item (result.getInt ("id"), 3, 4);
				item.setPublicValue (0, result.getString ("name"));
				item.setPublicValue (1, Utils.toNormDate (result.getString ("award_date")));
				item.setPublicValue (2, result.getString ("place"));
				item.setEditValue (0, result.getString ("name"));
				item.setEditValue (1, Utils.toNormDate (result.getString ("award_date")));
				item.setEditValue (2, result.getString ("place"));
				item.setEditValue (3, result.getString ("description"));
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
    private String itemAwardDate;
    public String getEditAwardDate ()           {   return "";   }
    public void setEditAwardDate (String value) {   itemAwardDate = Utils.escapeQuotes (value);  }
    private String itemPlace;
    public String getEditPlace ()           {   return "";   }
    public void setEditPlace (String value) {   itemPlace = Utils.escapeQuotes (value);  }
    private String itemDescription;
    public String getEditDescription ()           {   return "";   }
    public void setEditDescription (String value) {   itemDescription = Utils.escapeQuotes (value);  }
    
    public ModulePrizeBean() {
        itemId = 0;
		itemName = "";
		itemAwardDate = "";
		itemPlace = "";
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
						 " (name, award_date, place, description) VALUES (?, to_date(?,'DD.MM.YYYY'), ?, ?) ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemAwardDate);
			prepareStatement.setString (3, itemPlace);
			prepareStatement.setString (4, itemDescription);
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
						 " name=?, award_date=to_date(?,'DD.MM.YYYY'), place=?, description=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemAwardDate);
			prepareStatement.setString (3, itemPlace);
			prepareStatement.setString (4, itemDescription);
			prepareStatement.setString (5, itemId + "");
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
