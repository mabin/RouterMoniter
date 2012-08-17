package org.rm.bean;

import org.rm.core.basebean;

/**
 * DInterface entity. @author MyEclipse Persistence Tools
 */

public class DInterface extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String address;
	private Integer mtu;
	private String state;
	private String bindings;
	private Integer contextId;

	// Constructors

	/** default constructor */
	public DInterface() {
	}

	/** minimal constructor */
	public DInterface(String name, Integer contextId) {
		this.name = name;
		this.contextId = contextId;
	}

	/** full constructor */
	public DInterface(String name, String address, Integer mtu, String state,
			String bindings, Integer contextId) {
		this.name = name;
		this.address = address;
		this.mtu = mtu;
		this.state = state;
		this.bindings = bindings;
		this.contextId = contextId;
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

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getMtu() {
		return this.mtu;
	}

	public void setMtu(Integer mtu) {
		this.mtu = mtu;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getBindings() {
		return this.bindings;
	}

	public void setBindings(String bindings) {
		this.bindings = bindings;
	}

	public Integer getContextId() {
		return this.contextId;
	}

	public void setContextId(Integer contextId) {
		this.contextId = contextId;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "d_interface";
	}

}