package org.rm.action;

import java.util.List;

import org.rm.bean.DInterface;
import org.rm.dao.ContextDAO;
import org.rm.dao.InterfaceDAO;

public class InterfaceAction {
	public DInterface queryContextById(int id){
		InterfaceDAO interfaceDAO = new InterfaceDAO();
		return interfaceDAO.queryInterfaceById(id);
	}
	
	public List<DInterface> queryInterfaceAll(){
		InterfaceDAO interfaceDAO = new InterfaceDAO();
		return interfaceDAO.queryAllInterface();
	}
	
	
}
