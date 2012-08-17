package org.rm.scripts;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.rm.biz.PingBiz;
import org.rm.utils.VituralConsole;

public class RouterPing implements Analysis,Runnable{
	private  VituralConsole shell = null;
	private List<String> commandList = new ArrayList<String>();
	public List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
	
	public List<Map<String,String>> getAllContext(){
		String[] commands = {"show context all"," "};
		if (getShell().executeCommands(commands)){
			return AnalysisContext(getShell().getResponse(), "Context");
		}
		return null ;
	}
	public boolean switchContext(String contextName){
		String[] commands = {"\r","context "+contextName};
		return getShell().executeCommands(commands);
	}
	
	public List<Map<String,String>> getHostsByContext(){
		String[] arpCommand = {"\r","show arp"};
		if(getShell().executeCommands(arpCommand)){
			 return  AnalysisArp(getShell().getResponse(), "Host");
		}
		return null;
	}
	
	public boolean pingHost(String hostIP){
		String[] pingCommand = {"\r","ping "+hostIP};
		if (getShell().executeCommands(pingCommand)){
			return AnalysisPing(getShell().getResponse());
		}
		return false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		List<Map<String,String>> contextList =	getAllContext()	;	
		for (int i=0; i<contextList.size();i++){
			//结果集map，用于存储context对应主机状态
			Map<String,Object> resultMap = new HashMap<String,Object>();
			
			Map<String,String> map =new HashMap<String,String>();
			map = contextList.get(i);
			
			resultMap.put("ContextName", map.get("Context Name"));
			
			if (switchContext(map.get("Context Name"))){
				List<Map<String,String>> arpList = getHostsByContext();
				if (arpList == null){
					resultMap.put("ip_status", null);
					resultList.add(resultMap);
					continue;
				}
				
				List<String> ipList = new ArrayList<String>();
				for (int j=0; j<arpList.size();j++){
					Map<String,String> arpMap = new HashMap<String,String>();
					arpMap = arpList.get(i);
					
					if (pingHost(arpMap.get("Host"))){
						boolean pingResult = AnalysisPing(getShell().getResponse());
							ipList.add(arpMap.get("Host")+"--"+pingResult);
					}
				}
				resultMap.put("ip_status", ipList);
			}
				resultList.add(resultMap);
		}
		/*
		System.out.println("**********************结果集BEGIN*****************************");
		System.out.println(resultList.size());
		for (Map<String,Object> rsmap:resultList){
			//取出结果集的中每个map，map的key分别为：contextname<string>, ip_status<list>
			String contextName = (String) rsmap.get("ContextName");
			List<String> ips = (List<String>) rsmap.get("ip_status");
			//如果这个context没有主机,只打印contextname
			if (ips == null){
				System.out.println("["+contextName+"]"+" null hosts");
				continue;
			}
			//如果这个context有主机,打印contextname--IP-result
			for (String ipStr: ips){
				System.out.println("["+contextName+"] -- "+ipStr);
			}
		}
		
		System.out.println("**********************结果集END*****************************");
		*/
		/**
		 * resultList{{map1{ContextName-a,ip_status{1.1.1.1:true,2.2.2.2-false}}},{map1{ContextName-b,ip_status{3.3.3.3:true,4.4.4.4-false}}}}
		 */
		
	
	}
	
