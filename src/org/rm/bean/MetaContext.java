package org.rm.bean;

import org.rm.core.basebean;

/**
 * MetaContext entity. @author MyEclipse Persistence Tools
 */

public class MetaContext extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer deviceId;
	private String contextName;
	private String contextId;
	private String vpnRD;
	private String description;

	// Constructors

	/** default constructor */
	public MetaContext() {
	}

	/** full constructor */
	public MetaContext(Integer deviceId, String contextName, String contextId,
			String vpnRD, String description) {
		this.deviceId = deviceId;
		this.contextName = contextName;
		this.contextId = contextId;
		this.vpnRD = vpnRD;
		this.description = description;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getContextName() {
		return this.contextName;
	}

	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	public String getContextId() {
		return this.contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}


	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "meta_context";
	}

	public String getVpnRD() {
		return vpnRD;
	}

	public void setVpnRD(String vpnRD) {
		this.vpnRD = vpnRD;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}