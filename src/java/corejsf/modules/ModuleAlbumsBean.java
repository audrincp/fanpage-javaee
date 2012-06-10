package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;
import javax.faces.model.SelectItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

@Named("module_albums")
@SessionScoped
public class ModuleAlbumsBean implements Serializable {
	
	private final String tableName = "fp_album";
	private final String groupTableName = "fp_group";

    public String[] getTableFields () {
		String[] arr = new String[3];
		arr[0] = "Изображение";
		arr[1] = "Название";
		arr[2] = "Дата выхода";
		return arr;
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

    public Item[] getTableItems () {
		try {
			Connection conn = DBController.getConnection();
			
			String sql = "SELECT * FROM " + tableName + " WHERE group_id=?";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, groupId + "");
			ResultSet result = prepareStatement.executeQuery();
			
			ArrayList items = new ArrayList ();
			while (result.next ()) {
				Item item = new Item (result.getInt ("id"), 3, 4);
				item.setPublicValue (0, UploadFiles.getSrcForImage("albums", result.getInt ("id")));
				item.setPublicValue (1, result.getString ("name"));
				item.setPublicValue (2, Utils.toNormDate (result.getString ("released")));
				item.setEditValue (0, "");
				item.setEditValue (1, result.getString ("name"));
				item.setEditValue (2, Utils.toNormDate (result.getString ("released")));
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
    private String itemReleased;
    public String getEditReleased ()           {   return "";   }
    public void setEditReleased (String value) {   itemReleased = Utils.escapeQuotes (value);  }
    private String itemDescription;
    public String getEditDescription ()           {   return "";   }
    public void setEditDescription (String value) {   itemDescription = Utils.escapeQuotes (value);  }
	
	private boolean loadImage = false;
	public void uploadImage(FileUploadEvent event) {  
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            Utils.copyFile("albums", 0, event.getFile().getInputstream());
            loadImage = true;
        }
		catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public boolean isImageExists (int id) {
		String filepath = UploadFiles.getPathForImage("albums", id);
		System.out.println (filepath);
		return new File (filepath).exists ();
	}
	
    public ModuleAlbumsBean() {
        itemId = 0;
		itemName = "";
		itemReleased = "";
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
			if (groupId > 0) {
				Connection conn = DBController.getConnection();
				String sql = " INSERT INTO " + tableName + 
							 " (group_id, name, released, description) VALUES (?, ?, to_date(?,'DD.MM.YYYY'), ?) ";
				PreparedStatement prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, groupId + "");
				prepareStatement.setString (2, itemName);
				prepareStatement.setString (3, itemReleased);
				prepareStatement.setString (4, itemDescription);
				ResultSet result = prepareStatement.executeQuery();
				
				sql = "SELECT id FROM " + tableName + " WHERE name=? ORDER BY id DESC";
				prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, itemName);
				result = prepareStatement.executeQuery();
				result.next ();
				itemId = result.getInt ("id");
				
				conn.close ();
				
				if (loadImage) {
					Utils.copyLoadImageTo("albums", itemId);
					loadImage = false;
				}
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
						 " name=?, released=to_date(?,'DD.MM.YYYY'), description=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemReleased);
			prepareStatement.setString (3, itemDescription);
			prepareStatement.setString (4, itemId + "");
			ResultSet result = prepareStatement.executeQuery();
			conn.close ();
			
			if (loadImage) {
				Utils.copyLoadImageTo("albums", itemId);
				loadImage = false;
			}
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
