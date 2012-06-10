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

@Named("module_performer")
@SessionScoped
public class ModulePerformerBean implements Serializable {
	
	private final String tableName = "fp_performer";

    public String[] getTableFields () {
		String[] arr = new String[5];
		arr[0] = "Изображение";
		arr[1] = "Имя";
		arr[2] = "Фамилия";
		arr[3] = "Дата рождения";
		arr[4] = "Страна";
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
				Item item = new Item (result.getInt ("id"), 5, 10);
				item.setPublicValue (0, UploadFiles.getSrcForImage("performer", result.getInt ("id")));
				item.setPublicValue (1, result.getString ("name"));
				item.setPublicValue (2, result.getString ("surname"));
				item.setPublicValue (3, Utils.toNormDate (result.getString ("born_date")));
				item.setPublicValue (4, result.getString ("country"));
				item.setEditValue (0, "");
				item.setEditValue (1, result.getString ("name"));
				item.setEditValue (2, result.getString ("surname"));
				item.setEditValue (3, result.getString ("patronymic"));
				item.setEditValue (4, Utils.toNormDate (result.getString ("born_date")));
				item.setEditValue (5, result.getString ("sex"));
				item.setEditValue (6, result.getString ("born_place"));
				item.setEditValue (7, result.getString ("country"));
				item.setEditValue (8, Utils.toNormDate (result.getString ("death")));
				item.setEditValue (9, result.getString ("description"));
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
		String filepath = UploadFiles.getPathForImage("performer", id);
		System.out.println (filepath);
		return new File (filepath).exists ();
	}

    private String itemName;
    public String getEditName ()           {   return "";   }
    public void setEditName (String value) {   itemName = Utils.escapeQuotes (value);  }
    private String itemSurname;
    public String getEditSurname ()           {   return "";   }
    public void setEditSurname (String value) {   itemSurname = Utils.escapeQuotes (value);  }
    private String itemPatronymic;
    public String getEditPatronymic ()           {   return "";   }
    public void setEditPatronymic (String value) {   itemPatronymic = Utils.escapeQuotes (value);  }
    private String itemBornDate;
    public String getEditBornDate ()           {   return "";   }
    public void setEditBornDate (String value) {   itemBornDate = Utils.escapeQuotes (value);  }
    private String itemSex;
    public String getEditSex ()           {   return "";   }
    public void setEditSex (String value) {   itemSex = Utils.escapeQuotes (value);  }
    private String itemBornPlace;
    public String getEditBornPlace ()           {   return "";   }
    public void setEditBornPlace (String value) {   itemBornPlace = Utils.escapeQuotes (value);  }
    private String itemCountry;
    public String getEditCountry ()           {   return "";   }
    public void setEditCountry (String value) {   itemCountry = Utils.escapeQuotes (value);  }
    private String itemDeath;
    public String getEditDeath ()           {   return "";   }
    public void setEditDeath (String value) {   itemDeath = Utils.escapeQuotes (value);  }
    private String itemDescription;
    public String getEditDescription ()           {   return "";   }
    public void setEditDescription (String value) {   itemDescription = Utils.escapeQuotes (value);  }
    
	private boolean loadImage = false;
	public void uploadImage(FileUploadEvent event) {  
        FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);
        try {
            Utils.copyFile("performer", 0, event.getFile().getInputstream());
            loadImage = true;
        }
		catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public ModulePerformerBean() {
        itemId = 0;
		itemName = "";
		itemSurname = "";
		itemPatronymic = "";
		itemBornDate = "";
		itemSex = "";
		itemBornPlace = "";
		itemCountry = "";
		itemDeath = "";
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
						 " (name, surname, patronymic, born_date, sex, born_place, country, death, description) VALUES (?, ?, ?, to_date(?,'DD.MM.YYYY'), ?, ?, ?, to_date(?,'DD.MM.YYYY'), ?) ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemSurname);
			prepareStatement.setString (3, itemPatronymic);
			prepareStatement.setString (4, itemBornDate);
			prepareStatement.setString (5, itemSex);
			prepareStatement.setString (6, itemBornPlace);
			prepareStatement.setString (7, itemCountry);
			prepareStatement.setString (8, itemDeath);
			prepareStatement.setString (9, itemDescription);
			ResultSet result = prepareStatement.executeQuery();
			
			sql = "SELECT id FROM " + tableName + " WHERE name=? AND surname=? ORDER BY id DESC";
			prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemSurname);
			result = prepareStatement.executeQuery();
			result.next ();
			itemId = result.getInt ("id");
			
			conn.close ();
			
			if (loadImage) {
				Utils.copyLoadImageTo("performer", itemId);
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
						 " name=?, surname=?, patronymic=?, born_date=to_date(?,'DD.MM.YYYY'), sex=?, born_place=?, country=?, death=to_date(?,'DD.MM.YYYY'), description=? " + 
						 " WHERE id=? ";
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, itemName);
			prepareStatement.setString (2, itemSurname);
			prepareStatement.setString (3, itemPatronymic);
			prepareStatement.setString (4, itemBornDate);
			prepareStatement.setString (5, itemSex);
			prepareStatement.setString (6, itemBornPlace);
			prepareStatement.setString (7, itemCountry);
			prepareStatement.setString (8, itemDeath);
			prepareStatement.setString (9, itemDescription);
			prepareStatement.setString (10, itemId + "");
			ResultSet result = prepareStatement.executeQuery();
			conn.close ();
			
			if (loadImage) {
				Utils.copyLoadImageTo("performer", itemId);
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
