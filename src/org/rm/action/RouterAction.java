package org.rm.action;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.rm.bean.MetaDevice;
import org.rm.biz.RouterBiz;
import org.rm.core.baseaction;
import org.rm.core.dbquery;
import org.rm.core.log;
import org.rm.core.result;

public class RouterAction extends baseaction{

	public void addRouter(){
		System.out.println("Add Router");
		MetaDevice device = null ;
		RouterBiz biz = new RouterBiz();
		try{
			device = new MetaDevice();
//			device.AddFilterFild("id");
//			device.setDeviceName((String)request.getParameter("DeviceName"));
//			device.setDeviceType(Integer.parseInt((String)request.getParameter("DeviceType")));
//			device.setHostName((String)request.getParameter("HostName"));
			device.setDeviceIp((String)request.getParameter("DeviceIp"));
//			device.setLoginWay((String)request.getParameter("LoginWay"));
			device.setLoginName((String)request.getParameter("LoginName"));
			device.setPassword((String)request.getParameter("Password"));
			device.setStatus(0);
//			//device.setLastOnline(fun.request.getParameter("LastOnline"));
//			device.setDevicePath((String)request.getParameter("DevicePath"));
//			device.setDeviceParent(Integer.parseInt((String)request.getParameter("DeviceParent")));
			device.setDeviceInfo((String)request.getParameter("DeviceInfo"));
			device.setDevicePurpose((String)request.getParameter("DevicePurpose"));
//			device.setLocation((String)request.getParameter("Location"));
//			device.setDeviceDept((String)request.getParameter("DeviceDep"));
//			device.setOnlineHosts((String)request.getParameter("OnlineHosts"));
			int flag = biz.addRouter(device);
			if (flag != -1){
				out.print(result.JSONObjectSuccess());
			}else{
				out.print(result.JSONObjectFailure());
			}
		}catch(Exception e){
			device = null;
			e.printStackTrace();
		}finally{
			device = null;
		}
	}
	
	@Override
	public void exe() {
		// TODO Auto-generated method stub
		if (request.getParameter("actioncmd").equals("insert")){
			addRouter();
		}
	}

}
