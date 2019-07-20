package com.hdnav.entity.vo;

/**
 * CopyRright:           海大船舶导航                         
 * Module ID:            authority-manamement   
 * Comments:             分页实体类for VTMIS Server        
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋              
 * Create Date：                             2017-7-3 
*/ 
public class PageVo {

	private Integer limit;//分页中一页的数量
    private Integer offset;//偏移量
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
}
