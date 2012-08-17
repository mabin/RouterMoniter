package org.rm.action;

import java.util.List;

import org.rm.bean.MetaContext;
import org.rm.dao.ContextDAO;

public class ContextAction {
	public MetaContext queryContextById(int id){
		ContextDAO contextDAO = new ContextDAO();
		return contextDAO.queryContextById(id);
	}
	
	public List<MetaContext> queryContextAll(){
		ContextDAO contextDAO = new ContextDAO();
		return contextDAO.queryAllContext();
	}
	
	
}
