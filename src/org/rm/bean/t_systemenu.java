package org.rm.bean;

import org.rm.core.basebean;

public class t_systemenu extends basebean {
	private int id = 0;
	private int parentid = 0;
	private String code = "";
	private String name = "";
	private String description = "";
	private String guid = "";
	private String jsurl = "";
	private String jscom = "";

	private String jspurl = "";
	private String sysflg = "";
	private String validstate = "";
	private int sequence = 0;
	private String iconCls = "";
	private String xtype = "";
	private String parentcode="";

	public String getJscom() {
		return jscom;
	}

	public void setJscom(String jscom) {
		this.jscom = jscom;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setParentid(int parentid) {
		this.parentid = parentid;
	}

	public int getParentid() {
		return parentid;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getGuid() {
		return guid;
	}

	public void setJsurl(String jsurl) {
		this.jsurl = jsurl;
	}

	public String getJsurl() {
		return jsurl;
	}

	public void setJspurl(String jspurl) {
		this.jspurl = jspurl;
	}

	public String getJspurl() {
		return jspurl;
	}

	public void setSysflg(String sysflg) {
		this.sysflg = sysflg;
	}

	public String getSysflg() {
		return sysflg;
	}

	public void setValidstate(String validstate) {
		this.validstate = validstate;
	}

	public String getValidstate() {
		return validstate;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getSequence() {
		return sequence;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	public String getXtype() {
		return xtype;
	}

	public String getTableName() {
		return "t_systemenu";
	}

	public void setParentcode(String parentcode) {
		this.parentcode = parentcode;
	}

	public String getParentcode() {
		return parentcode;
	}

 
}
