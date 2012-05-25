package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import javax.faces.model.SelectItem;
import java.util.ArrayList;

@Named("module_group_prizes")
@SessionScoped
public class ModuleGroupPrizesBean implements Serializable {
	
	private final String tableName = "fp_group_prizes";
	private final String prizeTableName = "fp_prize";

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
			
			String sql = "SELECT * FROM " + tableName + " WHERE group_id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, groupId + "");
			ResultSet result = prepareStatement.executeQuery();
			
			sql = "SELECT * FROM " + prizeTableName + " WHERE id=?";
			PreparedStatement psmtSelectPrize = conn.prepareStatement (sql);
			
			ArrayList items = new ArrayList ();
			while (result.next ()) {
				Item item = new Item (result.getInt ("id"), 3, 1);
				
				psmtSelectPrize.setString (1, result.getInt ("prize_id") + "");
				ResultSet resultPrize = psmtSelectPrize.executeQuery();
				resultPrize.next ();
				
				item.setPublicValue (0, resultPrize.getString ("name"));
				item.setPublicValue (1, Utils.toNormDate (resultPrize.getString ("award_date")));
				item.setPublicValue (2, resultPrize.getString ("place"));
				
				item.setEditValue (0, result.getString ("prize_id"));
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
	public SelectItem[] getPrizes () {
		return DBCore.getPrizes ();
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

    private String itemPrizeId;
    public String getEditPrizeId ()           {   return "";   }
    public void setEditPrizeId (String value) {   itemPrizeId = Utils.escapeQuotes (value);  }
    
    public ModuleGroupPrizesBean() {
        itemId = 0;
		itemPrizeId = "";
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
							 " (prize_id, group_id) VALUES (?, ?) ";
				PreparedStatement prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, itemPrizeId);
				prepareStatement.setString (2, groupId + "");
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
						 " prize_id=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemPrizeId);
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
