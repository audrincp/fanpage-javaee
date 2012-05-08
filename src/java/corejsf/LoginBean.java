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
    
    private String currentLogin;
    public String getCurrentLogin () {  return currentLogin;    }
    
    public LoginBean() {
        login = "";
        password = "";
        isLogged = false;
        currentLogin = "";
    }
    
    private boolean isCorrectLoginPassword () {
        // ищем в базе данных логин и пароль
        try {
            Connection conn = DBController.getConnection();
            
			String sql = "SELECT COUNT(id) AS cnt_id FROM login WHERE login=? AND password=?";
            PreparedStatement prepareStatement = conn.prepareStatement (sql);
            prepareStatement.setString (1, login);
            prepareStatement.setString (2, DBController.md5 (password));
			
            ResultSet result = prepareStatement.executeQuery();
            result.next ();
            int count_id = Integer.parseInt (result.getString ("cnt_id"));
			
            conn.close ();
            return (count_id > 0);
        }
        catch (Exception e) {
            System.out.println ("Ошибка при обращении к базе данных: " + e);
        }
        return false;
    }
    public String logon () {
        if (isCorrectLoginPassword ()){
            currentLogin = login;
            isLogged = true;
            return "./index.xhtml";
        }
        return null;
    }
    public boolean isError () {
        return ! isCorrectLoginPassword() && 
                login.length() > 0 && password.length () > 0;
    }
    
}
