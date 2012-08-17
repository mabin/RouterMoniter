package org.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.rm.bean.MetaDevice;
import org.rm.core.basedao;
import org.rm.core.dbquery;
import org.rm.core.fun;


public class PingDAO extends basedao{
	public int updatePingResult(MetaDevice device){
		System.out.println(device.toString());
		dbquery db = new dbquery();
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		int flag = 0;
		try{
			conn = db.GetCon();
			String sql = "update meta_device set OnlineHosts=?,LastOnline=?,Status=? where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, device.getOnlineHosts());
			pstmt.setDate(2, new java.sql.Date(Calendar.getInstance().getTime().getTime()));
			pstmt.setInt(3, 1);
			pstmt.setInt(4, device.getId());
			flag = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pstmt = null ;
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db = null ;
		}
		return flag;
	}
}
