package org.rm.action;

import org.json.JSONObject;
import org.rm.core.baseaction;
import org.rm.core.log;
import org.rm.utils.DBGridQuery;

public class PeopleAction extends baseaction{
	
	public void queryallpeople(){
		JSONObject json = null ;
		DBGridQuery grid = null ;
		try{
			grid = new DBGridQuery();
			json = new JSONObject();
			json = grid.DBEXTGridResult("select id,name,mobile,email,device from meta_people ", "meta_people");
			log.debug(this.getClass(),json.toString());
			out.print(json);
			
		}catch(Exception e){
			e.printStackTrace();
			log.error(this.getClass(),"querall");
		}finally{
			grid = null ;
		}
	
		
	}

	@Override
	public void exe() {
		// TODO Auto-generated method stub
		if (request.getParameter("actioncmd").equals("query"))
			queryallpeople();
	}

}
