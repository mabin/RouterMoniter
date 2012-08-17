/*发送命令类*/
package org.rm.core;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class   servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void init() throws ServletException {
		System.setProperty("DBproperties", this.getServletContext().getRealPath("/"));
		log.info(getClass(), "启动 servlet...");
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getParameter("actionname") != null) {
			org.rm.core.baseaction action = null;
			//得到action的名称
			String actionname = request.getParameter("actionname");
			response.setContentType("text/html; charset=UTF-8");
			log.debug(this.getClass(),actionname);
			try {
				// 通过 前端传入的actionname参数来确认调用那个action (创建一个action实例)
				action = (org.rm.core.baseaction) Class.forName(actionname).newInstance();
				action.setRequest(request);
				action.setResponse(response);
				// 所有action控制类都这个抽象方法
				action.exe();
			} catch (Exception ex) {
				log.error(getClass(), ex.toString());
			}
	        finally
	        {     //这里不用了标记出对象为不可达，方便GC回收
	        	    actionname=null; 
	        	    action=null;
	        }
		}
	}

	public void destroy() {
		log.info(getClass(), "关闭 servlet...");
	}
}
