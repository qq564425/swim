package com.hdnav.entity;

import java.io.Serializable;
import java.util.List;

/**
 * CopyRright:           海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             菜单实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-5 
*/ 
public class Menu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3198918538148039407L;

	//菜单id
	private Integer id;
	
	//菜单上级id
	private Integer parentId;
	
	//菜单名字
	private String name;
	
	//菜单链接地址
	private String url;
	
	//菜单排序
	private Integer order;
	
	//菜单图片
	private String icon;
	
	//权限对象
	private Permission permission;
	
	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	private MenuPermission menuPermission;
	
	private List<Menu> menuList;
	

    public List<Menu> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<Menu> menuList) {
		this.menuList = menuList;
	}

	public MenuPermission getMenuPermission() {
		return menuPermission;
	}

	public void setMenuPermission(MenuPermission menuPermission) {
		this.menuPermission = menuPermission;
	}

	public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
    	this.id = id;
    }

    public Integer getParentId() {
    	return this.parentId;
    }

    public void setParentId(Integer parentId) {
    	this.parentId = parentId;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
