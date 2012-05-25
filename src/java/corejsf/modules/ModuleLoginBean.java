package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("module_login")
@SessionScoped
public class ModuleLoginBean implements Serializable {
	
	private final String tableName = "fp_login";

    public String[] getTableFields () {
		String[] arr = new String[2];
		arr[0] = "Логин";
		arr[1] = "Права";
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
				Item item = new Item (result.getInt ("id"), 2, 2);
				item.setPublicValue (0, result.getString ("login"));
				item.setPublicValue (1, result.getString ("is_admin"));
				item.setEditValue (0, result.getString ("login"));
				item.setEditValue (1, result.getString ("is_admin"));
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

    private String itemLogin;
    public String getEditLogin ()           {   return "";   }
    public void setEditLogin (String value) {   itemLogin = Utils.escapeQuotes (value);  }
    private String itemIsAdmin;
    public String getEditIsAdmin ()           {   return "";   }
    public void setEditIsAdmin (String value) {   itemIsAdmin = Utils.escapeQuotes (value);  }
    
    public ModuleLoginBean() {
        itemId = 0;
		itemLogin = "";
		itemIsAdmin = "";
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
						 " (login, is_admin) VALUES (?, ?) ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemLogin);
			prepareStatement.setString (2, itemIsAdmin);
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
						 " login=?, is_admin=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemLogin);
			prepareStatement.setString (2, itemIsAdmin);
			prepareStatement.setString (3, itemId + "");
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
