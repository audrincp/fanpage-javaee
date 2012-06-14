package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_index")
@SessionScoped
public class PageIndexBean implements Serializable {
	public PageIndexBean () {
		currentPage = 1;
		countItemsInPage = 1;
        System.out.println ("dscnsdkcmklsm");
	}
	
	private int currentPage;
	private int countItemsInPage;
	public int getPage () { return currentPage; }
	public String setPage (String pageStr) { currentPage = Integer.parseInt (pageStr); return null; }
	public String setDiffPage (String pageDiffStr) { currentPage += Integer.parseInt (pageDiffStr); return null; }
	public String setPageCount () { currentPage = countPages; return null; }
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
			countItemsInPage = 5;
			countPages = (int)Math.ceil ((double)News.getCount () / (double)countItemsInPage);
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
}
