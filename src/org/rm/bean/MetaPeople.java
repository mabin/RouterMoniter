package org.rm.bean;

import org.rm.core.basebean;

/**
 * MetaPeople entity. @author MyEclipse Persistence Tools
 */

public class MetaPeople extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String mobile;
	private String email;
	private Integer device;

	// Constructors

	/** default constructor */
	public MetaPeople() {
	}

	/** full constructor */
	public MetaPeople(String name, String mobile, String email, Integer device) {
		this.name = name;
		this.mobile = mobile;
		this.email = email;
		this.device = device;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getDevice() {
		return this.device;
	}

	public void setDevice(Integer device) {
		this.device = device;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "meta_people";
	}

}