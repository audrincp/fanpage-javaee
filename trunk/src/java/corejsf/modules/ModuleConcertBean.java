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

@Named("module_concert")
@SessionScoped
public class ModuleConcertBean implements Serializable {
	
	private final String tableName = "fp_concert";

    public String[] getTableFields () {
		String[] arr = new String[4];
		arr[0] = "Изображение";
		arr[1] = "Страна";
		arr[2] = "Место проведения";
		arr[3] = "Дата проведения";
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
				item.setPublicValue (0, UploadFiles.getSrcForImage("concert", result.getInt ("id")));
				item.setPublicValue (1, result.getString ("country"));
				item.setPublicValue (2, result.getString ("place"));
				item.setPublicValue (3, Utils.toNormDate (result.getString ("concert_date")));
				item.setEditValue (0, "");
				item.setEditValue (1, result.getString ("country"));
				item.setEditValue (2, result.getString ("place"));
				item.setEditValue (3, Utils.toNormDate (result.getString ("concert_date")));
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
	
	public boolean isImageExists (int id) {
		String filepath = UploadFiles.getPathForImage("concert", id);
		System.out.println (filepath);
		return new File (filepath).exists ();
	}

    private String itemCountry;
    public String getEditCountry ()           {   return "";   }
    public void setEditCountry (String value) {   itemCountry = Utils.escapeQuotes (value);  }
    private String itemPlace;
    public String getEditPlace ()           {   return "";   }
    public void setEditPlace (String value) {   itemPlace = Utils.escapeQuotes (value);  }
    private String itemConcertDate;
    public String getEditConcertDate ()           {   return "";   }
    public void setEditConcertDate (String value) {   itemConcertDate = Utils.escapeQuotes (value);  }
    private String itemDescription;
    public String getEditDescription ()           {   return "";   }
    public void setEditDescription (String value) {   itemDescription = Utils.escapeQuotes (value);  }
    
	private boolean loadImage = false;
	public void uploadImage(FileUploadEvent event) {  
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            Utils.copyFile("concert", 0, event.getFile().getInputstream());
            loadImage = true;
        }
		catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public ModuleConcertBean() {
        itemId = 0;
		itemCountry = "";
		itemPlace = "";
		itemConcertDate = "";
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
						 " (country, place, concert_date, description) VALUES (?, ?, to_date(?,'DD.MM.YYYY'), ?) ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemCountry);
			prepareStatement.setString (2, itemPlace);
			prepareStatement.setString (3, itemConcertDate);
			prepareStatement.setString (4, itemDescription);
			ResultSet result = prepareStatement.executeQuery();
			
			sql = "SELECT id FROM " + tableName + " WHERE country=? AND place=? ORDER BY id DESC";
			prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemCountry);
			prepareStatement.setString (2, itemPlace);
			result = prepareStatement.executeQuery();
			result.next ();
			itemId = result.getInt ("id");
			
			conn.close ();
			
			if (loadImage) {
				Utils.copyLoadImageTo("concert", itemId);
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
						 " country=?, place=?, concert_date=to_date(?,'DD.MM.YYYY'), description=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemCountry);
			prepareStatement.setString (2, itemPlace);
			prepareStatement.setString (3, itemConcertDate);
			prepareStatement.setString (4, itemDescription);
			prepareStatement.setString (5, itemId + "");
			ResultSet result = prepareStatement.executeQuery();
			conn.close ();
			
			if (loadImage) {
				Utils.copyLoadImageTo("concert", itemId);
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
