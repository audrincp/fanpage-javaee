package corejsf;

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
}
