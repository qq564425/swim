package com.hdnav.entity.vo;

import javax.validation.constraints.NotNull;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             编辑用户部门的实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋                
 * Create Date：                             2017-6-5 
*/ 
public class UserEditDepVO {
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    @NotNull(message = "部门id不能为空")
    private Integer depId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }
}
