package org.rm.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.rm.bean.MetaContext;
import org.rm.bean.MetaDevice;
import org.rm.bean.MetaPeople;
import org.rm.biz.PingBiz;
import org.rm.biz.RouterBiz;
import org.rm.core.dbquery;
import org.rm.core.fun;
import org.rm.dao.ContextDAO;
import org.rm.dao.RouterDAO;
import org.rm.utils.Notifier;
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
		
		System.out.println("*******the scriptRun timer excuted!************");
		// TODO Auto-generated method stub
		
		List<MetaDevice> allDevice = getDevicesInfo();
		RouterPing[] pings = new RouterPing[5];
		for (int i=0; i<allDevice.size(); i+=5){
			RouterPing ping = null ;
			PingBiz biz = null;
			//多线程时一次取出5个device
			
			for (int j=0; j<5; j++){
				MetaDevice dev = allDevice.get(i+j);
				System.out.println(dev.getDeviceIp());
				VituralConsole shell = new VituralConsole(dev.getDeviceIp(),22,dev.getLoginName(),dev.getPassword());
				ContextDAO contextDAO = new ContextDAO();
				List<Map<String,String>> contexts = contextDAO.queryContextByRouterId(dev.getId());
				if (!shell.isOnline()){
					ping = new RouterPing(contexts);
					dev.setStatus(1);
					ping.setShell(shell);
					System.out.println("Not Online");
					dev.setStatus(0);
					RouterDAO routerdao = new RouterDAO();
					routerdao.updateRouter(dev);
					//biz = new PingBiz();
					//biz.addPingResult(ping.getResultList(),dev);
					Notifier notify = new Notifier(new MetaPeople(dev.getDevicePurpose()),
							"router offline",dev.getDeviceIp()+" is offline at "+fun.GetCurFormatTime());
					notify.sendNotifier();
				}
				ping = new RouterPing(contexts);
				ping.setPeople(dev.getDevicePurpose());
				pings[j] = ping;
			}
			
			for (RouterPing routerping: pings){
				Thread pingThread = new Thread(routerping);
				pingThread.run();
			}
			
		}
		
		
		
	}
	

}
