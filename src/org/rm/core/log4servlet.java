package org.rm.core;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.PropertyConfigurator;
public class log4servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void init() {
		String prifix = getServletContext().getRealPath("/");
		String file = getInitParameter("log4j-init-file");
		if (file != null) {
			Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(prifix + file));
				prop.setProperty("log4j.appender.R.File", prifix + prop.getProperty("log4j.appender.R.File"));
				PropertyConfigurator.configure(prop);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
	}

}
