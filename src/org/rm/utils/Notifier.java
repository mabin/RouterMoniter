package org.rm.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.rm.bean.MetaPeople;
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
	
	
	public Notifier( MetaPeople people, String title,String content) {
		super();
		this.title = title;
		this.content = content;
		initPeopleInfo(people);
		initServerInfo();
	}
	
	public void initPeopleInfo(MetaPeople people){
		dbquery db = new dbquery();
		Connection conn = db.GetCon();
		String querySQL="select * from meta_people where name='"+people.getName()+"'";
		try {
			PreparedStatement pstmt = conn.prepareStatement(querySQL);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				emails.add(rs.getString("Email"));
				phones.add(rs.getString("Mobile"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initServerInfo(){
    	Properties props=new Properties();
        FileInputStream input;
		try {
			input = new FileInputStream(System.getProperty("DBproperties")+"WEB-INF/c3p0.properties");
			props.load(input);
			this.ServerIp = props.getProperty("ServerIp");
			this.ServerPort = Integer.parseInt(props.getProperty("ServerPort"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
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
