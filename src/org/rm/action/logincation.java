package org.rm.action;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.rm.core.baseaction;
import org.rm.core.dbquery;
import org.rm.core.fun;
import org.rm.core.log;
import org.rm.utils.MD5;

/*
 * 登录模块
 */
public class logincation extends baseaction {
	public void exe() {
		String LoginName = fun.nil(request.getParameter("loginname"), "");
		String Password = fun.nil(request.getParameter("password"), "");

		 if (isLoginOK(LoginName,Password)==true){
			 sendRedirect("pages/Main.jsp");
			 return;
			}
		 else {sendRedirect("index.jsp?error=1");return;}
	}
	private void sendRedirect(String url){
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			log.error("logincation.exe():"+e.toString());
			 
		} 
	 
	}
	private boolean isLoginOK(String aLoginName,String aPassWord){ 
		String SQLSTR=" SELECT id,username,password FROM meta_user WHERE username='"+aLoginName+"' ";
		
		Statement st = null;
		ResultSet rs = null;
		Connection con=null;
		dbquery db=null;       
		db = new dbquery();
	    con=db.GetCon();
	    try {
			st=con.createStatement();
			rs = st.executeQuery(SQLSTR);
	        if (rs.next()){
	        	session.setAttribute("personid",rs.getInt("id"));//登录用户的ID
	         	session.setAttribute("loginname",rs.getString("username"));//登录用户的名称
			    return true;
			    }
	        
	    } catch (SQLException e) {
			e.printStackTrace();
		}finally{
			db.freecon(con, st, rs);
			db=null;
		}
		return false;
	}
}