package corejsf;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;

@Named("login")
@SessionScoped
public class LoginBean implements Serializable {
    
    private String login;
    public String getLogin ()           {   return login;   }
    public void setLogin (String value) {   login = value;  }
    
    private String password;
    public String getPassword ()            {   return "";          }
    public void setPassword (String value)  {   password = value;   }
    
    private boolean isLogged;
    public boolean getIsLogged ()   {   return isLogged;    }
	
	private boolean isAdminLogged;
    public boolean getIsAdminLogged ()   {   return isAdminLogged;    }
    
    private String currentLogin;
    public String getCurrentLogin () {  return currentLogin;    }
    
    public LoginBean() {
        login = "";
        password = "";
        isLogged = false;
        currentLogin = "";
		isAdminLogged = false;
    }
    
    private int isCorrectLoginPassword () {
        // ищем в базе данных логин и пароль
        try {
            Connection conn = DBController.getConnection();
            
			String sql = "SELECT COUNT(id) AS cnt_id FROM fp_login WHERE login=? AND password=?";
            PreparedStatement prepareStatement = conn.prepareStatement (sql);
            prepareStatement.setString (1, login);
            prepareStatement.setString (2, DBController.md5 (password));
			
            ResultSet result = prepareStatement.executeQuery();
            result.next ();
            int count_id = Integer.parseInt (result.getString ("cnt_id"));
			int ret = 0;
			if (count_id > 0) {
				ret = 1;
				
				sql = "SELECT is_admin FROM fp_login WHERE login=? AND password=?";
				prepareStatement = conn.prepareStatement (sql);
				prepareStatement.setString (1, login);
				prepareStatement.setString (2, DBController.md5 (password));
				
				result = prepareStatement.executeQuery();
				result.next ();
				if (result.getInt ("is_admin") > 0) {
					ret = 2;
				}
			}
			
            conn.close ();
            return ret;
        }
        catch (Exception e) {
            System.out.println ("Ошибка при обращении к базе данных: " + e);
        }
        return 0;
    }
    public String logon () {
		int flag = isCorrectLoginPassword ();
        if (flag > 0){
            currentLogin = login;
            isLogged = true;
			isAdminLogged = (flag > 1);
            return "./index.xhtml";
        }
        return null;
    }
    public boolean isError () {
        return isCorrectLoginPassword() == 0 && 
                login.length() > 0 && password.length () > 0;
    }
    
}
