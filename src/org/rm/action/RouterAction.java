package org.rm.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONObject;
import org.rm.bean.MetaDevice;
import org.rm.biz.RouterBiz;
import org.rm.core.baseaction;
import org.rm.core.dbquery;
import org.rm.core.fun;
import org.rm.core.log;
import org.rm.core.result;
import org.rm.utils.DBGridQuery;

public class RouterAction extends baseaction{

	public void addRouter(){
		MetaDevice router = null ;
		dbquery db = null;
		try{
			db = new dbquery();
			router = new MetaDevice();
			router.setHostname(fun.nil(request.getParameter("hostname"), ""));
			router.setLoginName(fun.nil(request.getParameter("loginname"), ""));
			router.setDeviceIp(fun.nil(request.getParameter("deviceip"), ""));
			router.setPassword(fun.nil(request.getParameter("password"), ""));
			router.setStatus(fun.parseInt(request.getParameter("status"), 0));
			router.setLastOnline(fun.nil(request.getParameter("lastonline"), ""));
			router.setDeviceDep(fun.nil(request.getParameter("devicedep"), ""));
			router.setDeviceInfo(fun.nil(request.getParameter("deviceinfo"), ""));
			router.setDevicePurpose(fun.nil(request.getParameter("devicepurpose"), ""));
			router.setLocation(fun.nil(request.getParameter("location"), ""));
			router.AddReserveFild("hostname");
			router.AddReserveFild("loginname");
			router.AddReserveFild("password");
			router.AddReserveFild("status");
			router.AddReserveFild("lastonline");
			router.AddReserveFild("devicedep");
			router.AddReserveFild("deviceinfo");
			router.AddReserveFild("devicepurpose");
			router.AddReserveFild("location");
			router.AddReserveFild("deviceip");
			
			if (db.DBinsert(router)){
				out.print(result.JSONObjectSuccess());
				
			}else{
				out.print(result.JSONObjectFailure());
			}
			
		}catch(Exception e){
			router = null;
			e.printStackTrace();
		}finally{
			router = null;
			db = null ;
		}
	}
	
	public void GetAllRouter(){
		JSONObject json = null ;
		DBGridQuery grid = null ;
		try{
			grid = new DBGridQuery();
			json = new JSONObject();
			json = grid.DBEXTGridResult("select * from meta_device ", "meta_device");
			log.debug(this.getClass(),json.toString());
			out.print(json);
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(this.getClass(),"querall");
		}finally{
			grid = null ;
		}
	}
	
	public void UpdateById(){
		String id = fun.nil(request.getParameter("id"), "0");

		MetaDevice router = new MetaDevice();
		router.setHostname(fun.nil(request.getParameter("hostname"), ""));
		router.setDeviceIp(fun.nil(request.getParameter("deviceip"), ""));
		router.setLoginName(fun.nil(request.getParameter("loginname"), ""));
		router.setPassword(fun.nil(request.getParameter("password"), ""));
		router.setStatus(fun.parseInt(request.getParameter("status"), 0));
		router.setLastOnline(fun.nil(request.getParameter("lastonline"), ""));
		router.setDeviceDep(fun.nil(request.getParameter("devicedep"), ""));
		router.setDeviceInfo(fun.nil(request.getParameter("deviceinfo"), ""));
		router.setDevicePurpose(fun.nil(request.getParameter("devicepurpose"), ""));
		router.setLocation(fun.nil(request.getParameter("location"), ""));
		router.AddReserveFild("hostname");
		router.AddReserveFild("loginname");
		router.AddReserveFild("password");
		router.AddReserveFild("status");
		router.AddReserveFild("lastonline");
		router.AddReserveFild("devicedep");
		router.AddReserveFild("deviceinfo");
		router.AddReserveFild("devicepurpose");
		router.AddReserveFild("location");
		router.AddReserveFild("deviceip");
		dbquery db = null;
		JSONObject json = null ;
		
		try{
			db = new dbquery();
			json = new JSONObject();
			String updateSQL = db.GetUpdateSQL(router, "id = "+id);
			log.debug(this.getClass(),updateSQL);
			if (db.DBExeSQL(updateSQL)){
				json = result.JSONObjectSuccess();
			}else{
				json = result.JSONObjectFailure("设备信息更新失败");
			}
			out.println(json);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void exe() {
		// TODO Auto-generated method stub
		if (request.getParameter("actioncmd").equals("insert")){
			addRouter();
		}else if (request.getParameter("actioncmd").equals("query")){
			GetAllRouter();
		}else if (request.getParameter("actioncmd").equals("updatebyid")){
			UpdateById();
		}
	}

}















