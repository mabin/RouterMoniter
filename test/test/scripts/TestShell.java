package test.scripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.rm.bean.MetaDevice;
import org.rm.biz.PingBiz;
import org.rm.scripts.RouterPing;
import org.rm.utils.VituralConsole;

public class TestShell {

	public static void main(String args[]) throws Exception {
		//shell对象
		String host = "192.168.11.2";
		int port = 22 ;
		String user = "eric";
		String password = "se800";
		VituralConsole shell = new VituralConsole(host,port,user,password);
		
		//命令序列
		FileInputStream cmdFile = new FileInputStream("CMD_RouterPing.txt");
		BufferedReader cmdReader = new BufferedReader(new InputStreamReader(cmdFile));
		String command = "";
		List<String> commandList = new ArrayList<String>();
		while ( (command=cmdReader.readLine()) != null){
			commandList.add(command);
		}
		
		RouterPing ping = new RouterPing();
		ping.setShell(shell);
		ping.setCommandList(commandList);
		Thread t1 = new Thread(ping);
		t1.start();
		//List<Map<String,Object>> resultList = ping.getResult();
		
		MetaDevice device = new MetaDevice();
		device.setId(1);
		device.setDeviceIp(host);
		device.setDeviceName(user);
		device.setPassword(password);
		
		PingBiz biz = new PingBiz();
		biz.addPingResult(ping.getResult(),device);
		
	}
}
