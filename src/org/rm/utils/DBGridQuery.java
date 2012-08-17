package org.rm.utils;

import java.sql.*;

import org.json.*;
import org.rm.core.*;

public class DBGridQuery  extends dbquery {
	public DBGridQuery() {
	}
	public int DBGetTableCount(String TableName, String Field, String whereSQL) {
		Statement st = null;
		ResultSet rs = null;
		String SQLSTR=null;
		Connection con=null;
		int returnvalue = 0;
		if (whereSQL.equals("")){SQLSTR= "select count(" + Field + ")  from " + TableName;}
		else{ SQLSTR = "select count(" + Field + ")  from " + TableName + " where " + whereSQL;}
		try {
		    con =GetCon();
			st = con.createStatement();
			log.debug(this.getClass(), "得到SQL语句：" + SQLSTR);
			rs = st.executeQuery(SQLSTR);
			if (rs.next())
				returnvalue = rs.getInt(1);
		} catch (Exception ex) {
			log.error(getClass(), ex.toString());
		} finally {
			this.freecon(con, st, rs);
		}
		return returnvalue;
	}
	/*
	 * 得到表有多少行主要用于EXT.GRID分页用
	 */
	public int DBGetTableCount(String countsqlstr) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int countint = 0;
		try {
			con=GetCon();
			st = con.createStatement();
			rs = st.executeQuery(countsqlstr);
			rs.next();
			countint = rs.getInt(1);
		} catch (SQLException e) {
			log.error(this.getClass(), e.toString());
		} finally {
			this.freecon(con, st, rs);
			 
		}
		return countint;
	}

	/**
	 * 
	 * DBEXTGridResult(主要用于和EXT2.0 的grid取数据)
	 * @param dbsqlstr
	 *            查询数据
	 * @param root
	 *            Ext.data.JsonStore 对应的root
	 * @return JSONObject
	 */
	public JSONObject DBEXTGridResult(String dbsqlstr, String root) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		JSONObject json =null;
		try {
			json=new JSONObject(); 
			con = GetCon();
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
					// 这里要加入fun.nil函数 ：如果值为空的时候
					// JSONObject对象就不加入进去，JSONObject有潜在的问题
					obj.put(filednames[m].toLowerCase(), fun.nil(rs.getString(filednames[m]), ""));
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
			this.freecon(con, st, rs);
			 
		}
		return json;
	}	
	
	/**
	 * 
	 * DBEXTGridResult(主要用于和EXT2.0 的grid取数据)
	 * 
	 * @param countsqlstr
	 *            查询有多少行语句
	 * @param dbsqlstr
	 *            查询数据
	 * @param root
	 *            Ext.data.JsonStore 对应的root
	 * @return JSONObject
	 */
	public JSONObject DBEXTGridResult(int tablecount, String dbsqlstr, String root) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		JSONObject json = new JSONObject();
		try {
			json.put("success", "true");
			json.put("results", tablecount);
			if (tablecount == 0) {
				json.put(root, JSONObject.NULL);
				return json;
			}
			// 这里取数据
			con = GetCon();
			st = con.createStatement();
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
					// 这里要加入fun.nil函数 ：如果值为空的时候
					// JSONObject对象就不加入进去，JSONObject有潜在的问题
					obj.put(filednames[m].toLowerCase(), fun.nil(rs.getString(filednames[m]), ""));
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
}
