package org.rm.biz;

import java.util.List;

import org.rm.bean.DHost;
import org.rm.bean.MetaContext;
import org.rm.bean.MetaDevice;
import org.rm.core.dbquery;
import org.rm.dao.RouterDAO;

public class RouterBiz {
	public List<MetaDevice> getAllRouter(){
		RouterDAO routerDAO = new RouterDAO();
		return routerDAO.queryAllDevice();
	}
	public int addRouter(MetaDevice device){
		RouterDAO routerDAO = new RouterDAO();
		return routerDAO.insertRouter(device);
	}
	
	
	
}
