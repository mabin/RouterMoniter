package org.rm.bean;

import org.rm.core.basebean;

/**
 * MetaDevicetype entity. @author MyEclipse Persistence Tools
 */

public class MetaDevicetype extends basebean implements java.io.Serializable {

	// Fields

	private Integer id;
	private String typeName;

	// Constructors

	/** default constructor */
	public MetaDevicetype() {
	}

	/** full constructor */
	public MetaDevicetype(String typeName) {
		this.typeName = typeName;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return "meta_devicetype";
	}

}