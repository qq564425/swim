package com.hdnav.dao;

import java.util.List;

import com.hdnav.entity.DepartmentAccount;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             部门-用户Dao接口     
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣               
 * Create Date：                           2017-6-7 
*/ 
public interface DepartmentAccountMapper {
    /**
     * 查询所有
     * @param 
     * @return list
     */
	List<DepartmentAccount> selectAll();
    /**
     * 查询所有部门ID
     * @param 
     * @return list
     */
	List<Integer> selectAllDepartmentId();
    /**
     * 通过用户Id查询
     * @param accountId 用户id
     * @return DepartmentAccount
     */
	DepartmentAccount getByAccountId(int accountId);
    /**
     * 通过部门Id查询用户Id
     * @param accountId 用户id
     * @return int
     */
    Integer getDepIdByAccountId(int accountId);
    /**
     * 通过部门Id查询关联部门Id
     * @param departmentId 部门id
     * @return int
     */
    List<Integer> getDepIdByDepartmentId(int departmentId);
    /**
     * 通过部门Id删除
     * @param depId 部门id
     * @return int
     */
    int deleteByDepId(int depId);
    /**
     * 通过部门用户Id查询用户Id
     * @param accountId 用户id
     * @return int
     */
    int deleteByAccountId(int accountId);
    /**
     * 保存
     * @param DepartmentAccount 部门用户实体
     * @return int
     */
    int save(DepartmentAccount departmentAccount);
    /**
     * 更新
     * @param DepartmentAccount 部门用户实体
     * @return int
     */
	int update(DepartmentAccount departmentAccount);
}
