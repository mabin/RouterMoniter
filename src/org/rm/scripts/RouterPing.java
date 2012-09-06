package org.rm.scripts;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.rm.bean.MetaPeople;
import org.rm.biz.PingBiz;
import org.rm.core.dbquery;
import org.rm.core.fun;
import org.rm.core.log;

import org.rm.utils.Notifier;
import org.rm.utils.VituralConsole;

public class RouterPing implements Analysis, Runnable {

	private String hostName = null;

	private VituralConsole shell = null;

	private List<String> commandList = new ArrayList<String>();

	private List<Map<String, String>> contextList = new ArrayList<Map<String, String>>();

	public List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
	
	private String people = null;
	
	public RouterPing(List<Map<String, String>> contextList){
		this.contextList = contextList;
	}

	public void RouterInit() {
		this.HostNameInit();
		this.ContextArpInterfaceInit(this.contextList);
	}

	public void HostNameInit() {
		String[] commandsHostName = { "\r" };
		String hostName = null;
		if (shell.executeCommands(commandsHostName)) {
			this.hostName = AnalysisHostName(shell.getResponse());
			System.out.println(hostName);
		}
	}


	public String AnalysisHostName(StringBuffer buffer) {

		StringBuffer bufferInfo = new StringBuffer();

		bufferInfo = buffer;

		System.out
				.println("******begin*********the out put for ---- \r --------by jenny --------0808--------");

		System.out.println(bufferInfo);

		System.out
				.println("******end*********the out put for ----- \r --------by jenny --------0808--------");

		Scanner scan = new Scanner(new InputStreamReader(
				new ByteArrayInputStream(bufferInfo.toString().getBytes())));

		String hostname = null;
		;
		String line = "";
		while ((line = scan.nextLine()) != null && (line.contains("]"))) {
			System.out
					.println("******the while line*********the out put for ----- line----------------");

			System.out.println("the line is :" + line + "-------------------");

			int beginInt = line.indexOf("]");
			int endInt = line.indexOf("#");
			hostname = line.substring(beginInt + 1, endInt);

			System.out.println("the line beginInt is :" + beginInt
					+ "  the endInt is: " + endInt + "the hostname is :"
					+ hostname + "**********");
			System.out.println(line);
			return hostname;
		}
		return hostname;
	}

	public List<Map<String, String>> AnalysisInterface(StringBuffer buffer,
			String regex) {
		List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();
		List<Map<String, String>> valueListNew = new ArrayList<Map<String, String>>();

		StringBuffer bufferInfo = new StringBuffer();
		bufferInfo = buffer;
		Scanner scan = new Scanner(new InputStreamReader(
				new ByteArrayInputStream(bufferInfo.toString().getBytes())));
		System.out
				.println("*************the result for show ip brief Interface:**********begin**********");
		System.out.println(bufferInfo.toString());
		System.out
			   .println("*************the result for show ip brief Interface:**********end**********");

		List<String> headerList = new ArrayList<String>();
		String header = "";
		while ((header = scan.nextLine()).indexOf(regex) == -1) {
		}
		String headers[] = header.split("\\s\\s+");

		for (String tem : headers) {

			headerList.add(tem);

			System.out.println("headers :" + tem);
		}

		while (scan.hasNext()) {
			String strs[] = scan.nextLine().split("\\s");
			Map<String, String> valueMap = new HashMap<String, String>();
			for (int j = 0; j < strs.length; j++) {
				valueMap.put(headerList.get(j), strs[j]);
				System.out.println(headerList.get(j) + "-->" + strs[j]);
			}

			if (valueMap.get(headerList.get(0)) != null
					&& valueMap.get(headerList.get(1)) != null) {

				valueList.add(valueMap);
			}
		}
		for (int i = 0; i < valueList.size(); i++) {
			Map<String, String> mapNew = new HashMap<String, String>();
			mapNew.put("Name", valueList.get(i).get(headerList.get(0)));

			mapNew.put("Address", valueList.get(i).get(headerList.get(1)));

			mapNew.put("MTU", valueList.get(i).get(headerList.get(2)));

			mapNew.put("State", valueList.get(i).get(headerList.get(3)));
			mapNew.put("Bindings", valueList.get(i).get(headerList.get(4)));
			valueListNew.add(mapNew);
		}
		for (int m = 0; m < headerList.size(); m++) {
			System.out.println("the headList is Empty : "
					+ headerList.isEmpty());

			System.out.println("the headList is to String  :  "
					+ headerList.toString());

			System.out
					.println("the " + m + " headList is " + headerList.get(m));

		}
		try {
			scan.close();
			System.out.println(valueList.size());

		} catch (Exception e) {

			e.printStackTrace();
		}
		return valueListNew;
	}


