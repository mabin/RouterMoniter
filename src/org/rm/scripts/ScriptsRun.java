package org.rm.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.rm.bean.MetaDevice;
import org.rm.biz.PingBiz;
import org.rm.biz.RouterBiz;
import org.rm.utils.VituralConsole;

public class ScriptsRun extends TimerTask{
	public ServletContext sc = null ;
	
	public ScriptsRun(ServletContext servletContext){
		sc = servletContext;
	}
	
	public List<MetaDevice> getDevicesInfo(){
		RouterBiz router = new RouterBiz(); 
		return router.getAllRouter();
	}
	
	@SuppressWarnings("resource")
	public List<String> getCommands(){
		//脚本名称，后期功能增加通过参数传入
		
		String cmdFilePath=sc.getRealPath("/")+"/scripts/test.txt";
		System.out.println(cmdFilePath);
		FileInputStream cmdFile;
		List<String> commandList = new ArrayList<String>();
		try {
			cmdFile = new FileInputStream(cmdFilePath);
			BufferedReader cmdReader = new BufferedReader(new InputStreamReader(cmdFile));
			String command = "";
			while ( (command=cmdReader.readLine()) != null){
				commandList.add(command);
				System.out.println(command);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return commandList;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<MetaDevice> allDevice = getDevicesInfo();
		RouterPing ping = null ;
		PingBiz biz = null;
		for (int i=0; i<allDevice.size(); i++){
			//多线程时一次取出5个device
			MetaDevice device1 = allDevice.get(i);
			System.out.println(device1.getDeviceIp());
			VituralConsole shell = new VituralConsole(device1.getDeviceIp(),22,device1.getLoginName(),device1.getPassword());
			if (!shell.isOnline()){
				ping = new RouterPing();
				device1.setStatus(1);
				ping.setShell(shell);
				System.out.println("Not Online");
				device1.setStatus(0);
				biz = new PingBiz();
				//biz.addPingResult(ping.getResult(),device1);
				biz.addPingResult(ResultRobot(),device1);
			}
			ping = new RouterPing();
			device1.setStatus(1);
//			ping.setShell(shell);
//			ping.setCommandList(getCommands());
//			Thread t1 = new Thread(ping);
//			t1.run();
//			
//			System.out.println("**********************结果集BEGIN*****************************");
//			List<Map<String,Object>> robot = ResultRobot();
//			System.out.println(robot.size());
//			for (Map<String,Object> rsmap:robot){
//				//取出结果集的中每个map，map的key分别为：contextname<string>, ip_status<list>
//				String contextName = (String) rsmap.get("ContextName");
//				List<String> ips = (List<String>) rsmap.get("ip_status");
//				//如果这个context没有主机,只打印contextname
//				if (ips == null){
//					System.out.println("["+contextName+"]"+" null hosts");
//					continue;
//				}
//				//如果这个context有主机,打印contextname--IP-result
//				for (String ipStr: ips){
//					System.out.println("["+contextName+"] -- "+ipStr);
//				}
//			}
//			
//			System.out.println("**********************结果集END*****************************");
//			
			
			
			biz = new PingBiz();
			biz.addPingResult(ResultRobot(),device1);
		}
	}
	
	public List<Map<String,Object>> ResultRobot(){
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		Map<String,Object> m1 = new HashMap<String,Object>();
		String cn1 = "context1";
		List<String> ip1 = new ArrayList<String>();
		ip1.add("10.1.1.1--true");ip1.add("10.1.1.2--false");ip1.add("10.1.1.3--true");
		ip1.add("10.1.1.4--true");ip1.add("10.1.1.5--false");ip1.add("10.1.1.6--true");
		m1.put("ContextName", cn1);
		m1.put("ip_status", ip1);
		
		Map<String,Object> m2 = new HashMap<String,Object>();
		String cn2 = "context2";
		List<String> ip2 = new ArrayList<String>();
		ip2.add("20.1.1.1--true");ip2.add("20.1.1.2--false");ip2.add("20.1.1.3--true");
		ip2.add("20.1.1.4--true");ip2.add("20.1.1.5--false");ip2.add("20.1.1.6--true");
		m2.put("ContextName", cn2);
		m2.put("ip_status", ip2);
		result.add(m1);
		result.add(m2);
		
		return result ;
	}

}
