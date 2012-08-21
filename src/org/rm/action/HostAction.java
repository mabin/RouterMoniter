package org.rm.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.rm.bean.DHost;
import org.rm.bean.DInterface;
import org.rm.core.baseaction;
import org.rm.core.dbquery;
import org.rm.core.fun;
import org.rm.core.log;
import org.rm.dao.ContextDAO;
import org.rm.dao.HostDAO;
import org.rm.dao.InterfaceDAO;

public class HostAction extends baseaction{
	public DHost queryHostById(int id) {
		HostDAO hostDAO = new HostDAO();
		return hostDAO.queryHostById(id);
	}

	public List<DHost> queryInterfaceAll() {
		HostDAO hostDAO = new HostDAO();
		return hostDAO.queryAllHost();
	}

	public void queryInfoAll(){
		dbquery db = null ;
		Connection conn = null ;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		JSONObject json = null ;
		String fields = "ctx.deviceid, ctx.contextname, ctx.Contextid, ctx.vpnrd, ctx.description, ctx.id as ctxid"
				+", hst.id as hstid, hst.ipaddr, hst.macaddr, hst.ttl, hst.type, hst.circuit, hst.status";
		String querySQL = "SELECT "+fields
				+" FROM meta_context AS ctx , d_host AS hst WHERE ctx.deviceid=?";
		try{
			db = new dbquery();
			conn = db.GetCon();
			pstmt = conn.prepareStatement(querySQL);
			pstmt.setInt(1, fun.parseInt(request.getParameter("deviceid"),0));
			rs = pstmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			
			for (int k=0; k<metaData.getColumnCount(); k++){
				System.out.println("column --> "+metaData.getColumnLabel(k+1));
			}
			
			String[] columns = new String[metaData.getColumnCount()];
			for (int i=0; i<columns.length; i++){
				//System.out.println(metaData.getCatalogName(i+1));
				columns[i] = metaData.getColumnLabel(i+1);
			}
			json = new JSONObject();
			String root = "d_host";
			JSONArray objs = new JSONArray();
			int i = 0;
			while (rs.next()){
				JSONObject obj = new JSONObject();
				for (int m=0; m<columns.length; m++){
					if (columns[m].equals("deviceid") || 
						columns[m].equals("ctxid") ||
						columns[m].equals("hstid")){
					obj.put(columns[m], rs.getInt(columns[m]));
					}else{
						obj.put(columns[m].toLowerCase(), rs.getString(columns[m]));
					}
				}
				objs.put(i++,obj);
			}
			if (i!=0){
				   json.put("success","true");
				   json.put("results",i);
				   json.put(root, objs);
				}else 
				{
					json.put(root, JSONObject.NULL);
				}
			log.debug(this.getClass(),json.toString());
			out.print(json);
		}catch(Exception e){
			e.printStackTrace();
		}
	
	}

	@Override
	public void exe() {
		// TODO Auto-generated method stub
		if (request.getParameter("actioncmd").equals("queryall"))
			queryInfoAll();
	}
}
