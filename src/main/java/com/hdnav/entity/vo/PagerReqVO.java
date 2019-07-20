package com.hdnav.entity.vo;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             分页查询实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-5 
*/ 
public class PagerReqVO {
    private int limit;//分页中一页的数量
    private int offset;//偏移量
    private String order;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

	public int getPageNo() {
		return offset/limit+1;
	}
    
}
