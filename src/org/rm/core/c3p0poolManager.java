package org.rm.core;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mchange.v2.c3p0.ComboPooledDataSource;
 
public class c3p0poolManager {  
    private static  c3p0poolManager instance = null;  
    ComboPooledDataSource cpdsMaster = null;  
    ComboPooledDataSource cpdsSlave = null;  
    Properties props = null;
    java.net.URL  url=null;
    String filepath=null;
     
    private c3p0poolManager() {  
        try{  
        	url= this.getClass().getResource("/");
         
        	 
        	props=new Properties();
            FileInputStream input = new FileInputStream(System.getProperty("DBproperties")+"WEB-INF/c3p0.properties");   
            props.load(input);
            cpdsMaster = new ComboPooledDataSource();  
            cpdsMaster.setDriverClass(props.getProperty ("Master.driverClass")); //loads the jdbc driver              
            cpdsMaster.setJdbcUrl( props.getProperty ("Master.jdbcUrl") );  
            cpdsMaster.setUser(props.getProperty ("Master.user"));                                    
            cpdsMaster.setPassword(props.getProperty ("Master.password"));  
            cpdsMaster.setMinPoolSize(Integer.parseInt(props.getProperty("Master.minPoolSize"))); 
            cpdsMaster.setMaxPoolSize(Integer.parseInt(props.getProperty("Master.maxPoolSize")));
            cpdsMaster.setAcquireIncrement(Integer.parseInt(props.getProperty("Master.acquireIncrement")));
            cpdsMaster.setAcquireRetryAttempts(Integer.parseInt(props.getProperty("Master.acquireRetryAttempts"))); 
            cpdsMaster.setAcquireRetryDelay(Integer.parseInt(props.getProperty("Master.acquireRetryDelay"))); 
            if (props.getProperty("Master.autoCommitOnClose").equals("true")){
              cpdsMaster.setAutoCommitOnClose(true);
            }else {cpdsMaster.setAutoCommitOnClose(false);}
            cpdsMaster.setIdleConnectionTestPeriod(Integer.parseInt(props.getProperty("Master.idleConnectionTestPeriod"))); 
            cpdsMaster.setInitialPoolSize(Integer.parseInt(props.getProperty("Master.initialPoolSize"))); 
            cpdsMaster.setMaxIdleTime(Integer.parseInt(props.getProperty("Master.maxIdleTime"))); 
            cpdsMaster.setNumHelperThreads(Integer.parseInt(props.getProperty("Master.numHelperThreads"))); 
            
            
        }catch(Exception ex){  
        	log.error(ex.toString());
        }  
    }  
    public static synchronized c3p0poolManager getInstance() {  
        if (instance == null){  
            instance = new c3p0poolManager();  
        }  
        return instance;  
    }  
    public Connection getConnection(Integer i) throws SQLException{  
       
        try{  
        	Connection conn=null; 
        	if (i==1){
                conn = cpdsMaster.getConnection();
        	}
            return conn;  
        }catch(Exception ex){             
            return null;  
        }  
    }  
  
}  
