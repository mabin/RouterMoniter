package org.rm.core;
import org.apache.log4j.Logger;
/**
 *
 * <p>Title: 在整个项目中都调用这个错误类来写入日志</p>
 
 */
public  class  log {
  public log() {
  }
  @SuppressWarnings("unchecked")
public static final void error(Class thisclasss,String DebugMessage){
	  Logger log = Logger.getLogger(thisclasss.getName());    
	  log.info("[error]"+DebugMessage); 
  }
  public static final void error(String DebugMessage){
	  Logger log = Logger.getLogger(DebugMessage);    
	  log.info("[error]"+DebugMessage); 
  }
  @SuppressWarnings("unchecked")
public static final void info(Class thisclasss,String DebugMessage){
	  Logger log = Logger.getLogger(thisclasss.getName());    
	  log.info(DebugMessage);
	 
  }
  public static final void info(String DebugMessage){
	  Logger log = Logger.getLogger(DebugMessage);    
	  log.info(DebugMessage);
  }
  @SuppressWarnings("unchecked")
public static final void debug(Class thisclasss ,String DebugMessage){
	  Logger log = Logger.getLogger(thisclasss.getName());   
	  log.info("[debug]"+DebugMessage); 
  }
  public static final void debug(String DebugMessage){
	   
	  Logger log = Logger.getLogger("debug");
	    
	 log.info(DebugMessage); 
	 
  }
}

