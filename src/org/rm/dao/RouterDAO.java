package org.rm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.rm.bean.DHost;
import org.rm.bean.DInterface;
import org.rm.bean.MetaContext;
import org.rm.bean.MetaDevice;
import org.rm.core.dbquery;
import org.rm.core.fun;
import org.rm.core.log;

public class RouterDAO {
	public List<MetaDevice> queryAllDevice(){
		List<MetaDevice> devices = new ArrayList<MetaDevice>();
		
		dbquery db = new dbquery();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null ;
		String sql = "select * from meta_device ";
		try {
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			//pstmt.setInt(1, 1);
			rs = pstmt.executeQuery();
			while (rs.next()){
				MetaDevice device = new MetaDevice();
				device.setId(rs.getInt("id"));
				device.setDeviceIp(rs.getString("DeviceIp"));
				device.setLoginName(rs.getString("LoginName"));
				device.setPassword(rs.getString("Password"));
				device.setOnlineHosts(rs.getString("OnlineHosts"));
				System.out.println("*************************"+device.getOnlineHosts());
				devices.add(device);
				log.debug(this.getClass(),device.toString());
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
		return devices;
	}
	
	public int insertRouter(MetaDevice device){
		dbquery db = null;
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		int flag = 0;
		try{
			
			String sql = "insert into meta_device (DeviceIp,LoginName,Password,DeviceInfo,DevicePurpose,Status) " +
					"values (?,?,?,?,?,?)";
			
			db = new dbquery();
			conn = db.GetCon();
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, device.getDeviceIp());
			pstmt.setString(2, device.getLoginName());
			pstmt.setString(3, device.getPassword());
			pstmt.setString(4, device.getDeviceInfo());
			pstmt.setString(5, device.getDevicePurpose());
			pstmt.setInt(6, device.getStatus());
			flag = pstmt.executeUpdate();
		}catch(Exception e){
			log.error(this.getClass(),"insert failure");
			pstmt = null ;
			conn = null ;
			db = null ;
		}finally{
			pstmt = null ;
			conn = null ;
			db = null ;
		}
		return flag ;
	}
	
	public int updateRouter(MetaDevice device){
		
		dbquery db = null ;
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		int flag = -1;
		try{
			db = new dbquery();
			conn = db.GetCon();
			String updateSQL = "update meta_device set Status=? , LastOnline=? where id=?";
			pstmt = conn.prepareStatement(updateSQL);
			pstmt.setInt(1, device.getStatus());
			pstmt.setString(2, device.getLastOnline());
			pstmt.setInt(3, device.getId());
			flag = pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			pstmt = null ;
			conn = null ;
			db = null ;
		}
		return flag ;
	}
	
	
	public int updateContext(MetaContext context){
		dbquery db = null ;
		int id = 0;
		try{
			db = new dbquery();
			String field = "id";
			String whereSQL = "ContextId='"+context.getContextId()+"' and contextName='"+context.getContextName()+"'";//只用context_id,context_name做为条件
			log.debug(this.getClass(),whereSQL);
			id = db.DBResultTableValueOfInt(context.getTableName(), field, whereSQL);
			
			context.AddFilterFild("id");
			if (id==0){
				//这条数据不存在
				if( db.DBinsert(context)){
					id = db.DBResultTableValueOfInt(context.getTableName(), field, whereSQL);
				}else{
					return -1;
				}
			}
			
			//数据已存在，根据ID更新数据
			String updateSQL = db.GetUpdateSQL(context, "id='"+id+"'");
			if (!db.DBExeSQL(updateSQL)){
				return -1;
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db = null ;
		}
		return id;
	}
	
	public int updateHost(DHost host){
		dbquery db = null ;
		int id = -1 ;
		try{
			db = new dbquery();
			String field = "id";
			String whereSQL = "IpAddr='"+host.getIpAddr()+"' and MacAddr='"+host.getMacAddr()+"' and ContextId='"+host.getContextId()+"'";
			id = db.DBResultTableValueOfInt(host.getTableName(), field, whereSQL);
			
			host.AddFilterFild("id");
			if (id==0){
				//这条数据不存在
				if( db.DBinsert(host)){
					id = db.DBResultTableValueOfInt(host.getTableName(), field, whereSQL);
				}else{
					return -1;
				}
			}
			
			//数据已存在，根据ID更新数据
			String updateSQL = db.GetUpdateSQL(host, "id='"+id+"'");
			if (db.DBExeSQL(updateSQL)){
				return -1;
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db = null ;
		}
		return id ;
	}
	
	public int updateInterface(DInterface ainterface){
		dbquery db = null ;
		int id = -1;
		try{
			db = new dbquery();
			String field = "id";
			String whereSQL = "Address ='"+ainterface.getAddress()+"' and Name='"+ainterface.getName()+"'";
			id = db.DBResultTableValueOfInt(ainterface.getTableName(), field, whereSQL);
			
			ainterface.AddFilterFild("id");
			if (id==0){
				//这条数据不存在
				if( db.DBinsert(ainterface)){
					id = db.DBResultTableValueOfInt(ainterface.getTableName(), field, whereSQL);
				}else{
					return -1;
				}
			}
			
			//数据已存在，根据ID更新数据
			String updateSQL = db.GetUpdateSQL(ainterface, "id='"+id+"'");
			if (db.DBExeSQL(updateSQL)){
				return -1;
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			db = null ;
		}
		return id ;
	}
	
	public MetaDevice getRouterById(int routerid){
		MetaDevice dev = new MetaDevice();
		dbquery db = null ;
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		try{
			db = new dbquery();
			conn = db.GetCon();
			String sql = "select * from meta_device where id='"+routerid+"'";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()){
				dev.setId(rs.getInt("id"));
				dev.setDeviceName(rs.getString("devicename"));
				dev.setDeviceType(rs.getInt("devicetype"));
				dev.setHostname(rs.getString("hostname"));
				dev.setDeviceIp(rs.getString("deviceip"));
				dev.setLoginWay(rs.getString("loginway"));
				dev.setLoginName(rs.getString("loginname"));
				dev.setPassword(rs.getString("password"));
				dev.setStatus(rs.getInt("status"));
				dev.setLastOnline(fun.GetFormatDate(rs.getDate("LastOnline")));
				dev.setDevicePath(rs.getString("devicepath"));
				dev.setDeviceParent(String.valueOf(rs.getInt("deviceparent")));
				dev.setDeviceInfo(rs.getString("deviceinfo"));
				dev.setDevicePurpose(rs.getString("devicepurpose"));
				dev.setLocation(rs.getString("location"));
				dev.setDeviceDep(rs.getString("devicedep"));
				dev.setOnlineHosts(rs.getString("onlinehost"));
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return dev;
	}

}
