package org.rm.core;

import java.lang.reflect.Field;
import java.sql.*;

import org.json.*;
/*
 * 数据库操作类，根据JAVABEAN自动生存SQL语句
 */
public class dbquery {
	private boolean IsMasterDB=false;//不是主数据库默认 
	private boolean IsPrintSQL=false; //是否打印SQL语句调试用
	public dbquery() {
	}
	/*
	 * 通过bean生存insert 预编译SQL
	 */
	public String GetInsertSQL(basebean bean) {
		try {
			bean.ClearFilterCurrent();
			Field[] fields = bean.getClass().getDeclaredFields();
			StringBuffer fieldsNames = new StringBuffer();
			StringBuffer fieldsValues = new StringBuffer();
			String returnvalue = "";
			bean.ClearFilterCurrent();
			for (int i = 0; i < fields.length; i++) {
				if (bean.FilterIndexOf(fields[i].getName()) == -1) {// 当等于-1的时候证明这个字段名字没有在过滤列表里面
					fieldsNames.append(fields[i].getName() + ",");// 得到字段名称
					fields[i].setAccessible(true); 
					if (fun.isintfloat(fields[i].getType().getName()))// 得到字段类型
						fieldsValues.append(fields[i].get(bean).toString()+",");// 字段值
					else
						fieldsValues.append("'"+fields[i].get(bean).toString()+"',");// 字段值
				}
			}
			// 去掉最后一位的,逗号
			fieldsNames.delete(fieldsNames.length() - 1, fieldsNames.length());
			fieldsValues.delete(fieldsValues.length() - 1, fieldsValues.length());
			returnvalue = "insert into " + bean.getTableName() + "(" + fieldsNames + ")" + "values(" + fieldsValues + ")";
			return returnvalue;
		} catch (Exception ex) {
			log.error(ex.toString());
			System.out.println(ex.toString());
			return "";
		}

	}
	 
	/*
	 * 通过Bean得到删除的SQL语句
	 */
	public String GetDeleteSQL(basebean bean) {
		try {
			bean.ClearFilterCurrent();
			Field[] fields = bean.getClass().getDeclaredFields();
			StringBuffer fieldsValues = new StringBuffer();
			String returnvalue = "";
			for (int i = 0; i < fields.length; i++) {
				if (bean.FilterIndexOf(fields[i].getName()) == -1) {// 当等于-1的时候证明这个字段名字没有在过滤列表里面
					fields[i].setAccessible(true); 
					if (fun.isintfloat(fields[i].getType().getName()))// 得到字段类型
						fieldsValues.append(fields[i].getName() + "="+fields[i].get(bean).toString()+" and ");
					else
						fieldsValues.append(fields[i].getName() + "='"+fields[i].get(bean).toString()+"' and ");
				}
			}
			// 去掉最后一位的,逗号
			fieldsValues.delete(fieldsValues.length() - 4, fieldsValues.length());
			returnvalue = "delete " + bean.getTableName() + " where " + fieldsValues.toString();
			return returnvalue;
		} catch (Exception ex) {
			log.error(ex.toString());
			return "";
		} finally{
			
		}
	}
	/*
	 * 通过Bean得到删除的SQL语句
	 */
	public String GetDeleteSQL(basebean bean,String aWhereSQL) {
	    	return "delete " + bean.getTableName() + " where " + aWhereSQL;
	}
	/*
	 * 通过Bean得到更新的SQL语句
	 */
	public String GetUpdateSQL(basebean bean,String aWhereSQL) {
		String returnvalue = "";
		try {
			bean.ClearFilterCurrent();
			Field[] fields = bean.getClass().getDeclaredFields();
			StringBuffer fieldsValues = new StringBuffer();
			for (int i = 0; i < fields.length; i++) {
				 
				if (bean.FilterIndexOf(fields[i].getName()) == -1) // 当等于-1的时候证明这个字段名字没有在过滤列表里面
				{
					fields[i].setAccessible(true); 
					if (fun.isintfloat(fields[i].getType().getName()))// 得到字段类型
						fieldsValues.append(fields[i].getName() + "="+fields[i].get(bean).toString()+",");
					else
						fieldsValues.append(fields[i].getName() + "='"+fields[i].get(bean).toString()+"',");
				}
			}
			// 去掉最后一位的,逗号
			fieldsValues.delete(fieldsValues.length() - 1, fieldsValues.length());
			returnvalue = "update " + bean.getTableName() + " set " + fieldsValues.toString()+" where "+aWhereSQL;
			return returnvalue;
		} catch (Exception ex) {
			log.error(ex.toString());
			returnvalue="";
		} finally{
			
		}
		return returnvalue;
	}
	 
