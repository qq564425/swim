package com.hdnav.entity.vo;

import javax.validation.constraints.NotNull;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             权限编辑实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/ 
public class PermissionEditVO {
    @NotNull(message = "权限id不能为空")
    private Integer id;
    @NotNull(message = "父级权限不能为空")
    private Integer parentId;
    @NotNull(message = "权限名字不能为空")
    private String name;
    @NotNull(message = "权限键值不能为空")
    private String key;
    @NotNull(message = "权限排序不能为空")
    private Integer order;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

