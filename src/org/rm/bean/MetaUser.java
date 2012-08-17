package org.rm.bean;

import java.sql.Timestamp;

import org.rm.core.basebean;

/**
 * MetaUser entity. @author MyEclipse Persistence Tools
 */

public class MetaUser extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private String realName;
	private String password;
	private String department;
	private String phone;
	private String mobile;
	private String email;
	private Integer status;
	private Timestamp createTime;
	private Timestamp lastLogin;
	private String usePurpose;
	private String remark;

	// Constructors

	/** default constructor */
	public MetaUser() {
	}

	/** minimal constructor */
	public MetaUser(Integer id) {
		this.id = id;
	}

	/** full constructor */
	public MetaUser(Integer id, String realName, String password,
			String department, String phone, String mobile, String email,
			Integer status, Timestamp createTime, Timestamp lastLogin,
			String usePurpose, String remark) {
		this.id = id;
		this.realName = realName;
		this.password = password;
		this.department = department;
		this.phone = phone;
		this.mobile = mobile;
		this.email = email;
		this.status = status;
		this.createTime = createTime;
		this.lastLogin = lastLogin;
		this.usePurpose = usePurpose;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRealName() {
		return this.realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getUsePurpose() {
		return this.usePurpose;
	}

	public void setUsePurpose(String usePurpose) {
		this.usePurpose = usePurpose;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "meta_user";
	}

}