<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.rm.biz.*"  %>
<%@ page import="org.rm.bean.*"  %>
<%@ page import="java.util.*"  %>
<%@ page import="org.rm.core.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<center>
<br>
  <%
  	RouterBiz routerBiz = new RouterBiz();
  	List<MetaDevice> devices = routerBiz.getAllRouter();
  	out.print("<h2>共有"+devices.size()+"台主机</h2><hr>");
  	%>
  	
  	<table border=1>
  		<colgroup>
		<col style="width:50px;"></col>
		<col style="width:80px;"></col>
		<col style="width:400px;"></col>
	</colgroup>
  		<tr>
  			<td>序号</td><td>IP</td><td>在线主机</td>
  		</tr><tr>
	  		<% 
	  		for (int i=0; i<devices.size(); i++){
	  			MetaDevice device = devices.get(i);	
	  			
	  			out.print("<tr><td>"+device.getId()+"</td>");
	  			out.print("<td>"+device.getDeviceIp()+"</td>");
	  			String hosts = fun.nil(device.getOnlineHosts(), " ");
	  			out.print("<td>"+hosts.replace(",", "\n")+"</td>");
	  			out.print("<td>"+"编辑"+"</td></tr>");
	  		}
	  		%>
  		</tr>
  	</table>
  
</center>
</body>
</html> 