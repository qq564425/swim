package com.hdnav.entity;

import java.io.Serializable;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             角色实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class Role implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6876141231572786986L;
	
	//角色id
	private Integer id;
	
	//角色名字
	private String name;
	
	//角色key
	private String key;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
