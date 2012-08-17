package org.rm.scripts;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import javax.servlet.ServletContext;

import org.rm.bean.MetaDevice;
import org.rm.biz.PingBiz;
import org.rm.biz.RouterBiz;
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
			device1.setLastOnline(new Timestamp(Calendar.getInstance().getTimeInMillis()));
			biz = new PingBiz();
			////调用biz，修改device状态
			if (biz.updateRouterInfo(device1) != -1){
				log.debug(this.getClass(),"update router success");
				//调用biz，修改Context状态
				//int contextID = biz.updateContextInfo(resultList,routerID);
				//调用biz，修改Host状态
				//biz.updateHostInfo(resultList,contextID);
			}else{
				log.error(this.getClass(),"update router info failure");
			}
			
		}
	}
	
	
}
