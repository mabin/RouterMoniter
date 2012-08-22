package test.scripts;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.rm.utils.NotifyXML;

public class TestNotify {

	@Test
	public void test() {
		List<String> emailList = new ArrayList<String>();
		List<String> phoneList = new ArrayList<String>();
		
		emailList.add("1");
		emailList.add("2");
		emailList.add("3");
		emailList.add("4");
		emailList.add("5");
		
		phoneList.add("a");
		phoneList.add("b");
		phoneList.add("c");
		phoneList.add("d");
		phoneList.add("e");
		
		String reqxml = NotifyXML.GenerateXML(emailList, phoneList, "content", "title");
		String resxml = "<ResponceBlock code=\"0\"><Email status=\"0\"><SentList><Recipient>user0@example.com</Recipient>"
			+"<Recipient>user1@example.com</Recipient></SentList><UnSentList/></Email><Message status=\"1\">"
			+"<SentList><MSID>13800001000</MSID><MSID>13800001001</MSID></SentList><UnSentList><MSID>13800001002</MSID>"
			+"<MSID>13800001003</MSID></UnSentList></Message></ResponceBlock>";
		Map<String,Object> map = NotifyXML.analysisXML(resxml);
		System.out.println(map.toString());
	}

}
