package com.hdnav.entity;

import java.io.Serializable;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             账户角色实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋                
 * Create Date：                             2017-6-5 
*/ 
public class AccountRole implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1783507775211175129L;

	/**
	 * 表字段定义静态类
	 */
	
	//account_role.id (用户角色id)
	private Integer id;
	
	//account_role.account_id (账号id)
	private Integer accountId;
	
	//account_role.role_id (角色id)
	private Integer roleId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
