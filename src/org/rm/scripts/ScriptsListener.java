package org.rm.scripts;

import java.util.Calendar;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.rm.core.log;

public class ScriptsListener implements ServletContextListener{
	public Timer ScriptTimer = null ;
	public Timer InitTimer = null ;
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		ScriptTimer.cancel();
		InitTimer.cancel();
		System.out.println("ScriptTimer Destroyed at : "+Calendar.getInstance().getTime());
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		ScriptTimer = new Timer(true);
		InitTimer = new Timer(true);
		
		//ping定时器在程序启动后15秒开始执行，每隔1天执行一次。
		InitTimer.schedule(new InitInfos(event.getServletContext()), 15*1000,24*60*60*1000);
		System.out.println("InitTimer Initialized at : "+Calendar.getInstance().getTime());
		
		//ping定时器在程序启动后2分钟开始执行，每隔1个小时执行一次。
		//ScriptTimer.schedule(new ScriptsRun(event.getServletContext()), 10*1000,60*60*1000);
		ScriptTimer.schedule(new ScriptsRun(event.getServletContext()), 2*60*1000,60*60*1000);
		System.out.println("ScriptTimer Initialized at : "+Calendar.getInstance().getTime());
	}

}
