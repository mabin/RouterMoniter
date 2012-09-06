package org.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rm.bean.MetaContext;
import org.rm.core.dbquery;
import org.rm.core.log;

public class ContextDAO {
	public List<MetaContext> queryAllContext(){
		List<MetaContext> contexts = new ArrayList<MetaContext>();
		
		dbquery db = new dbquery();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from meta_context ";
		try {
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()){
				MetaContext context = new MetaContext();
				context.setId(rs.getInt("id"));
				context.setContextId(rs.getString("Context_id"));
				context.setContextName(rs.getString("Context_name"));
				context.setDeviceId(rs.getInt("Device_id"));
				context.setVpnRD(rs.getString("VPN_RD"));
				context.setDescription(rs.getString("Description"));
				contexts.add(context);
				log.debug(this.getClass(),context.toString());
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
		return contexts;
	
	}
	
	public List<Map<String, String>> queryContextByRouterId(int routerID){
		List<Map<String, String>> contexts = new ArrayList<Map<String, String>>();
		
		dbquery db = new dbquery();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from meta_context where deviceId='"+routerID+"'";
		try {
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()){
				Map<String, String> mapNew = new HashMap<String, String>();
				mapNew.put("id", String.valueOf(rs.getInt("id")));
				mapNew.put("Context_id", rs.getString("Context_id"));
				mapNew.put("Context_name", rs.getString("Context_name"));
				mapNew.put("Device_id", String.valueOf(rs.getInt("Device_id")));
				mapNew.put("VPN_RD", rs.getString("VPN_RD"));
				mapNew.put("Description", rs.getString("Description"));
				
				contexts.add(mapNew);
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
		return contexts;
	
	}
	
	public MetaContext queryContextById(int id){
		
		dbquery db = new dbquery();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from meta_context where id='"+id+"'";
		MetaContext context = null ;
		try {
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			context = new MetaContext();
			while (rs.next()){
				context.setId(rs.getInt("id"));
				context.setContextId(rs.getString("Context_id"));
				context.setContextName(rs.getString("Context_name"));
				context.setDeviceId(rs.getInt("Device_id"));
				context.setVpnRD(rs.getString("VPN_RD"));
				context.setDescription(rs.getString("Description"));
				log.debug(this.getClass(),context.toString());
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
		return context;
	
	}
}
