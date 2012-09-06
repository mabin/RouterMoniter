package org.rm.action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.rm.bean.DHost;
import org.rm.bean.DInterface;
import org.rm.bean.MetaContext;
import org.rm.bean.MetaDevice;
import org.rm.core.baseaction;
import org.rm.core.dbquery;
import org.rm.core.fun;
import org.rm.core.log;
import org.rm.core.result;
import org.rm.dao.RouterDAO;
import org.rm.scripts.InitInfos;
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
	
	public void initRouter(){
		RouterDAO routerdao = new RouterDAO();
		String routerid = request.getParameter("routerid");
		MetaDevice dev = routerdao.getRouterById(Integer.parseInt(routerid));
		InitInfos init = new InitInfos(dev);
		init.run();
		String hostname = init.getHostname();
		List<Map<String,String>> contextList = init.getContextList();
		List<Map<String,Object>> hiMapList = init.getHiMapList();
		
		try{
			//遍历context
			for (Map<String,String> ctxMap:contextList){
				//得到一个context对象
				MetaContext tmpContext = new MetaContext();
				tmpContext.setDeviceId(Integer.parseInt(routerid));
				tmpContext.setContextName(fun.nil((String)ctxMap.get("Context Name")," "));
				tmpContext.setContextId(fun.nil((String)ctxMap.get("Context ID")," "));
				tmpContext.setVpnRD(fun.nil((String)ctxMap.get("VPN-RD")," "));
				tmpContext.setDescription(fun.nil((String)ctxMap.get("Description")," "));
				
				int ctxID = routerdao.updateContext(tmpContext);
				log.debug(this.getClass(),"insert Context "+tmpContext.getContextName()+" "+ctxID);
				
				//通过context对象得到其对应的interface和host
				Map<String,Object> hiMap = getContexttoHost(tmpContext.getContextName(), hiMapList);
				
				//2)更新主机信息，通过himap获取到主机list和接口list
				List<Map<String,String>> ahosts = (List<Map<String,String>>)hiMap.get("Host_List");
				if (ahosts != null){
					for (Map<String,String> hostMap: ahosts){
						DHost tmpHost = new DHost();
						tmpHost.setContextId(ctxID);
						tmpHost.setIpAddr(fun.nil(hostMap.get("Host"), " "));
						tmpHost.setMacAddr(fun.nil(hostMap.get("Hardware address"), " "));
						tmpHost.setTtl(fun.nil(hostMap.get("Ttl"), " "));
						tmpHost.setType(fun.nil(hostMap.get("Type"), " "));
						tmpHost.setCircuit(fun.nil(hostMap.get("Circuit"), " "));
						tmpHost.setStatus(fun.nil(hostMap.get("PingStatus"), " "));
						int hostid = routerdao.updateHost(tmpHost);
						log.debug(this.getClass(),"insert host "+tmpHost.getIpAddr()+" "+hostid);
					}
				}
				//接口集合
				@SuppressWarnings("unchecked")
				List<Map<String,String>> ainterfaces = (List<Map<String,String>>)hiMap.get("Interface_List");
				if (ainterfaces != null){
					for (Map<String,String> interfaceMap: ainterfaces){
						DInterface tmpInterface = new DInterface();
						tmpInterface.setContextId(ctxID);
						tmpInterface.setName(fun.nil(interfaceMap.get("Name")," "));
						tmpInterface.setAddress(fun.nil(interfaceMap.get("Address")," "));
						tmpInterface.setMtu(fun.parseInt(interfaceMap.get("MTU"),0));
						tmpInterface.setState(fun.nil(interfaceMap.get("State")," "));
						tmpInterface.setBindings(fun.nil(interfaceMap.get("Bindings")," "));
						int interfaceid = routerdao.updateInterface(tmpInterface);
						log.debug(this.getClass(),"insert interface "+tmpInterface.getName()+" "+interfaceid);
					}
				}
			}
			out.print(result.JSONObjectSuccess());
		}catch(Exception e){
			out.print(result.JSONObjectFailure("路由器信息初始化失败"));
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param contextName 
	 * @param hiMapList
	 * @return 返回的是contextName对应的hostList和interfaceList
	 */
	public Map<String,Object> getContexttoHost(String contextName, List<Map<String,Object>> hiMapList){
		Iterator<Map<String, Object>> hiIter = hiMapList.iterator();
		//遍历HostInterface映射List
		while (hiIter.hasNext()){
			Map<String,Object> tmpMap = hiIter.next();
			if (tmpMap.get("Context Name").equals(contextName))
				return tmpMap ;
		}
		return null ;
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
		}else if (request.getParameter("actioncmd").equals("init"))
			initRouter();
	}

}















