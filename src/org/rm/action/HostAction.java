package org.rm.action;

import java.util.List;

import org.rm.bean.DHost;
import org.rm.bean.DInterface;
import org.rm.dao.ContextDAO;
import org.rm.dao.HostDAO;
import org.rm.dao.InterfaceDAO;

public class HostAction {
	public DHost queryHostById(int id){
		HostDAO hostDAO = new HostDAO();
		return hostDAO.queryHostById(id);
	}
	
	public List<DHost> queryInterfaceAll(){
		HostDAO hostDAO = new HostDAO();
		return hostDAO.queryAllHost();
	}
	
	
}