	public boolean switchContext(String contextName) {
		String[] commands = { "\r", "context " + contextName };
		return getShell().executeCommands(commands);
	}

	public List<Map<String, String>> getHostsByContext() {
		String[] arpCommand = { "\r", "show arp" };
		if (getShell().executeCommands(arpCommand)) {
			return AnalysisArp(shell.getResponse(), "Host");
		}
		return null;
	}

	public List<Map<String, String>> getInterfaceByContext() {
		String[] arpCommand = { "\r", "show ip interface brief" };
		if (getShell().executeCommands(arpCommand)) {
			return AnalysisInterface(shell.getResponse(), "Name");
		}
		return null;
	}

	public boolean pingHost(String hostIP) {
		String[] pingCommand = { "\r", "ping " + hostIP };
		if (getShell().executeCommands(pingCommand)) {
			return AnalysisPing(shell.getResponse());
		}
		return false;
	}

	/*
	 * @Override public void run() { // TODO Auto-generated method stu
	 */

	/**
	 * resultList{{map1{ContextName-a,ip_status{1.1.1.1:true,2.2.2.2-false}}},{
	 * map1{ContextName-b,ip_status{3.3.3.3:true,4.4.4.4-false}}}}
	 */


	public List<Map<String, String>> AnalysisArp(StringBuffer buffer,
			String regex) {

		List<Map<String, String>> valueList = new ArrayList<Map<String, String>>();

		List<Map<String, String>> valueListNew = new ArrayList<Map<String, String>>();

		StringBuffer bufferInfo = new StringBuffer();
		bufferInfo = buffer;
		Scanner scan = new Scanner(new InputStreamReader(
				new ByteArrayInputStream(bufferInfo.toString().getBytes())));
		log.debug(this.getClass(),"********** the result for show arp****  begin *** ");
		System.out.println(bufferInfo);
		
		log.debug(this.getClass(),"********** the result for show arp***** end *** ");	

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
		while ((header = scan.nextLine()).indexOf(regex) == -1) {
		}
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
			mapNew.put("Hardware address",valueList.get(i).get(headerList.get(1)));
			mapNew.put("Ttl", valueList.get(i).get(headerList.get(2)));
			mapNew.put("Type", valueList.get(i).get(headerList.get(3)));			
			mapNew.put("Circuit", valueList.get(i).get(headerList.get(4)));
			mapNew.put("PingStatus", "false");

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
			
			log.debug(this.getClass(),"********** the result for show arp***** begin ***** ");
			System.out.println(bufferInfo);
			
			log.debug(this.getClass(),"********** the result for show arp*****  end  ***** ");	

			Scanner scan = new Scanner(new InputStreamReader(
					new ByteArrayInputStream(bufferInfo.toString().getBytes())));
			scan.nextLine();
			String header = "";
			while ((header = scan.nextLine()) != null) {

				if (header.charAt(0) == '!') {
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

	public void ContextArpInterfaceInit(List<Map<String, String>> contextList) {

		/*
		 * interfaceMap: store the interface information in every context
		 * arpMap: store the host infomation in every context
		 * contextResourceList : the List for context resource contain
		 * arp/interface Author : Jenny Liang DateTime : 2012-08-08
		 */

		List<Map<String, Object>> ArpInterfaceResultList = new ArrayList<Map<String, Object>>();

		// Map<String, Object> resultMap = null;

		// List<Map<String,String>> arpList = null;

		// List<Map<String,String>> interfaceList = null;

		for (int i = 0; i < contextList.size(); i++) {

			Map<String, Object> resultMap = new HashMap<String, Object>();

			List<Map<String, String>> arpList = new ArrayList<Map<String, String>>();

			List<Map<String, String>> interfaceList = new ArrayList<Map<String, String>>();

			String contextName = contextList.get(i).get("Context Name");
			String contextId = contextList.get(i).get("Context ID");

			resultMap.put("Context Name", contextName);
			resultMap.put("Context ID",contextId);

			if (switchContext(contextName)) {

				arpList = getHostsByContext();

				interfaceList = getInterfaceByContext();

				// resultMap.put("InterfaceList",interfaceList);

				if (arpList == null) {

					resultMap.put("Host_List", null);

					System.out.println("---context name: " + contextName
							+ "the arpList is null");

				} else {

					for (int j = 0; j < arpList.size(); j++) {

						if (pingHost(arpList.get(j).get("Host"))) {

							arpList.get(j).remove("PingStatus");
							arpList.get(j).put("PingStatus", "True");
						}
					}
					resultMap.put("Host_List", arpList);
				}

				if (interfaceList == null) {

					resultMap.put("Interface_List", null);

				} else {

					resultMap.put("Interface_List", interfaceList);

				}

				ArpInterfaceResultList.add(resultMap);

				// ------the test program for the arpList------begin-------

				if (arpList == null) {

					System.out
							.println("------------- the arpList is null----------");
				} else {

					for (int m = 0; m < arpList.size(); m++) {

						System.out.println(" the size for arpList : " + m
								+ " is :" + arpList.size());

						Map<String, String> arpMap = new HashMap<String, String>();

						arpMap = arpList.get(m);

						System.out.println(arpMap.get("Host"));
						System.out.println(arpMap.get("Hardware address"));
						System.out.println(arpMap.get("Ttl"));
						System.out.println(arpMap.get("Type"));
						System.out.println(arpMap.get("Circuit"));
						System.out.println(arpMap.get("PingStatus"));
					}
				}

				// ------the test program for the arpList------end----------

				// ------the test program for the
				// interfaceList------begin---------

				if (interfaceList == null) {

					System.out
							.println("------------- the interfaceList is null----------");
				} else {

					for (int k = 0; k < interfaceList.size(); k++) {

						System.out.println(" the size for interface : " + k
								+ " is :" + interfaceList.size());

						Map<String, String> interfaceMap = new HashMap<String, String>();

						interfaceMap = interfaceList.get(k);

						System.out.println(interfaceMap.get("Name"));
						System.out.println(interfaceMap.get("Address"));
						System.out.println(interfaceMap.get("MTU"));
						System.out.println(interfaceMap.get("State"));
						System.out.println(interfaceMap.get("Bindings"));

					}
					// ------the test program for the
					// interfaceList------end---------
				}

				this.resultList = ArpInterfaceResultList;
			}
		}
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

	public String getHostName() {
		return hostName;
	}

	public List<Map<String, String>> getContextList() {
		return contextList;
	}

	public List<Map<String, Object>> getResultList() {
		return resultList;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		RouterInit();
		StringBuffer hosts = new StringBuffer();
		for (int iContextNum = 0; iContextNum < resultList.size(); iContextNum++) {

			Map<String, Object> rsMap = resultList.get(iContextNum);

			String contextName = (String) rsMap.get("Context Name");

			List<Map<String, String>> interfaceList = new ArrayList<Map<String, String>>();

			List<Map<String, String>> arpList = new ArrayList<Map<String, String>>();

			interfaceList = (List<Map<String, String>>) rsMap.get("Interface_List");

			arpList = (List<Map<String, String>>) rsMap.get("Host_List");


			if (arpList == null) {
				System.out.println("arpList is null");
			} else {
				for (int jHostNum = 0; jHostNum < arpList.size(); jHostNum++) {

					Map<String, String> arpMap = new HashMap<String, String>();

					arpMap = arpList.get(jHostNum);

					if (arpMap.get("PingStatus").equals("false")){
						hosts.append(arpMap.get("Host"));
					}
				}
			}
			
			/**
			 * 告警
			 */
			//得到所有结果为false的主机
				Notifier notify = new Notifier(new MetaPeople(people),
						"host offline",hosts.toString()+" is offline at "+fun.GetCurFormatTime());
				notify.sendNotifier();
		}
	}

	@Override
	public List<Map<String, String>> AnalysisContext(StringBuffer buffer,
			String regex) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPeople() {
		return people;
	}

	public void setPeople(String people) {
		this.people = people;
	}
}

/*
 * System.out.println("**********************结果集BEGIN*****************************"
 * ); System.out.println(resultList.size()); for (Map<String,Object>
 * rsmap:resultList){ //取出结果集的中每个map，map的key分别为：contextname<string>,
 * ip_status<list> String contextName = (String) rsmap.get("ContextName");
 * List<String> ips = (List<String>) rsmap.get("ip_status");
 * //如果这个context没有主机,只打印contextname if (ips == null){
 * System.out.println("["+contextName+"]"+" null hosts"); continue; }
 * //如果这个context有主机,打印contextname--IP-result for (String ipStr: ips){
 * System.out.println("["+contextName+"] -- "+ipStr); } }
 * 
 * System.out.println("**********************结果集END*****************************"
 * );
 * 
 * }
 */