	/**
	 * 
	 * @param buffer
	 * @param regex
	 * @return
	 */
	public List<Map<String, String>> AnalysisContext(StringBuffer buffer,
			String regex) {

		List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();

		List<Map<String, String>> valueListNew = new ArrayList<Map<String, String>>();

		StringBuffer bufferInfo = new StringBuffer();

		bufferInfo = buffer;

		Scanner scan = new Scanner(new InputStreamReader(
				new ByteArrayInputStream(bufferInfo.toString().getBytes())));

		List<String> headerList = new ArrayList<String>();
		String header = "";
		while ((header = scan.nextLine()).indexOf(regex) == -1) {
		}

		String headers[] = header.split("\\s\\s+");
		for (String tem : headers) {
			headerList.add(tem);
		}
		scan.nextLine();
		scan.nextLine();
		while (scan.hasNext()) {
			String strs[] = scan.nextLine().split("\\s\\s+");
			Map<String, String> valueMap = new HashMap<String, String>();
			for (int j = 0; j < strs.length; j++) {
				valueMap.put(headerList.get(j), strs[j]);
			}
			if (valueMap.get(headerList.get(0)) != null
					&& valueMap.get(headerList.get(1)) != null) {
				valueList.add(valueMap);
			}
		}

		for (int i = 0; i < valueList.size(); i++) {

			Map<String, String> mapNew = new HashMap<String, String>();

			mapNew.put("Context Name", valueList.get(i).get(headerList.get(0)));
			mapNew.put("Context ID", valueList.get(i).get(headerList.get(1)));
			mapNew.put("VPN-RD", valueList.get(i).get(headerList.get(2)));
			mapNew.put("Description", valueList.get(i).get(headerList.get(3)));
			valueListNew.add(mapNew);
		}

		try {
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valueListNew;
	}

	public List<Map<String, String>> AnalysisArp(StringBuffer buffer,String regex) {
		
		List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();

		List<Map<String, String>> valueListNew = new ArrayList<Map<String, String>>();

		StringBuffer bufferInfo = new StringBuffer();
		bufferInfo = buffer;
		Scanner scan = new Scanner(new InputStreamReader(
				new ByteArrayInputStream(bufferInfo.toString().getBytes())));

		String arpCache = "";
		while ((arpCache = scan.nextLine()).indexOf("Total number of arp entries in cache") == -1) {}
		char lastChar = arpCache.charAt(arpCache.length() - 1);
		int cacheSize = Integer.valueOf(String.valueOf(lastChar));
		if (cacheSize == 0) {
			scan.close();
			return null;
		}

		List<String> headerList = new ArrayList<String>();
		String header = "";
		while ((header = scan.nextLine()).indexOf(regex) == -1) {}
		String headers[] = header.split("\\s\\s+");
		for (String tem : headers) {
			headerList.add(tem);
		}

		while (scan.hasNext()) {
			String strs[] = scan.nextLine().split("\\s\\s+");
			Map<String, String> valueMap = new HashMap<String, String>();
			for (int j = 0; j < strs.length; j++) {
				valueMap.put(headerList.get(j), strs[j]);
			}

			if (valueMap.get(headerList.get(0)) != null
					&& valueMap.get(headerList.get(1)) != null) {
				valueList.add(valueMap);
			}
		}
		for (int i = 0; i < valueList.size(); i++) {
			Map<String, String> mapNew = new HashMap<String, String>();
			mapNew.put("Host", valueList.get(i).get(headerList.get(0)));
			mapNew.put("Hardware address",
					valueList.get(i).get(headerList.get(1)));
			mapNew.put("Ttl", valueList.get(i).get(headerList.get(2)));
			mapNew.put("Circuit", valueList.get(i).get(headerList.get(4)));
			valueListNew.add(mapNew);
		}
		try {
			scan.close();
		} catch (Exception e) {

			e.printStackTrace();
		}
		return valueListNew;

	}

	public boolean AnalysisPing(StringBuffer buffer) {
		try {
			StringBuffer bufferInfo = new StringBuffer();
			bufferInfo = buffer;
			
			Scanner scan = new Scanner(new InputStreamReader(
					new ByteArrayInputStream(bufferInfo.toString().getBytes())));
			scan.nextLine();
			String header = "";
			while ((header = scan.nextLine()) != null) {
				
				if (header.charAt(0)=='!') {
					scan.close();
					return true;
				}
			}
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	
	
	public List<Map<String,Object>> getResult(){
		return resultList;
	}

	public VituralConsole getShell() {
		return shell;
	}

	public void setShell(VituralConsole shell) {
		this.shell = shell;
	}

	public List<String> getCommandList() {
		return commandList;
	}

	public void setCommandList(List<String> commandList) {
		this.commandList = commandList;
	}

}

