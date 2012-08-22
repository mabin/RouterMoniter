package org.rm.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.rm.core.dbquery;
import org.rm.core.fun;


public class Notifier {
	String ServerIp="";
	int ServerPort = 0;
	String message = "";
	List<String> emails = new ArrayList<String>();
	List<String> phones = new ArrayList<String>();
	String title = "" ;
	String content = "";
	
	
	public Notifier(String serverIp, int serverPort, String message,
			List<String> emails, List<String> phones, String title,
			String content) {
		super();
		ServerIp = serverIp;
		ServerPort = serverPort;
		this.message = message;
		this.emails = emails;
		this.phones = phones;
		this.title = title;
		this.content = content;
	}

	public void sendNotifier(){
		//得到需要发送的XML消息
		message = NotifyXML.GenerateXML(emails, phones, content, title);
		//调用发送方法
		send();
	}
	
	public void send(){
		Thread sendTreahd = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Socket sendSocket = new Socket(ServerIp,ServerPort);
					PrintWriter send = new PrintWriter(sendSocket.getOutputStream());
					send.write(message);
					send.flush();
					
					BufferedReader recive = new BufferedReader(new InputStreamReader(sendSocket.getInputStream(),"UTF-8"));
					StringBuffer buffer = new StringBuffer();
					String line="";
					while ((line=recive.readLine()) != null){
						buffer.append(line);
					}
					
					message = buffer.toString();
					send.close();
					recive.close();
					sendSocket.close();
					
					//将返回的数据写入数据库
					Map<String,Object> result = NotifyXML.analysisXML(message);
					dbquery db = new dbquery();
					Connection conn = db.GetCon();
					String insertSQL = "insert into alarm set title=?,content=?,email=?,phone=?,sent=?,unsent=?,time=?";
					PreparedStatement pstmt = conn.prepareStatement(insertSQL);
					pstmt.setString(1, title);
					pstmt.setString(2, content);
					pstmt.setString(3, emails.toString());
					pstmt.setString(4, phones.toString());
					pstmt.setString(5, result.get("emailSentList").toString()+result.get("phoneSentList").toString());
					pstmt.setString(6, result.get("emailUnSentList").toString()+result.get("phoneUnSentList").toString());
					pstmt.setString(7, fun.GetCurFormatTime());
					pstmt.executeUpdate();
					pstmt.close();
					conn.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
		sendTreahd.start();
	}
}
