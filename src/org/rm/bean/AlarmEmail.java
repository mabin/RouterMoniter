package org.rm.bean;

import java.sql.Timestamp;

import org.rm.core.basebean;

/**
 * AlarmEmail entity. @author MyEclipse Persistence Tools
 */

public class AlarmEmail extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private Timestamp emailTime;
	private String smsTitle;
	private String emailAddr;
	private String alarmDeviceInfo;
	private String emailSign;
	private String emailStatus;

	// Constructors

	/** default constructor */
	public AlarmEmail() {
	}

	/** full constructor */
	public AlarmEmail(Timestamp emailTime, String smsTitle, String emailAddr,
			String alarmDeviceInfo, String emailSign, String emailStatus) {
		this.emailTime = emailTime;
		this.smsTitle = smsTitle;
		this.emailAddr = emailAddr;
		this.alarmDeviceInfo = alarmDeviceInfo;
		this.emailSign = emailSign;
		this.emailStatus = emailStatus;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getEmailTime() {
		return this.emailTime;
	}

	public void setEmailTime(Timestamp emailTime) {
		this.emailTime = emailTime;
	}

	public String getSmsTitle() {
		return this.smsTitle;
	}

	public void setSmsTitle(String smsTitle) {
		this.smsTitle = smsTitle;
	}

	public String getEmailAddr() {
		return this.emailAddr;
	}

	public void setEmailAddr(String emailAddr) {
		this.emailAddr = emailAddr;
	}

	public String getAlarmDeviceInfo() {
		return this.alarmDeviceInfo;
	}

	public void setAlarmDeviceInfo(String alarmDeviceInfo) {
		this.alarmDeviceInfo = alarmDeviceInfo;
	}

	public String getEmailSign() {
		return this.emailSign;
	}

	public void setEmailSign(String emailSign) {
		this.emailSign = emailSign;
	}

	public String getEmailStatus() {
		return this.emailStatus;
	}

	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "alarm_email";
	}

}