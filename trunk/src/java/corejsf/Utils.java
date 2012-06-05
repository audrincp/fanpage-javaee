package corejsf;

import java.io.*;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;

/**
 * класс со всякими функциями
 */
public class Utils {
	/**
	 * перевод даты из yyyy-mm-dd в dd.mm.yyyy
	 */
	public static String toNormDate (String date) {
		if (date == null || date.length () < 10)
			return "";
		String[] arr = date.split (" ");
		if (arr.length != 2)
			return "";
		String[] arr2 = arr[0].split ("-");
		if (arr2.length != 3)
			return "";
		return arr2[2] + "." + arr2[1] + "." + arr2[0];
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
	/**
	 * копирование файла из InputStream на диск
	 */
	public static void copyFile(String folder, int id, InputStream in) {
	   try {
                        File f = new File(UploadFiles.getPathForImage(folder, id));
                        System.out.println (f.getAbsolutePath());
			OutputStream out = new FileOutputStream(f);
		 
			int read = 0;
			byte[] bytes = new byte[1024];
		 
			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		 
			in.close();
			out.flush();
			out.close();
		 
			System.out.println("New file created!");
		} 
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
    }
        /**
	 * копирование изображений
	 */
	public static void copyLoadImageTo(String folder, int id) {
	   try {
                        File f = new File(UploadFiles.getPathForImage(folder, 0));
                        f.renameTo (new File(UploadFiles.getPathForImage(folder, id)));
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
    }
}
