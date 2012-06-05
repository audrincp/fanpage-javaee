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


@Named("module_news")
@SessionScoped
public class ModuleNewsBean implements Serializable {
	
	private final String tableName = "fp_news";

    public String[] getTableFields () {
		String[] arr = new String[4];
		arr[0] = "Заголовок";
		arr[1] = "Изображение";
		arr[2] = "Дата";
		arr[3] = "Анонс";
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
				item.setPublicValue (0, result.getString ("title"));
				item.setPublicValue (1, UploadFiles.getSrcForImage("news", result.getInt ("id")));
				item.setPublicValue (2, Utils.toNormDate (result.getString ("news_date")));
				item.setPublicValue (3, result.getString ("short"));
				item.setEditValue (0, result.getString ("title"));
				item.setEditValue (1, "");
				item.setEditValue (2, Utils.toNormDate (result.getString ("news_date")));
				item.setEditValue (3, result.getString ("short"));
				item.setEditValue (4, result.getString ("full"));
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

    private String itemTitle;
    public String getEditTitle ()           {   return "";   }
    public void setEditTitle (String value) {   itemTitle = Utils.escapeQuotes (value);  }
    private String itemNewsDate;
    public String getEditNewsDate ()           {   return "";   }
    public void setEditNewsDate (String value) {   itemNewsDate = Utils.escapeQuotes (value);  }
    private String itemShort;
    public String getEditShort ()           {   return "";   }
    public void setEditShort (String value) {   itemShort = Utils.escapeQuotes (value);  }
    private String itemFull;
    public String getEditFull ()           {   return "";   }
    public void setEditFull (String value) {   itemFull = Utils.escapeQuotes (value);  }
	
        private boolean loadImage = false;
	public void uploadImage(FileUploadEvent event) {  
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            Utils.copyFile("news", 0, event.getFile().getInputstream());
            loadImage = true;
        }
		catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public ModuleNewsBean() {
        itemId = 0;
		itemTitle = "";
		itemNewsDate = "";
		itemShort = "";
		itemFull = "";
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
                        
                        String sql;
                        PreparedStatement prepareStatement;
                        ResultSet result;
                        
			sql = " INSERT INTO " + tableName + 
						 " (title, news_date, short, full) VALUES (?, to_date(?,'DD.MM.YYYY'), ?, ?) ";
			prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemTitle);
			prepareStatement.setString (2, itemNewsDate);
			prepareStatement.setString (3, itemShort);
			prepareStatement.setString (4, itemFull);
			result = prepareStatement.executeQuery();
                        
                        sql = "SELECT id FROM " + tableName + " WHERE title=? AND news_date=to_date(?,'DD.MM.YYYY') ORDER BY id DESC";
                        prepareStatement = conn.prepareStatement (sql);
                        prepareStatement.setString (1, itemTitle);
			prepareStatement.setString (2, itemNewsDate);
                        result = prepareStatement.executeQuery();
                        result.next ();
                        itemId = result.getInt ("id");
                        
			conn.close ();
                        
                        if (loadImage) {
                            Utils.copyLoadImageTo("news", itemId);
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
						 " title=?, news_date=to_date(?,'DD.MM.YYYY'), short=?, full=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemTitle);
			prepareStatement.setString (2, itemNewsDate);
			prepareStatement.setString (3, itemShort);
			prepareStatement.setString (4, itemFull);
			prepareStatement.setString (5, itemId + "");
			ResultSet result = prepareStatement.executeQuery();
			conn.close ();
                        
                        if (loadImage) {
                            Utils.copyLoadImageTo("news", itemId);
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
