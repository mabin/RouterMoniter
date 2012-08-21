package org.rm.scripts;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.rm.bean.MetaDevice;
import org.rm.biz.PingBiz;
import org.rm.biz.RouterBiz;
import org.rm.core.fun;
import org.rm.core.log;
import org.rm.utils.VituralConsole;

public class InitInfos extends TimerTask{
	public ServletContext  sc = null ;
	
	public InitInfos(ServletContext sc){
		this.sc = sc ;
	}

	public List<MetaDevice> getDevicesInfo(){
		RouterBiz router = new RouterBiz(); 
		return router.getAllRouter();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Run Init Method");
		List<MetaDevice> allDevice = getDevicesInfo();
		System.out.println("init run method router size : "+allDevice.size());
		//脚本名称
		PingBiz biz = null;
		for (int i=0; i<allDevice.size(); i++){
			//多线程时一次取出5个device
			MetaDevice device1 = allDevice.get(i);
			System.out.println("init run method router ip : "+device1.getDeviceIp());
			
			VituralConsole shell = new VituralConsole(device1.getDeviceIp(),22,device1.getLoginName(),device1.getPassword());
			System.out.println("init run method shell is online: "+shell.isOnline());
			if (!shell.isOnline()){
				//设备不在线,改变状态
				device1.setStatus(-1);
				//调用DAO，修改device状态
				biz = new PingBiz();
				//获取脚本的resultList，修改状态
				//biz.updateRouterInfo(resultList);
				log.error(this.getClass(), "Device timeout : "+device1.getDeviceIp());
				continue;//返回循环，开始登陆下一台设备
			}
			device1.setStatus(1);
			device1.setLastOnline(fun.GetCurFormatTime());
			biz = new PingBiz();
			////调用biz，修改device状态
			if (biz.updateRouterInfo(device1) != -1){
				log.debug(this.getClass(),"update router success");
				
				//通过脚本获取到contextList,格式List<Map<String,String>>
				//通过脚本获取到HIMapList,格式List<Map<String,Object>>,将文档"路由巡检list定义0818.docx"中2的resultList名字改为hiMapList
				//H表示Host，I表示Interface，map代表这是context 与 （host，interface)的映射list，原来的名字容易引起歧义
				//将脚本获取到的两个List及router id同时传入biz层中的update方法
				List<Map<String,String>> contextList = new ArrayList<Map<String,String>>();
				List<Map<String,Object>> hiMapList = new ArrayList<Map<String,Object>>();
				biz.updateContextInfo(contextList, hiMapList, device1.getId());
				
				
				
			}else{
				log.error(this.getClass(),"update router info failure");
			}
			
		}
	}
	
	
}
