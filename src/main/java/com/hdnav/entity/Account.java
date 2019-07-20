package com.hdnav.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             账户实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋                
 * Create Date：                             2017-6-5 
*/ 
public class Account implements Serializable {

	private static final long serialVersionUID = 1482095198236672573L;

	//account.id (用户id)
	private Integer id;
	
	//account.account (账号)
	private String account;
	
	//account.password (密码)
	private String password;
	
	//account.register_time (注册时间)
	private Date registerTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	
}
