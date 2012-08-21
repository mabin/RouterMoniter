package org.rm.biz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.rm.bean.DHost;
import org.rm.bean.DInterface;
import org.rm.bean.MetaContext;
import org.rm.bean.MetaDevice;
import org.rm.core.fun;
import org.rm.core.log;
import org.rm.dao.PingDAO;
import org.rm.dao.RouterDAO;

public class PingBiz {
	public void addPingResult(List<Map<String,Object>> resultList,MetaDevice device){
		StringBuffer onlineHosts = new StringBuffer();
		for (Map<String,Object> rsmap:resultList){
			String contextName = (String) rsmap.get("ContextName");
			List<String> ips = (List<String>) rsmap.get("ip_status");
			
			onlineHosts.append(contextName+":[");
			if (ips == null){
				onlineHosts.append("];");
				continue;
			}
			for (String ipStr: ips){
				onlineHosts.append(ipStr+",");
			}
			onlineHosts.append("];");
		}
		System.out.println(onlineHosts.toString());
		device.setOnlineHosts(onlineHosts.toString());
		
		PingDAO dao = new PingDAO();
		dao.updatePingResult(device);
	}
	
	public int updateRouterInfo(MetaDevice device){
		RouterDAO dao = new RouterDAO();
		return dao.updateRouter(device);
	}
	
	public boolean updateContextInfo(List<Map<String,String>> contextList,List<Map<String,Object>> hiMapList,int RouterID){
		List<MetaContext> contexts = new ArrayList<MetaContext>();
		//1)首先更新context信息
		for (Map<String,String> rsmap:contextList){
			//1.1)从contextList中获取到一个context
			MetaContext tmpContext = new MetaContext();
			tmpContext.setDeviceId(RouterID);
			tmpContext.setContextName(fun.nil((String)rsmap.get("Context Name")," "));
			tmpContext.setContextId(fun.nil((String)rsmap.get("Context ID")," "));
			tmpContext.setVpnRD(fun.nil((String)rsmap.get("VPN-RD")," "));
			tmpContext.setDescription(fun.nil((String)rsmap.get("Description")," "));
			//1.2)将这个最新的context对象装入list中，为保证效率，在后面一次性更新完成。
			contexts.add(tmpContext);
		}
		
		RouterDAO routerDAO = new RouterDAO();
		boolean flag = false;
			//1.3)循环对象List，对context进行更新
		for (MetaContext ctx:contexts){
			int id = routerDAO.updateContext(ctx);
			if (id ==-1){
				log.error(this.getClass(), "Insert or Add Context failure with "+ctx.getContextName());
				flag = false;
			}else{
				//1.4)如果context更新或者添加成功，返回当前操作context的数据库id
				log.info(this.getClass(), "Insert or Add Context Success with "+ctx.getContextName());
				//1.5)获取context的Name，并从hiMapList中得到对应contextName的映射list
				Map<String,Object> hiMap = getContexttoHost(ctx.getContextName(), hiMapList);
				updateHostInfo(hiMap,id);
				flag = true;
			}
		}
		
		return flag ;
	}
	
	public void updateHostInfo(Map<String,Object> hiMap,int ContextID){
		List<DHost> hosts = new ArrayList<DHost>();
		List<DInterface> interfaces = new ArrayList<DInterface>();
			//主机集合
			@SuppressWarnings("unchecked")
			//2)更新主机信息，通过himap获取到主机list和接口list
			List<Map<String,String>> ahosts = (List<Map<String,String>>)hiMap.get("Host_List");
			if (ahosts != null){
				for (Map<String,String> hostMap: ahosts){
					DHost tmpHost = new DHost();
					tmpHost.setContextId(ContextID);
					tmpHost.setIpAddr(fun.nil(hostMap.get("Host"), " "));
					tmpHost.setMacAddr(fun.nil(hostMap.get("Hardware address"), " "));
					tmpHost.setTtl(fun.nil(hostMap.get("Ttl"), " "));
					tmpHost.setType(fun.nil(hostMap.get("Type"), " "));
					tmpHost.setCircuit(fun.nil(hostMap.get("Circuit"), " "));
					tmpHost.setStatus(fun.nil(hostMap.get("PingStatus"), " "));
					hosts.add(tmpHost);
				}
			}
			//接口集合
			@SuppressWarnings("unchecked")
			List<Map<String,String>> ainterfaces = (List<Map<String,String>>)hiMap.get("Interface_List");
			if (ainterfaces != null){
				for (Map<String,String> interfaceMap: ainterfaces){
					DInterface tmpInterface = new DInterface();
					tmpInterface.setContextId(ContextID);
					tmpInterface.setName(fun.nil(interfaceMap.get("Name")," "));
					tmpInterface.setAddress(fun.nil(interfaceMap.get("Address")," "));
					tmpInterface.setMtu(fun.parseInt(interfaceMap.get("MTU"),0));
					tmpInterface.setState(fun.nil(interfaceMap.get("State")," "));
					tmpInterface.setBindings(fun.nil(interfaceMap.get("Bindings")," "));
					interfaces.add(tmpInterface);
			}
			RouterDAO routerDAO = new RouterDAO();
			for (DHost host: hosts){
				if (routerDAO.updateHost(host) == -1){
					log.error(this.getClass(),"Insert or Add Host failure with "+host.getIpAddr());
				}else{
					log.info(this.getClass(),"Insert or Add Host Success with "+host.getIpAddr());
				}
			}
			for (DInterface dinterface: interfaces){
				if (routerDAO.updateInterface(dinterface) == -1){
					log.error(this.getClass(),"Insert or Add Inteface failure with "+dinterface.getAddress());
				}else{
					log.info(this.getClass(),"Insert or Add Inteface Success with "+dinterface.getAddress());
				}
			}
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
	
	
}
