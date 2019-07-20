package com.hdnav.entity;

import java.io.Serializable;

public class Department implements Serializable {

    private static final long serialVersionUID = 152085276802161751L;

    //主键
    private Integer id;

    //上级部门id
    private Integer parentId;

    //部门名字
    private String name;

    //排序
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

    /**
    * get主键
    * @param @return   
    * @return Integer 
    */
    public Integer getId(){
        return id;
    }

    /**
    * set主键
    * @param id   
    * @return void
    */
    public void setId(Integer id){
        this. id = id;
    }

    /**
    * get上级部门id
    * @param @return   
    * @return Integer 
    */
    public Integer getParentId(){
        return parentId;
    }

    /**
    * set上级部门id
    * @param parentId   
    * @return void
    */
    public void setParentId(Integer parentId){
        this. parentId = parentId;
    }

    /**
    * get部门名字
    * @param @return   
    * @return String 
    */
    public String getName(){
        return name;
    }

    /**
    * set部门名字
    * @param name   
    * @return void
    */
    public void setName(String name){
        this. name = name;
    }

    /**
    * get排序
    * @param @return   
    * @return Integer 
    */
    public Integer getOrder(){
        return order;
    }

    /**
    * set排序
    * @param order   
    * @return void
    */
    public void setOrder(Integer order){
        this. order = order;
    }

    /**
    * get行政级别代码 1:总站级，2：站级，4：科队级
    * @param @return   
    * @return String 
    */
    public String getAdministrationLevel(){
        return administrationLevel;
    }

    /**
    * set行政级别代码 1:总站级，2：站级，4：科队级
    * @param administrationLevel   
    * @return void
    */
    public void setAdministrationLevel(String administrationLevel){
        this. administrationLevel = administrationLevel;
    }

    /**
    * get业务级别代码 1:总站级，2：站级，4：科队级
    * @param @return   
    * @return String 
    */
    public String getBusinessLevel(){
        return businessLevel;
    }

    /**
    * set业务级别代码 1:总站级，2：站级，4：科队级
    * @param businessLevel   
    * @return void
    */
    public void setBusinessLevel(String businessLevel){
        this. businessLevel = businessLevel;
    }

    /**
    * get使用标记 0：禁用，1：可用
    * @param @return   
    * @return String 
    */
    public String getUseMark(){
        return useMark;
    }

    /**
    * set使用标记 0：禁用，1：可用
    * @param useMark   
    * @return void
    */
    public void setUseMark(String useMark){
        this. useMark = useMark;
    }

    /**
    * get录入人
    * @param @return   
    * @return String 
    */
    public String getInputMan(){
        return inputMan;
    }

    /**
    * set录入人
    * @param inputMan   
    * @return void
    */
    public void setInputMan(String inputMan){
        this. inputMan = inputMan;
    }

    /**
    * get录入时间
    * @param @return   
    * @return String 
    */
    public String getInputTime(){
        return inputTime;
    }

    /**
    * set录入时间
    * @param inputTime   
    * @return void
    */
    public void setInputTime(String inputTime){
        this. inputTime = inputTime;
    }

}