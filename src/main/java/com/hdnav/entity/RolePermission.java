package com.hdnav.entity;

import java.io.Serializable;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             角色权限实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/
public class RolePermission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5068358393863465400L;
	
	//角色权限id
	private Integer id;
	
	//角色id
	private Integer roleId;
	
	//权限id
	private Integer permissionId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}
