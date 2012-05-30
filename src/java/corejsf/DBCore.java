package corejsf;import java.sql.Connection;import javax.faces.model.SelectItem;import java.sql.PreparedStatement;import java.sql.ResultSet;import java.util.ArrayList;public class DBCore {	/**	 * названия таблиц	 */	private static final String groupTableName 		= "fp_group";	private static final String albumTableName 		= "fp_album";	private static final String performerTableName 	= "fp_performer";	private static final String prizeTableName 		= "fp_prize";	private static final String labelTableName 		= "fp_label";	private static final String trackTableName 		= "fp_track";	private static final String concertTableName 	= "fp_concert";		/**	 * создать массив SelectItem по двум ArrayList	 */	public static SelectItem[] convertArrayListToSelectItem (ArrayList value, ArrayList text) {		SelectItem[] items = new SelectItem[value.size ()];		for (int i = 0; i < value.size (); ++ i) {			items[i] = new SelectItem (value.get (i).toString (), text.get (i).toString ());		}		return items;	}		/**	 * достать список всех групп для <select>	 */	public static SelectItem[] getGroups () {		try {			Connection conn = DBController.getConnection();						String sql = "SELECT * FROM " + groupTableName;			PreparedStatement prepareStatement = conn.prepareStatement (sql);			ResultSet result = prepareStatement.executeQuery();						ArrayList groupsId = new ArrayList ();			ArrayList groupsName = new ArrayList ();						groupsId.add ("0");			groupsName.add ("Выберите группу");			while (result.next ()) {				groupsId.add (result.getInt ("id"));				groupsName.add (result.getString ("name"));			}						conn.close ();						return convertArrayListToSelectItem (groupsId, groupsName);		}		catch (Exception e) {			System.out.println ("Error in get list groups: " + e);		}		return null;	}		/**	 * достать список всех исполнителей для <select>	 */	public static SelectItem[] getPerformers () {		try {			Connection conn = DBController.getConnection();						String sql = "SELECT * FROM " + performerTableName + " ORDER BY surname, name";			PreparedStatement prepareStatement = conn.prepareStatement (sql);			ResultSet result = prepareStatement.executeQuery();						ArrayList performerId = new ArrayList ();			ArrayList performerName = new ArrayList ();						performerId.add ("0");			performerName.add ("Выберите исполнителя");			while (result.next ()) {				performerId.add (result.getInt ("id"));				performerName.add (result.getString ("surname") + " " + result.getString ("name"));			}						conn.close ();						return convertArrayListToSelectItem (performerId, performerName);		}		catch (Exception e) {			System.out.println ("Error in get list performers: " + e);		}		return null;	}		/**	 * достать список всех наград для <select>	 */	public static SelectItem[] getPrizes () {		try {			Connection conn = DBController.getConnection();						String sql = "SELECT * FROM " + prizeTableName;			PreparedStatement prepareStatement = conn.prepareStatement (sql);			ResultSet result = prepareStatement.executeQuery();						ArrayList prizesId = new ArrayList ();			ArrayList prizesName = new ArrayList ();						prizesId.add ("0");			prizesName.add ("Выберите награду");			while (result.next ()) {				prizesId.add (result.getInt ("id"));				prizesName.add (result.getString ("name") + " " + Utils.toNormDate (result.getString ("award_date")));			}						conn.close ();						return convertArrayListToSelectItem (prizesId, prizesName);		}		catch (Exception e) {			System.out.println ("Error in get list prizes: " + e);		}		return null;	}		/**	 * достать список всех альбомов группы для <select>	 */	public static SelectItem[] getAlbums (int groupId) {		try {			Connection conn = DBController.getConnection();						String sql = "SELECT * FROM " + albumTableName + " WHERE group_id=?";			PreparedStatement prepareStatement = conn.prepareStatement (sql);			prepareStatement.setString (1, groupId + "");			ResultSet result = prepareStatement.executeQuery();						ArrayList albumsId = new ArrayList ();			ArrayList albumsName = new ArrayList ();						albumsId.add ("0");			albumsName.add ("Выберите альбом");			while (result.next ()) {				albumsId.add (result.getInt ("id"));				albumsName.add (result.getString ("name"));			}						conn.close ();						return convertArrayListToSelectItem (albumsId, albumsName);		}		catch (Exception e) {			System.out.println ("Error in get list albums: " + e);		}		return null;	}		/**	 * достать список всех лейблов для <select>	 */	public static SelectItem[] getLabels () {		try {			Connection conn = DBController.getConnection();						String sql = "SELECT * FROM " + labelTableName;			PreparedStatement prepareStatement = conn.prepareStatement (sql);			ResultSet result = prepareStatement.executeQuery();						ArrayList labelsId = new ArrayList ();			ArrayList labelsName = new ArrayList ();						labelsId.add ("0");			labelsName.add ("Выберите лейбл");			while (result.next ()) {				labelsId.add (result.getInt ("id"));				labelsName.add (result.getString ("name"));			}						conn.close ();						return convertArrayListToSelectItem (labelsId, labelsName);		}		catch (Exception e) {			System.out.println ("Error in get list labels: " + e);		}		return null;	}		/**	 * достать список всех композиций альбома для <select>	 */	public static SelectItem[] getTracks (int albumId) {		try {			Connection conn = DBController.getConnection();						String sql = "SELECT * FROM " + trackTableName + " WHERE album_id=?";			PreparedStatement prepareStatement = conn.prepareStatement (sql);			prepareStatement.setString (1, albumId + "");			ResultSet result = prepareStatement.executeQuery();						ArrayList tracksId = new ArrayList ();			ArrayList tracksName = new ArrayList ();						tracksId.add ("0");			tracksName.add ("Выберите композицию");			while (result.next ()) {				tracksId.add (result.getInt ("id"));				tracksName.add (result.getString ("name"));			}						conn.close ();						return convertArrayListToSelectItem (tracksId, tracksName);		}		catch (Exception e) {			System.out.println ("Error in get list tracks: " + e);		}		return null;	}		/**	 * достать список всех концертов для <select>	 */	public static SelectItem[] getConcerts () {		try {			Connection conn = DBController.getConnection();						String sql = "SELECT * FROM " + concertTableName;			PreparedStatement prepareStatement = conn.prepareStatement (sql);			ResultSet result = prepareStatement.executeQuery();						ArrayList concertsId = new ArrayList ();			ArrayList concertsName = new ArrayList ();						concertsId.add ("0");			concertsName.add ("Выберите концерт");			while (result.next ()) {				concertsId.add (result.getInt ("id"));				concertsName.add (	Utils.toNormDate (result.getString ("concert_date")) + " " + 									result.getString ("country") + " " + result.getString ("place"));			}						conn.close ();						return convertArrayListToSelectItem (concertsId, concertsName);		}		catch (Exception e) {			System.out.println ("Error in get list concerts: " + e);		}		return null;	}		/**	 * достать список композиций группы	 * композиция представляется массивом строк	 * 0 - id, 1 - name, 2 - length, 3 - album_id, 4 - label_id	 */	/*public static String[][] getTracks (int groupId) {		try {			Connection conn = DBController.getConnection();						String sql = " SELECT * FROM " + trackTableName + " WHERE album_id IN ( " + 								" SELECT id FROM " + albumTableName + " WHERE group_id=? " + 							" ) ";			PreparedStatement prepareStatement = conn.prepareStatement (sql);			prepareStatement.setString (1, groupId + "");			ResultSet result = prepareStatement.executeQuery();						ArrayList tracks = new ArrayList ();			while (result.next ()) {				String[] res = 	{									result.getString ("id"), 									result.getString ("name"),									result.getString ("length_seconds"),									result.getString ("album_id"),									result.getString ("label_id"),								};				tracks.add (res);			}						conn.close ();						String[][] ret = new String[tracks.size ()][];			for (int i = 0; i < tracks.size (); ++ i) {				ret[i] = (String[])tracks.get (i);			}						return ret;		}		catch (Exception e) {			System.out.println ("Error in get list tracks for group: " + e);		}		return null;	}*/	/**	 * получить название композиции по id	 */	public static String getTrackName (String track_id) {		try {			Connection conn = DBController.getConnection();						String sql = "SELECT * FROM " + trackTableName + " WHERE id=?";			PreparedStatement prepareStatement = conn.prepareStatement (sql);			prepareStatement.setString (1, track_id);			ResultSet result = prepareStatement.executeQuery();						result.next ();			String ret = result.getString ("name");						conn.close ();						return ret;		}		catch (Exception e) {			System.out.println ("Error in get track name: " + e);		}		return null;	}	/**	 * перевод секунд в минуты:секунды	 */	public static String convertSecondsToMMSS (String seconds) {		try {			int length_seconds = Integer.parseInt (seconds);			int m = length_seconds / 60;			int s = length_seconds % 60;			return (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);		}		catch (Exception e) {			System.out.println ("Error in convertSecondsToMMSS: " + e);		}		return null;	}	/**	 * перевод минуты:секунды в секунды	 */	public static String convertMMSSToSeconds (String mmss) {		try {			String[] arr = Utils.escapeQuotes (mmss).split (":");			if (arr.length == 2) {				int m = Integer.parseInt (arr[0]);				int s = Integer.parseInt (arr[1]);				return (m * 60 + s) + "";			}			else {				return Utils.escapeQuotes (mmss);			}		}		catch (Exception e) {			System.out.println ("Error in convertMMSSToSeconds: " + e);		}		return null;	}}