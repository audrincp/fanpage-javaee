package corejsf.site;

import java.beans.*;
import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.sql.*;
import corejsf.*;
import java.util.ArrayList;

@Named("page_search")
@SessionScoped
public class PageSearchBean implements Serializable {
	public PageSearchBean () {
	}
	
	private String searchField = "";
	public String getSearchField () {
		return searchField;
	}
	public void setSearchField (String value) {
		searchField = value;
	}
}
