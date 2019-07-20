package com.hdnav.entity.vo;

import java.util.List;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             返回时使用的分页实体类        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-5 
*/ 
public class Pager<T> {
    private long total=0;//总数据条数
    private List<T> rows;//具体数据
    public Pager(List<T> list,long l){
    	this.setRows(list);
    	if(list!=null)
    	this.setTotal(l);
    } 
    
    public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
