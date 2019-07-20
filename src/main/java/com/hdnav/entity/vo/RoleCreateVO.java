package com.hdnav.entity.vo;

import javax.validation.constraints.NotNull;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             角色创建实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/ 
public class RoleCreateVO {
    @NotNull(message = "角色名字不能为空")
    private String name;
    @NotNull(message = "角色键值不能为空")
    private String key;
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
}
