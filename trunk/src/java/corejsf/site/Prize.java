package corejsf.site;

import java.sql.*;
import corejsf.*;
import java.io.*;
import java.util.*;

public class Prize {
	private int id;
	private String name;
	private String date;
	private String place;
	
	public Prize (ResultSet result) {
		try {
			id = result.getInt ("id");
			name = result.getString ("name");
			date = Utils.toNormDate (result.getString ("award_date"));
			place = result.getString ("place");
		}
		catch (Exception e) {
			System.out.println ("Error in new Prize: " + e);
		}
	}
	
	public String getName () {
		return name;
	}
	public String getDate () {
		return date;
	}
	public String getPlace () {
		return place;
	}
}