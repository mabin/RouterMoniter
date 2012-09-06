package org.rm.scripts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.rm.bean.MetaDevice;
import org.rm.bean.MetaPeople;
import org.rm.biz.PingBiz;
import org.rm.biz.RouterBiz;
import org.rm.core.fun;
import org.rm.core.log;
import org.rm.dao.RouterDAO;
import org.rm.utils.Notifier;
import org.rm.utils.VituralConsole;

public class InitInfos {

	private List<Map<String, String>> contextList = new ArrayList<Map<String, String>>();

	private List<Map<String, Object>> hiMapList = new ArrayList<Map<String, Object>>();

	private String hostname = "unknown";

	private MetaDevice dev = null;

	public InitInfos(MetaDevice dev) {
		this.dev = dev;
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("**********InitInfo.Run Init Method**********");
		// 脚本名称
		PingBiz biz = null;
		System.out.println("init run method router ip : " + dev.getDeviceIp());

		VituralConsole shell = new VituralConsole(dev.getDeviceIp(), 22,
				dev.getLoginName(), dev.getPassword());
		System.out.println("init run method shell is online: "
				+ shell.isOnline());
		if (!shell.isOnline()) {
			// 设备不在线,改变状态
			dev.setStatus(-1);
			// 调用DAO，修改device状态
			RouterDAO routerdao = new RouterDAO();
			routerdao.updateRouter(dev);
			//biz = new PingBiz();
			// 获取脚本的resultList，修改状态
			// biz.updateRouterInfo(resultList);
			Notifier notify = new Notifier(new MetaPeople(dev.getDevicePurpose()),
					"router offline",dev.getDeviceIp()+" is offline at "+fun.GetCurFormatTime());
			notify.sendNotifier();
			log.error(this.getClass(), "Device timeout : " + dev.getDeviceIp());
		}
		dev.setStatus(1);
		dev.setLastOnline(fun.GetCurFormatTime());
		biz = new PingBiz();
		// //调用biz，修改device状态
		if (biz.updateRouterInfo(dev) != -1) {
			log.debug(this.getClass(), "update router success");
			RouterInit routerInit = new RouterInit();
			System.out.println("RouterPing ping = new RouterPing(); ");
			routerInit.setShell(shell);

			routerInit.run();

			int routerID = dev.getId();

			System.out.println("int routerID = device1.getId();" + routerID);

			/**
			 * 将初始化后的结果返回给调用的action
			 */
			setHostname(routerInit.getHostName());

			setContextList(routerInit.getContextList());

			setHiMapList(routerInit.getResultList());

			// biz.updateContextInfo(contextList, hiMapList, dev.getId());

			// biz.updateContextInfo(/*这个参数为context的结果集List<Map<String,Object>>*/ping.getContextList(),ping.getResultList(),routerID);

			// 调用biz，修改Host状态
			// biz.updateHostInfo(resultList,contextID);
		} else {
			log.error(this.getClass(), "update router info failure");
		}

	}

	public List<Map<String, String>> getContextList() {
		return contextList;
	}

	public void setContextList(List<Map<String, String>> contextList) {
		this.contextList = contextList;
	}

	public List<Map<String, Object>> getHiMapList() {
		return hiMapList;
	}

	public void setHiMapList(List<Map<String, Object>> hiMapList) {
		this.hiMapList = hiMapList;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}
