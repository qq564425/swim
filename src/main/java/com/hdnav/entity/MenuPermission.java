package com.hdnav.entity;

import java.io.Serializable;

/**
 * CopyRright:           海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             菜单权限实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-5 
*/ 
public class MenuPermission implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5249747229120555442L;

	//菜单权限id
	private Integer id;
	
	//菜单id
	private Integer menuId;
	
	//权限id
	private Integer permissionId;
	
	private Permission permission;
	

    public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
    	this.menuId = menuId;
    }

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
    	this.permissionId = permissionId;
    }
}