	/*
	 * 返回jsonobject数据
	 */
	public JSONObject DBResultJsonObj(String sqlstr, String root) {
		Statement st = null;
		ResultSet rs = null;
		Connection con=null;
		JSONObject json = new JSONObject();
		try {
		    con =GetCon();
			con.setAutoCommit(true);
			st = con.createStatement();
			// 这里取数据
			rs = st.executeQuery(sqlstr);
			PrintSQL(sqlstr);
			// 得到列
			String filednames[] = null;
			int size = rs.getMetaData().getColumnCount();
			filednames = new String[size];
			for (int j = 1; j <= size; j++) {
				filednames[j - 1] = rs.getMetaData().getColumnName(j).toLowerCase();
			}

			JSONArray objs = new JSONArray();
			int i = 0;
			while (rs.next()) {
				JSONObject obj = new JSONObject();
				for (int m = 0; m < filednames.length; m++) {
					obj.put(filednames[m], fun.nil(rs.getString(filednames[m]), ""));
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
	/*
	 * 根据条件得到表中某个字段的行数
	 */
	public int DBResultTableCount(String TableName, String Field, String whereSQL) {
		Statement st = null;
		ResultSet rs = null;
		String SQLSTR=null;
		Connection con=null;
		int returnvalue = 0;
		if (whereSQL.equals("")){SQLSTR= "select count(" + Field + ")  from " + TableName;}
		else{ SQLSTR = "select count(" + Field + ")  from " + TableName + " where " + whereSQL;}
		try {
			con = GetCon();
			st = con.createStatement();
			rs = st.executeQuery(SQLSTR);
			PrintSQL(SQLSTR);
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
	 * 根据条件得到表中某个字段的值，只返回第一条数字
	 */
	public int DBResultTableValueOfInt(String TableName, String Field, String whereSQL) {
		Statement st = null;
		ResultSet rs = null;
		Connection con=null;
		int returnvalue = 0;
		String SQLSTR = "select " + Field + " from " + TableName + " where " + whereSQL;
		try {
		    con = GetCon();
			st = con.createStatement();
			rs = st.executeQuery(SQLSTR);
			PrintSQL(SQLSTR);
			if (rs.next())
				returnvalue = rs.getInt(Field);
		} catch (Exception ex) {
			log.error(getClass(), ex.toString());
		} finally {
			this.freecon(con, st, rs);
		}
		return returnvalue;

	}

	/*
	 * 根据条件得到表中某个字段的值，只返回第一条字符串的
	 */
	public String DBResultTableValue(String TableName, String Field, String whereSQL) {
		Statement st = null;
		ResultSet rs = null;
		Connection con=null;
		String returnvalue = "";
		String SQLSTR = "select " + Field + " from " + TableName + " where " + whereSQL;
		try {
		    con = GetCon();
			st = con.createStatement();
			rs = st.executeQuery(SQLSTR);
			PrintSQL(SQLSTR);
			if (rs.next())
				returnvalue = rs.getString(1);
		} catch (Exception ex) {
			log.error(getClass(), ex.toString());
		} finally {
			this.freecon(con, st, rs);
		}
		if (returnvalue==null){returnvalue="";}
		return returnvalue;

	}
 
	/**
	 * DBdelete(删除数据)
	 */
	public boolean DBdelete(basebean bean, String wheresql) {
		return DBExeSQL(GetDeleteSQL(bean, wheresql));
	}
	public boolean DBExeSQL(String  SQL) {
		Statement st = null;
		Connection con=null;
		boolean returnvalue = false;
		try {
		    con = GetCon();
			con.setAutoCommit(true);
			st = con.createStatement();
			st.execute(SQL);
			PrintSQL(SQL);
			returnvalue = true;
		} catch (Exception ex) {
			log.error(getClass(), ex.toString());
		} finally {
			this.freecon(con, st, null);
		}
		return returnvalue;
	}	 
		
	/**
	 * 
	 * DBupdate(更新数据)
	 * 
	 * @param wheresql
	 *            条件语句 如 where id=12
	 * @return
	 */
	public boolean DBupdate(basebean bean, String wheresql) {
		return DBExeSQL(this.GetUpdateSQL(bean, wheresql));
	}

	/**
	 * 
	 DBinsert(添加数据)
	 * 
	 * @param bean
	 * @return
	 */
	public boolean DBinsert(basebean bean) {
		 return DBExeSQL(GetInsertSQL(bean));
	}
	public int DBResultTableCount(String SQLSTR) {
	    Statement st = null;
	    ResultSet rs = null;
		Connection con = null;
		int countint=0;
		try {
			con = GetCon();
			st = con.createStatement();
			rs = st.executeQuery(SQLSTR);
			PrintSQL(SQLSTR);
			if (rs.next()){
			countint = rs.getInt(1);}
		} catch (SQLException e) {
			log.error(this.getClass(),e.toString());
		} finally {
			this.freecon(con, st, rs);
		}
		return countint;
	}
	
private void PrintSQL(String SQLSTR){
	if (IsPrintSQL==true){
	  log.debug(dbquery.class," SQL:"+SQLSTR);
	}
}
public void SetMasterDB(){//主数据
	IsMasterDB=true;
}
public void SetPrintSQL(){
	IsPrintSQL=true;
}

public void freecon(Connection con,Statement st,ResultSet rs){
	 
    try {
		if (rs != null) {
			rs.close();
			rs=null;
		}
	} catch (Exception ex) {
		 
	}
	try {
		if (st != null) {
			st.close();
			st=null;
		}
	} catch (Exception ex) {
	}
	try {
		if (con!=null){
		    con.close();
		    con=null;
		}
	} catch (SQLException e) {
	} 
	
}
public Connection GetCon(){
		return dbpool.getConMasterDB();  		
  }
}
