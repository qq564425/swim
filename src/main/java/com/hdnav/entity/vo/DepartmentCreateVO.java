package com.hdnav.entity.vo;

import javax.validation.constraints.NotNull;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             创建部门的实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋                
 * Create Date：                             2017-6-5 
*/ 
public class DepartmentCreateVO {
    @NotNull(message = "上级部门不能为空")
    private Integer parentId;
    @NotNull(message = "部门名字不能为空")
    private String name;
    @NotNull(message = "部门排序不能为空")
    private Integer order;
    //行政级别代码 1:总站级，2：站级，4：科队级
    private String administrationLevel;

    //业务级别代码 1:总站级，2：站级，4：科队级
    private String businessLevel;

    //使用标记 0：禁用，1：可用
    private String useMark;

    //录入人
    private String inputMan;

    //录入时间
    private String inputTime;

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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

	public String getAdministrationLevel() {
		return administrationLevel;
	}

	public void setAdministrationLevel(String administrationLevel) {
		this.administrationLevel = administrationLevel;
	}

	public String getBusinessLevel() {
		return businessLevel;
	}

	public void setBusinessLevel(String businessLevel) {
		this.businessLevel = businessLevel;
	}

	public String getUseMark() {
		return useMark;
	}

	public void setUseMark(String useMark) {
		this.useMark = useMark;
	}

	public String getInputMan() {
		return inputMan;
	}

	public void setInputMan(String inputMan) {
		this.inputMan = inputMan;
	}

	public String getInputTime() {
		return inputTime;
	}

	public void setInputTime(String inputTime) {
		this.inputTime = inputTime;
	}
    
}
