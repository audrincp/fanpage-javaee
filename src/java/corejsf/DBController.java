package corejsf;
import com.sun.xml.rpc.processor.modeler.j2ee.xml.javaIdentifierType;
import java.sql.*;
import javax.sql.*;
import java.util.Locale;
import javax.naming.*;
import java.security.*;
/**
 * класс предназначен для получения доступа к базе данных
 */
public class DBController {
    /**
     * открыть соединение с базой данных
     */
    public static Connection getConnection () {
        try {
            /*InitialContext initialContext = new InitialContext();
            DataSource dataSource = (DataSource) initialContext.lookup("jdbc/TestOracle");
            Locale.setDefault(Locale.ENGLISH);
            return dataSource.getConnection();*/
            String url = "jdbc:oracle:thin:@localhost:1521:XE";
            String login = "system";
            String password = "123456";
            String drive = "oracle.jdbc.OracleDriver";
            Class.forName(drive);

            Locale.setDefault(Locale.ENGLISH);
            return DriverManager.getConnection(url, login, password);
        }
        catch (Exception e) {
            System.out.println("Ошибка при соединении с базой данных: " + e);
        }
        return null;
    }
    /**
     * md5 хэш строки
     */
    public static String md5 (String str) {
        try {
            MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(str.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } 
			catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
	/**
	 * экранирование кавычек и переводов строк
	 */
	public static String escapeQuotes (String s) {
		StringBuffer str = new StringBuffer ();
		for (int i = 0; i < s.length (); ++ i) {
			char c = s.charAt (i);
			if (c == '\"')
				str.append ("&quot;");
			else if (c == '\'')
				str.append ("&#39");
			else if (c == '\n')
				str.append ("<br />");
			else if (c != '\r')
				str.append (c);
		}
		return str.toString ();
	}
}
