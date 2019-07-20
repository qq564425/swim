package com.hdnav.entity.vo;

import javax.validation.constraints.NotNull;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             登录实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋                
 * Create Date：                             2017-6-5 
*/ 
public class LoginVO {
    @NotNull(message = "账号格式错误")
    private String account;
    @NotNull(message = "密码格式错误")
    private String password;
    /*@NotNull(message = "验证码格式错误")
    private String verifycode;*/
    private boolean rememberMe;

    public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
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

   /* public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
    }*/
}
