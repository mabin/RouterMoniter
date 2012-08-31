package org.rm.action;

import java.sql.SQLException;
import java.util.List;

import org.json.JSONObject;
import org.rm.bean.MetaContext;
import org.rm.core.baseaction;
import org.rm.core.dbquery;
import org.rm.core.log;
import org.rm.core.result;
import org.rm.dao.ContextDAO;
import org.rm.utils.DBGridQuery;

public class ContextAction extends baseaction {
	public MetaContext queryContextById(int id){
		ContextDAO contextDAO = new ContextDAO();
		return contextDAO.queryContextById(id);
	}
	
	public List<MetaContext> queryContextAll(){
		ContextDAO contextDAO = new ContextDAO();
		return contextDAO.queryAllContext();
	}
	
	public void querybyrouter(){
		String routerid = (String)request.getParameter("routerid");
		System.out.println("router id = "+routerid);
		JSONObject json = null ;
		DBGridQuery grid = null ;
		try{
			grid = new DBGridQuery();
			json = grid.DBEXTGridResult("select * from meta_context where deviceid='"+routerid+"'", "meta_context");
			log.debug(this.getClass(),json.toString());
			out.print(json);
		}catch(Exception e){
			out.print(result.JSONObjectFailure("context query fail"));
			e.printStackTrace();
		}finally{
			try {
				grid.GetCon().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void insert(){
		MetaContext context = null ;
		dbquery db = null ;
		
		try{
			context = new MetaContext();
			db = new dbquery();
			context.AddFilterFild("id");
			context.setDeviceId(Integer.parseInt(request.getParameter("deviceid")));
			context.setContextName(request.getParameter("contextname"));
			context.setContextId(request.getParameter("contextid"));
			context.setVpnRD(request.getParameter("vpnrd"));
			context.setDescription(request.getParameter("description"));
			if (db.DBinsert(context)){
				out.print(result.JSONObjectSuccess());
				
			}else{
				out.print(result.JSONObjectFailure());
			}
			
		}catch(Exception e){
			context = null;
			e.printStackTrace();
		}finally{
			context = null;
			db = null ;
		}
	}
	
	public void updatebyid(){
		MetaContext context = null ;
		dbquery db = null ;
		
		try{
			context = new MetaContext();
			db = new dbquery();
			String id = request.getParameter("id");
			context.AddFilterFild("id");
			context.setDeviceId(Integer.parseInt(request.getParameter("deviceid")));
			context.setContextName(request.getParameter("contextname"));
			context.setContextId(request.getParameter("contextid"));
			context.setVpnRD(request.getParameter("vpnrd"));
			context.setDescription(request.getParameter("description"));
			String updateSQL = db.GetUpdateSQL(context, "id = '"+id+"'");
			log.debug(this.getClass(),updateSQL);
			if (db.DBExeSQL(updateSQL)){
				out.print(result.JSONObjectSuccess());
			}else{
				out.print(result.JSONObjectFailure("context更新失败"));
			}
			
		}catch(Exception e){
			context = null;
			e.printStackTrace();
		}finally{
			context = null;
			db = null ;
		}
	
		
	}

	@Override
	public void exe() {
		// TODO Auto-generated method stub
		if (request.getParameter("actioncmd").equals("querybyrouter"))
			querybyrouter();
		if (request.getParameter("actioncmd").equals("insert"))
			insert();
		if (request.getParameter("actioncmd").equals("updatebyid"))
			updatebyid();
		
	}
	
	
}
