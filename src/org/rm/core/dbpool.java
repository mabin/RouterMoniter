package org.rm.core;

import java.sql.*;

import org.rm.core.c3p0poolManager;

/**
 * 
 * 得到一个连接，这里可以换成其它更好的连接池
 * 
 */

public class dbpool {
	
	public dbpool() {
	}
	public static Connection getConMasterDB() {//主数据库
		try {
			return c3p0poolManager.getInstance().getConnection(1);

		} catch (Exception ex) {
			log.error("dbpool.getConnection:" + ex.toString());
		}
		return null;
	}

 
}
 