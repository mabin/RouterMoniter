/**
 * mabin
 */

package org.rm.core;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class basedao {
	public Connection con = null;
	public CallableStatement cstmt = null ;
	public ResultSet rs = null;
	
	public basedao(){
		this.con = dbpool.getConMasterDB();
	};

	public void freeCon(Connection con, CallableStatement cstmt, ResultSet rs){
	    try {
			if (rs != null) {
				rs.close();
				rs=null;
			}
		} catch (Exception ex) {
			 log.error(this.getClass(), "release resultset error!");
		}
		try {
			if (cstmt != null) {
				cstmt.close();
				cstmt=null;
			}
		} catch (Exception ex) {
			log.error(this.getClass(), "release statement error!");
		}
		try {
			if (con!=null){
			    con.close();
			    con=null;
			}
		} catch (SQLException e) {
			log.error(this.getClass(), "release connection error!");
		} 
		
		//log.debug(this.getClass(),"freeCon successfully");
	}
}
