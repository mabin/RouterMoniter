package org.rm.utils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rm.core.dbpool;
import org.rm.core.dbquery;
import org.rm.core.fun;
import org.rm.core.log;
public class DBComboBox  extends dbquery {
	public int DBGetTableCount(String countsqlstr) {
		dbquery db=null;
	    Statement st = null;
	    ResultSet rs = null;
		Connection con = null;
		int countint=0;
		try {
		 
			db=new dbquery();
        	db.SetMasterDB();
        	con = db.GetCon();
			st = con.createStatement();
			rs = st.executeQuery(countsqlstr);
			rs.next();
			countint = rs.getInt(1);
		} catch (SQLException e) {
			log.error(this.getClass(),e.toString());
		} finally {
			db.freecon(con, st,null);
        	db=null;
		}
		return countint;
	}
	public  JSONObject DBEXTComboxBoxResult(String TablelistCodes,String TablelistNames,String Code,String Name, String root) {
		JSONObject json = new JSONObject();
		String []TablelistCodesArray=TablelistCodes.split(",");
		String []TablelistNamesArray=TablelistNames.split(",");
		int tablecount=0;
		try {
			tablecount=TablelistCodesArray.length;
			json.put("success", "true");
			json.put("results", tablecount);
			if (tablecount == 0) {
				json.put(root, JSONObject.NULL);
				return json;
			}
			JSONArray objs = new JSONArray();
			
			int i = 0;
			for (int j = 0; j < TablelistCodesArray.length; j++) {
				JSONObject obj = new JSONObject();
				
				obj.put(Code,fun.nil(TablelistCodesArray[j], "") );
				obj.put(Name,fun.nil(TablelistNamesArray[j], "") );
				objs.put(i++, obj);
			}
			json.put(root, objs);
			return json;
		} catch (Exception e) {
			log.error(getClass(), e.toString());
		} finally {
			 
		}
		return null;
	}
	public  JSONObject DBEXTComboxBoxResult(int tablecount, String dbsqlstr, String root) {
		JSONObject json = new JSONObject();
		Statement st = null;
	    ResultSet rs = null;
		Connection con = null;
		try {
			json.put("success", "true");
			json.put("results", tablecount);
			if (tablecount == 0) {
				json.put(root, JSONObject.NULL);
				return json;
			}
			con=dbpool.getConMasterDB();
			st=con.createStatement();
			// 这里取数据
			rs = st.executeQuery(dbsqlstr);
			 
			// 得到列
			String filednames[] = null;
			int size = rs.getMetaData().getColumnCount();
			filednames = new String[size];
			for (int j = 1; j <= size; j++) {
				filednames[j - 1] = rs.getMetaData().getColumnName(j);
			}
			JSONArray objs = new JSONArray();
			int i = 0;
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				for (int m = 0; m < filednames.length; m++) {
					obj.put(filednames[m].toLowerCase(),fun.nil(rs.getString(filednames[m]), "") );
				}
				objs.put(i++, obj);
			}
			json.put(root, objs);
		} catch (Exception e) {
			log.error(getClass(), e.toString());
		} finally {
			this.freecon(con, st, rs);
		}
		return json;
	}

	public  JSONObject DBEXTComboxBoxResult(String dbsqlstr, String root) {
		dbquery db=null;
		JSONObject json = null;
		Connection con=null;
		Statement st=null;
		ResultSet rs = null;
		try {
			json=new JSONObject(); 
			db=new dbquery();
        	db.SetMasterDB();
        	con = db.GetCon();
			st=con.createStatement();
			rs = st.executeQuery(dbsqlstr);
			// 得到列
			String filednames[] = null;
			int size = rs.getMetaData().getColumnCount();
			filednames = new String[size];
			for (int j = 1; j <= size; j++) {
				filednames[j - 1] = rs.getMetaData().getColumnName(j);
			}
			JSONArray objs = new JSONArray();
			int i = 0;
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				for (int m = 0; m < filednames.length; m++) {
					obj.put(filednames[m].toLowerCase(),fun.nil(rs.getString(filednames[m]), "") );
				}
				objs.put(i++, obj);
			}
			if (i!=0){
			   json.put("success","true");
			   json.put("results",i);
			   json.put(root, objs);
			}else 
			{
				json.put(root, JSONObject.NULL);
			}
		 
		} catch (Exception e) {
			log.error(getClass(), e.toString());
		} finally {

			db.freecon(con, st,null);
		    db=null;
		}
		return json;
	}
}
