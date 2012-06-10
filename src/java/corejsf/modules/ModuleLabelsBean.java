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

@Named("module_labels")
@SessionScoped
public class ModuleLabelsBean implements Serializable {
	
	private final String tableName = "fp_label";

    public String[] getTableFields () {
		String[] arr = new String[4];
		arr[0] = "Изображение";
		arr[1] = "Название";
		arr[2] = "Страна";
		arr[3] = "Местоположение";
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
				Item item = new Item (result.getInt ("id"), 4, 5);
				item.setPublicValue (0, UploadFiles.getSrcForImage("labels", result.getInt ("id")));
				item.setPublicValue (1, result.getString ("name"));
				item.setPublicValue (2, result.getString ("country"));
				item.setPublicValue (3, result.getString ("place"));
				item.setEditValue (0, "");
				item.setEditValue (1, result.getString ("name"));
				item.setEditValue (2, result.getString ("country"));
				item.setEditValue (3, result.getString ("place"));
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

    private String itemCountry;
    public String getEditCountry ()           {   return "";   }
    public void setEditCountry (String value) {   itemCountry = Utils.escapeQuotes (value);  }

    private String itemPlace;
    public String getEditPlace ()           {   return "";   }
    public void setEditPlace (String value) {   itemPlace = Utils.escapeQuotes (value);  }

    private String itemDescription;
    public String getEditDescription ()           {   return "";   }
    public void setEditDescription (String value) {   itemDescription = Utils.escapeQuotes (value);  }
    
	private boolean loadImage = false;
	public void uploadImage(FileUploadEvent event) {  
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            Utils.copyFile("labels", 0, event.getFile().getInputstream());
            loadImage = true;
        }
		catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public boolean isImageExists (int id) {
		String filepath = UploadFiles.getPathForImage("labels", id);
		System.out.println (filepath);
		return new File (filepath).exists ();
	}
	
    public ModuleLabelsBean() {
        itemId = 0;
        itemName = "";
        itemCountry = "";
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
						 " (name, country, place, description) VALUES (?, ?, ?, ?) ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemCountry);
			prepareStatement.setString (3, itemPlace);
			prepareStatement.setString (4, itemDescription);
			ResultSet result = prepareStatement.executeQuery();
			
			sql = "SELECT id FROM " + tableName + " WHERE name=? AND country=? ORDER BY id DESC";
			prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemCountry);
			result = prepareStatement.executeQuery();
			result.next ();
			itemId = result.getInt ("id");
			
			conn.close ();
			
			if (loadImage) {
				Utils.copyLoadImageTo("labels", itemId);
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
						 " name=?, country=?, place=?, description=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemCountry);
			prepareStatement.setString (3, itemPlace);
			prepareStatement.setString (4, itemDescription);
			prepareStatement.setString (5, itemId + "");
			ResultSet result = prepareStatement.executeQuery();
			conn.close ();
			
			if (loadImage) {
				Utils.copyLoadImageTo("labels", itemId);
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
