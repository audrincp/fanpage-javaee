package corejsf.modules;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

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

@Named("module_groups")
@SessionScoped
public class ModuleGroupsBean implements Serializable {
	
	private final String tableName = "fp_group";

    public String[] getTableFields () {
		String[] arr = new String[4];
		arr[0] = "Изображение";
		arr[1] = "Название";
		arr[2] = "Жанр";
		arr[3] = "Дата создания";
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
				Item item = new Item (result.getInt ("id"), 4, 6);
				item.setPublicValue (0, UploadFiles.getSrcForImage("groups", result.getInt ("id")));
				item.setPublicValue (1, result.getString ("name"));
				item.setPublicValue (2, result.getString ("genre"));
				item.setPublicValue (3, Utils.toNormDate (result.getString ("creation_date")));
				item.setEditValue (0, "");
				item.setEditValue (1, result.getString ("name"));
				item.setEditValue (2, result.getString ("genre"));
				item.setEditValue (3, Utils.toNormDate (result.getString ("creation_date")));
				item.setEditValue (4, Utils.toNormDate (result.getString ("decay_date")));
				item.setEditValue (5, result.getString ("description"));
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
	
	public boolean isImageExists (int id) {
		String filepath = UploadFiles.getPathForImage("groups", id);
		System.out.println (filepath);
		return new File (filepath).exists ();
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
    
	private boolean loadImage = false;
	public void uploadImage(FileUploadEvent event) {  
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            Utils.copyFile("groups", 0, event.getFile().getInputstream());
            loadImage = true;
        }
		catch (IOException e) {
            e.printStackTrace();
        }
    }
	
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
			
			sql = "SELECT id FROM " + tableName + " WHERE name=? AND genre=? ORDER BY id DESC";
			prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemGenre);
			result = prepareStatement.executeQuery();
			result.next ();
			itemId = result.getInt ("id");
			
			conn.close ();
			
			if (loadImage) {
				Utils.copyLoadImageTo("groups", itemId);
				loadImage = false;
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
			
			if (loadImage) {
				Utils.copyLoadImageTo("groups", itemId);
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
