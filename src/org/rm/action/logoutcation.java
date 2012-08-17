package org.rm.action;
import org.rm.core.baseaction;
/*
 *注销模块
 */
public class logoutcation extends baseaction {
	public void exe() {
		exe_logout(); 
	}
	//系统注销
	private void exe_logout(){
		if (request.getSession().getAttribute("loginname") != null){
			session.setAttribute("loginname",null);
			session.setAttribute("personid",null);
			session.setAttribute("restempletid",null);
			session.setAttribute("permissionscode",null);
		}	
	} 
}