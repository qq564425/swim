package com.hdnav.entity.vo;

import javax.validation.constraints.NotNull;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             创建用户的实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-5 
*/ 
public class UserCreateVO {
    @NotNull(message = "账号格式错误")
    private String account;
    @NotNull(message = "密码格式错误")
    private String  password;
    @NotNull(message = "请选择部门")
    private Integer dep;

    public Integer getDep() {
        return dep;
    }

    public void setDep(Integer dep) {
        this.dep = dep;
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
}

