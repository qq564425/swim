package com.hdnav.entity;

import java.io.Serializable;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             权限实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class Permission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1170359489473929893L;
	
	//权限id
	private Integer id;
	
	//权限名字
	private String name;
	
	//权限key
	private String key;
	
	//上级权限id
	private Integer parentId;
	
	//权限排序
	private Integer order;
	
	//菜单id
	private Integer menuId;
	
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	//是否有权限标识
	private String isExit;
	
	public String getIsExit() {
		return isExit;
	}

	public void setIsExit(String isExit) {
		this.isExit = isExit;
	}

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
	
	public Integer getParentId() {
		return parentId;
	}
	
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public Integer getOrder() {
		return order;
	}
	
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
