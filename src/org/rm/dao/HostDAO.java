package org.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.rm.bean.DHost;
import org.rm.bean.DHost;
import org.rm.core.dbquery;
import org.rm.core.log;

public class HostDAO {
	public List<DHost> queryAllHost(){
		List<DHost> hosts = new ArrayList<DHost>();
		
		dbquery db = new dbquery();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from d_host ";
		try {
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()){
				DHost host = new DHost();
				host.setId(rs.getInt("id"));
				host.setIpAddr(rs.getString("IpAddr"));
				host.setMacAddr(rs.getString("MacAddr"));
				host.setTtl(rs.getString("TTL"));
				host.setType(rs.getString("Type"));
				host.setCircuit(rs.getString("Circuit"));
				host.setContextId(rs.getInt("ContextId"));
				host.setStatus(rs.getString("Status"));
				hosts.add(host);
				log.debug(this.getClass(),host.toString());
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
		return hosts;
	
	}
	
	public DHost queryHostById(int id){
		
		dbquery db = new dbquery();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from d_host where id='"+id+"'";
		DHost host = null ;
		try {
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			host = new DHost();
			while (rs.next()){
				host.setId(rs.getInt("id"));
				host.setIpAddr(rs.getString("IpAddr"));
				host.setMacAddr(rs.getString("MacAddr"));
				host.setTtl(rs.getString("TTL"));
				host.setType(rs.getString("Type"));
				host.setCircuit(rs.getString("Circuit"));
				host.setContextId(rs.getInt("ContextId"));
				host.setStatus(rs.getString("Status"));
				log.debug(this.getClass(),host.toString());
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
		return host;
	
	}
}
