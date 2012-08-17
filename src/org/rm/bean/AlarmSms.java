package org.rm.bean;

import java.sql.Timestamp;

import org.rm.core.basebean;

/**
 * AlarmSms entity. @author MyEclipse Persistence Tools
 */

public class AlarmSms extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private Timestamp smsTime;
	private String smsTitle;
	private String smsMobile;
	private String alarmDevice;
	private String smsSign;
	private String smsStatus;

	// Constructors

	/** default constructor */
	public AlarmSms() {
	}

	/** full constructor */
	public AlarmSms(Timestamp smsTime, String smsTitle, String smsMobile,
			String alarmDevice, String smsSign, String smsStatus) {
		this.smsTime = smsTime;
		this.smsTitle = smsTitle;
		this.smsMobile = smsMobile;
		this.alarmDevice = alarmDevice;
		this.smsSign = smsSign;
		this.smsStatus = smsStatus;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getSmsTime() {
		return this.smsTime;
	}

	public void setSmsTime(Timestamp smsTime) {
		this.smsTime = smsTime;
	}

	public String getSmsTitle() {
		return this.smsTitle;
	}

	public void setSmsTitle(String smsTitle) {
		this.smsTitle = smsTitle;
	}

	public String getSmsMobile() {
		return this.smsMobile;
	}

	public void setSmsMobile(String smsMobile) {
		this.smsMobile = smsMobile;
	}

	public String getAlarmDevice() {
		return this.alarmDevice;
	}

	public void setAlarmDevice(String alarmDevice) {
		this.alarmDevice = alarmDevice;
	}

	public String getSmsSign() {
		return this.smsSign;
	}

	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}

	public String getSmsStatus() {
		return this.smsStatus;
	}

	public void setSmsStatus(String smsStatus) {
		this.smsStatus = smsStatus;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "alarm_sms";
	}

}