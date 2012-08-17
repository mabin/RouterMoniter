package org.rm.core;

import org.json.JSONException;
import org.json.JSONObject;

public class result {
	public static final String FailureSession="1000"; //session掉线
	public static final String Success="0";//数据提交成功后的返回值
	public static final String Failure="1";//Session掉线
	public static final String FailureTreeMaxlayer="4";//服务器返回：树节点超过最大层：10层！
	public static final String FailureDataexist="6"; //服务器返回数据是存在的
	public static final String FailureInvoiceMax="7";//发票超过最大金额
	public static final String FailureInvoiceUserNo="8"; //发票输入的用户卡号不存在
	
	public static  JSONObject JSONObjectSuccess(){
		JSONObject obj =new JSONObject(); 
	    try {
			obj.put("success", true);
			obj.put("msg", Success);
		} catch (JSONException e) {
			log.error("result.JSONObjectSuccess:"+e.toString()); 
		}
		return  obj;
	}
	/*
	 * 返回失败操作数据的JSONOBject
	 */
	public static  JSONObject JSONObjectFailure(String msg){
		JSONObject obj =new JSONObject(); 
	    try {
			obj.put("success", false);
			obj.put("msg", msg);
		} catch (JSONException e) {
			 
			log.error("result.JSONObjectFailure:"+e.toString()); 
		}
		return  obj;
	 
	}
	/*
	 * 返回失败操作数据的JSONOBject
	 */
	public static  JSONObject JSONObjectFailure(){
		JSONObject obj =new JSONObject(); 
	    try {
			obj.put("success", false);
			obj.put("msg",  Failure);
		} catch (JSONException e) {
			 
			log.error("result.JSONObjectFailure:"+e.toString()); 
		}
		return  obj;
	 
	}
}
