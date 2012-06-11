package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("site")
@SessionScoped
public class SiteBean implements Serializable {
	public SiteBean () {
		currentPage = 1;
		countItemsInPage = 1;
	}
	
	private int currentPage;
	private int countItemsInPage;
	public int getPage () { return currentPage; }
	public void setPage (String pageStr) { currentPage = Integer.parseInt (pageStr); }
	public void setDiffPage (String pageDiffStr) { currentPage += Integer.parseInt (pageDiffStr); }
	public void setPageCount () { currentPage = countPages; }
	private int getFirstItemInPage () { return (currentPage - 1) * countItemsInPage + 1; }
	public int getPageNumber (int add) {
		if (currentPage + add > getCountPages ())
			return getCountPages ();
		if (currentPage + add < 1)
			return 1;
		return currentPage + add;
	}
	public boolean getIsPageExists (int add) {
		return 1 <= currentPage + add && currentPage + add <= getCountPages ();
	}
	private int countPages;
	public int getCountPages () {
		if (countPages < 1)
			countPages = 1;
		return countPages;
	}
	
	private ResultSet getResultForPagingQuery (Connection conn, String sql) {
		try {
			PreparedStatement prepareStatement = conn.prepareStatement (sql);
			prepareStatement.setString (1, getFirstItemInPage () + "");
			prepareStatement.setString (2, (getFirstItemInPage () + countItemsInPage - 1) + "");
			System.out.println (getFirstItemInPage () + " " + (getFirstItemInPage () + countItemsInPage - 1));
			ResultSet result = prepareStatement.executeQuery();
			return result;
		}
		catch (Exception e) {
			System.out.println ("Error in [getResultForPagingQuery] width query [" + sql + "]: " + e);
		}
		return null;
	}
	private void setCountNews () {
		try {
			Connection conn = DBController.getConnection ();
			
			countItemsInPage = 5;
			String sql = "SELECT COUNT(*) FROM fp_news";
			PreparedStatement ps = conn.prepareStatement (sql);
			ResultSet result = ps.executeQuery ();
			result.next ();
			countPages = (int)Math.ceil ((double)result.getInt (1) / (double)countItemsInPage);
			
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in setCountNews:" + e);
		}
	}
	public News[] getNews () {
		setCountNews ();
		
		ArrayList list = new ArrayList ();
		
		String sql = "SELECT * FROM (SELECT fp_news.*, ROW_NUMBER() OVER (ORDER BY news_date DESC) RN FROM fp_news) WHERE ? <=RN AND RN <= ?";
		try {
			Connection conn = DBController.getConnection();
			ResultSet result = getResultForPagingQuery (conn, sql);
			while (result.next ())
				list.add (new News (result));
			conn.close ();
		}
		catch (Exception e) {
			System.out.println ("Error in getNews: " + e);
			return null;
		}
		
		System.out.println ("Current page " + getPage ());
		
		News array[] = new News[list.size ()];
		int i = 0;
		for (Object item : list)
			array[i ++] = (News)item;
		return array;
	}
	
	private String searchField;
	public String getSearchField () { return searchField; }
	public void setSearchField (String value) { searchField = value; }
}
