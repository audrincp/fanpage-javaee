package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import javax.faces.model.SelectItem;
import java.util.ArrayList;

@Named("module_group_performers")
@SessionScoped
public class ModuleGroupPerformersBean implements Serializable {
	
	private final String tableName = "fp_group_performers";
	private final String performerTableName = "fp_performer";

    public String[] getTableFields () {
		String[] arr = new String[3];
		arr[0] = "Имя";
		arr[1] = "Фамилия";
		arr[2] = "Период участия";
		return arr;
    }

    public Item[] getTableItems () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM " + tableName + " WHERE group_id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, groupId + "");
			ResultSet result = prepareStatement.executeQuery();
			
			sql = "SELECT * FROM " + performerTableName + " WHERE id=?";
			PreparedStatement psmtSelectPerformer = conn.prepareStatement (sql);
			
			ArrayList items = new ArrayList ();
			while (result.next ()) {
				Item item = new Item (result.getInt ("id"), 3, 2);
				
				psmtSelectPerformer.setString (1, result.getInt ("performer_id") + "");
				ResultSet resultPerformer = psmtSelectPerformer.executeQuery();
				resultPerformer.next ();
				
				item.setPublicValue (0, resultPerformer.getString ("name"));
				item.setPublicValue (1, resultPerformer.getString ("surname"));
				item.setPublicValue (2, result.getString ("period"));
				
				item.setEditValue (0, result.getString ("performer_id"));
				item.setEditValue (1, result.getString ("period"));
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
	public boolean getIsSelectGroup () {
		return groupId > 0;
	}
	public SelectItem[] getGroups () {
		return DBCore.getGroups ();
	}
	public SelectItem[] getPerformers () {
		return DBCore.getPerformers ();
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

    private String itemPerformerId;
    public String getEditPerformerId ()           {   return "";   }
    public void setEditPerformerId (String value) {   itemPerformerId = Utils.escapeQuotes (value);  }
    private String itemPeriod;
    public String getEditPeriod ()           {   return "";   }
    public void setEditPeriod (String value) {   itemPeriod = Utils.escapeQuotes (value);  }
    
    
    public ModuleGroupPerformersBean() {
        itemId = 0;
		itemPerformerId = "";
		itemPeriod = "";
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
							 " (period, performer_id, group_id) VALUES (?, ?, ?) ";
				PreparedStatement prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, itemPeriod);
				prepareStatement.setString (2, itemPerformerId);
				prepareStatement.setString (3, groupId + "");
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
						 " period=?, performer_id=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemPeriod);
			prepareStatement.setString (2, itemPerformerId);
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
