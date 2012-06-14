package corejsf.site;

import java.sql.*;
import corejsf.*;

public class SearchResult {
	private String catalogTitle;
	private String title;
	
	public SearchResult (String catalogTitle, String title) {
		this.catalogTitle = catalogTitle;
		this.title = title;
	}
	
	public String getCatalogTitle () {
		return catalogTitle;
	}
	public String getTitle () {
		return title;
	}
}