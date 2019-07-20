package com.hdnav.entity;

import java.io.Serializable;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             账户部门实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋                
 * Create Date：                             2017-6-5 
*/ 
public class DepartmentAccount implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1698342836359802939L;
	//department_account.id
	private Integer id;
	//department_account.dep_id (部门id)
	private Integer depId;
	//department_account.account_id (账号id)
	private Integer accountId;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDepId() {
		return depId;
	}
	public void setDepId(Integer depId) {
		this.depId = depId;
	}
	public Integer getAccountId() {
		return accountId;
	}
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
