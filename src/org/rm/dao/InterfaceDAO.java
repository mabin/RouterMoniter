package org.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.rm.bean.DInterface;
import org.rm.bean.DInterface;
import org.rm.core.dbquery;
import org.rm.core.log;

public class InterfaceDAO {
	public List<DInterface> queryAllInterface(){
		List<DInterface> interfaces = new ArrayList<DInterface>();
		
		dbquery db = new dbquery();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from d_interface ";
		try {
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()){
				DInterface dinterface = new DInterface();
				dinterface.setId(rs.getInt("id"));
				dinterface.setName(rs.getString("Name"));
				dinterface.setAddress(rs.getString("Address"));
				dinterface.setMtu(rs.getInt("MTU"));
				dinterface.setState(rs.getString("State"));
				dinterface.setBindings(rs.getString("Bindings"));
				dinterface.setContextId(rs.getInt("ContextId"));
				interfaces.add(dinterface);
				log.debug(this.getClass(),dinterface.toString());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return interfaces;
	
	}
	
	public DInterface queryInterfaceById(int id){
		
		dbquery db = new dbquery();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from d_interface where id='"+id+"'";
		DInterface dinterface = null ;
		try {
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			dinterface = new DInterface();
			while (rs.next()){
				dinterface.setId(rs.getInt("id"));
				dinterface.setName(rs.getString("Name"));
				dinterface.setAddress(rs.getString("Address"));
				dinterface.setMtu(rs.getInt("MTU"));
				dinterface.setState(rs.getString("State"));
				dinterface.setBindings(rs.getString("Bindings"));
				dinterface.setContextId(rs.getInt("ContextId"));
				log.debug(this.getClass(),dinterface.toString());
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return dinterface;
	
	}
}
