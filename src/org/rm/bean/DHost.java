package org.rm.bean;

import org.rm.core.basebean;

/**
 * DHost entity. @author MyEclipse Persistence Tools
 */

public class DHost extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private String ipAddr;
	private String macAddr;
	private String ttl;
	private String type;
	private String circuit;
	private Integer contextId;
	private String status;

	// Constructors

	/** default constructor */
	public DHost() {
	}

	/** minimal constructor */
	public DHost(String ipAddr, String macAddr, Integer contextId,String status) {
		this.ipAddr = ipAddr;
		this.macAddr = macAddr;
		this.contextId = contextId;
		this.status = status;
	}

	/** full constructor */
	public DHost(String ipAddr, String macAddr, String ttl, String type,
			String circuit, Integer contextId) {
		this.ipAddr = ipAddr;
		this.macAddr = macAddr;
		this.ttl = ttl;
		this.type = type;
		this.circuit = circuit;
		this.contextId = contextId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIpAddr() {
		return this.ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getMacAddr() {
		return this.macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getTtl() {
		return this.ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCircuit() {
		return this.circuit;
	}

	public void setCircuit(String circuit) {
		this.circuit = circuit;
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
		return "d_host";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}