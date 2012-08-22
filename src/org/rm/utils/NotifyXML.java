package org.rm.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class NotifyXML {
	
	public static String GenerateXML(List<String> recipients,List<String> msids,String content,String title){
		DocumentFactory docfac = DocumentFactory.getInstance();
		Document doc = docfac.createDocument("UTF-8");
		Element RequestBlock = doc.addElement("RequestBlock");
		
		//增加邮件块和短信块
		Element Email = RequestBlock.addElement("Email");
		Element Message = RequestBlock.addElement("Message");
		
		//邮件块
		Element Recipients = Email.addElement("Recipients");

		//邮件地址
		for (String rec:recipients){
			Element RecTmp = Recipients.addElement("Recipient");
			RecTmp.setText(rec);
		}
		Element EmailTitle = Recipients.addElement("Title");
		Element EmailContent = Recipients.addElement("Content");		
		EmailTitle.setText(title);
		EmailContent.setText(content);
		//短信块
		Element Msids = Message.addElement("MSIDS");
		//短信地址
		for (String msid:msids){
			Element MsidTmp = Msids.addElement("MSID");
			MsidTmp.setText(msid);
		}
		
		Element  MsgContent = Message.addElement("Content");
		MsgContent.setText("content");

		
		return doc.asXML();
	}
	
	public static Map<String,Object> analysisXML(String xml){
		Map<String,Object> resultMap = null ;
		try {
			Document doc = DocumentHelper.parseText(xml);
			Element root = doc.getRootElement();
			
			String code = root.attributeValue("code"); //根节点状态
			String emailStatus = root.element("Email").attributeValue("status");
			String msgStatus = root.element("Message").attributeValue("status");
			
			List<String> emailUnSentList = new ArrayList<String>();
			List<String> emailSentList = new ArrayList<String>();
			List<String> msgUnSentList = new ArrayList<String>();
			List<String> msgSentList = new ArrayList<String>();
			Iterator<Element> emailSentIter = root.element("Email").element("SentList").elements().iterator();
			while (emailSentIter.hasNext()){
				Element ele = emailSentIter.next();
				emailSentList.add(ele.getTextTrim());
			}
			
			Iterator<Element> emailUnSentIter = root.element("Email").element("UnSentList").elements().iterator();
			while (emailUnSentIter.hasNext()){
				Element ele = emailUnSentIter.next();
				emailUnSentList.add(ele.getTextTrim());
			}
			
			Iterator<Element> msgSentIter = root.element("Message").element("SentList").elements().iterator();
			while (msgSentIter.hasNext()){
				Element ele = msgSentIter.next();
				msgSentList.add(ele.getTextTrim());
			}
			
			Iterator<Element> msgUnSentIter = root.element("Message").element("UnSentList").elements().iterator();
			while (msgUnSentIter.hasNext()){
				Element ele = msgUnSentIter.next();
				msgUnSentList.add(ele.getTextTrim());
			}
			
			resultMap = new HashMap<String,Object>();
			resultMap.put("code", code);//<ResponceBlock code="0">
			resultMap.put("emailStatus", emailStatus);
			resultMap.put("msgStatus", msgStatus);
			resultMap.put("emailUnSentList", emailUnSentList);
			resultMap.put("emailSentList", emailSentList);
			resultMap.put("msgSentList", msgSentList);
			resultMap.put("msgUnSentList", msgUnSentList);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	
	
}
