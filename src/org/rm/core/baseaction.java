package org.rm.core;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.*;

public abstract class baseaction {
	protected HttpServletRequest request = null;
	protected HttpServletResponse response = null;
	protected PrintWriter out = null;
	protected HttpSession session = null;
	public baseaction() {
	}
	public baseaction(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
		this.session = this.request.getSession();
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
		try {
			this.out = this.response.getWriter();
		} catch (IOException e) {
			log.error(this.getClass(), e.toString());
		}
	}
	/**
	 * 
	 * checkSession(检测是否还在线)
	 * 
	 * @return
	 */
	public boolean checkSession() {
		if (request.getSession().getAttribute("loginname") != null)
			return true;
		else {
			log.error(this.getClass(), "Session 掉线..");
			return false;
		}
	}

	 
	public abstract void exe();// 抽象
}
