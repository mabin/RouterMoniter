package org.rm.bean;

import java.sql.Timestamp;

import org.rm.core.basebean;

/**
 * MetaDevice entity. @author MyEclipse Persistence Tools
 */

public class MetaDevice extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private String deviceName;
	private Integer deviceType;
	private String hostname;
	private String deviceIp;
	private String loginWay;
	private String loginName;
	private String password;
	private Integer status;
	private Timestamp lastOnline;
	private String devicePath;
	private String deviceParent;
	private String deviceInfo;
	private String devicePurpose;
	private String location;
	private String deviceDep;
	private String onlineHosts;

	// Constructors

	/** default constructor */
	public MetaDevice() {
	}

	/** full constructor */
	public MetaDevice(String deviceName, Integer deviceType, String hostname,
			String deviceIp, String loginWay, String loginName,
			String password, Integer status, Timestamp lastOnline,
			String devicePath, String deviceParent, String deviceInfo,
			String devicePurpose, String location, String deviceDep,
			String onlineHosts) {
		this.deviceName = deviceName;
		this.deviceType = deviceType;
		this.hostname = hostname;
		this.deviceIp = deviceIp;
		this.loginWay = loginWay;
		this.loginName = loginName;
		this.password = password;
		this.status = status;
		this.lastOnline = lastOnline;
		this.devicePath = devicePath;
		this.deviceParent = deviceParent;
		this.deviceInfo = deviceInfo;
		this.devicePurpose = devicePurpose;
		this.location = location;
		this.deviceDep = deviceDep;
		this.onlineHosts = onlineHosts;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceName() {
		return this.deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Integer getDeviceType() {
		return this.deviceType;
	}

	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getDeviceIp() {
		return this.deviceIp;
	}

	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getLoginWay() {
		return this.loginWay;
	}

	public void setLoginWay(String loginWay) {
		this.loginWay = loginWay;
	}

	public String getLoginName() {
		return this.loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getLastOnline() {
		return this.lastOnline;
	}

	public void setLastOnline(Timestamp lastOnline) {
		this.lastOnline = lastOnline;
	}

	public String getDevicePath() {
		return this.devicePath;
	}

	public void setDevicePath(String devicePath) {
		this.devicePath = devicePath;
	}

	public String getDeviceParent() {
		return this.deviceParent;
	}

	public void setDeviceParent(String deviceParent) {
		this.deviceParent = deviceParent;
	}

	public String getDeviceInfo() {
		return this.deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public String getDevicePurpose() {
		return this.devicePurpose;
	}

	public void setDevicePurpose(String devicePurpose) {
		this.devicePurpose = devicePurpose;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDeviceDep() {
		return this.deviceDep;
	}

	public void setDeviceDep(String deviceDep) {
		this.deviceDep = deviceDep;
	}

	public String getOnlineHosts() {
		return this.onlineHosts;
	}

	public void setOnlineHosts(String onlineHosts) {
		this.onlineHosts = onlineHosts;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "meta_device";
	}

}