package com.hdnav.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.hdnav.entity.Account;
import com.hdnav.entity.vo.Pager;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             用户Dao接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣               
 * Create Date：                           2017-6-7 
*/ 
public interface AccountMapper {
    /**
     * 获取用户
     *
     * @param 用户id
     * @return
     */
	Account get(int id);
    
	/**
     * 通过account获取用户
     * @param 用户account
     * @return Account
     */
    Account getByAccount(String account);
    
	/**
     * 通过ID获取用户
     * @param id
     * @return Account
     */
    Account getById(int id);
	/**
     * 获取所有用户
     * @param 
     * @return List
     */
    List<Account> selectAll();
    
	/**
     * 分页获取用户
     * @param limit每页条数, offset偏移量
     * @return List
     */
    List<Account> selectPage(@Param("limit")int limit,@Param("offset")int offset);
	/**
     * 保存
     * @param Account 用户
     * @return int
     */
    int save(Account account);
	/**
     * 删除
     * @param accountId 用户id
     * @return int
     */
    int delete(int accountId);
    /**
     * 账号管理页面获取账号id
     * @return
     */
    Pager<Account> selectAccountIdManage(String querySql, int pageNow, int pageSize);
    /**
     * 用户管理
     * @param depIdSet  要查询的部门id集合
     * @param excludeAccountIdSet   排除的不查询的账号id集合
     * @param pageNow
     * @param pageSize
     * @return
     */
    Pager<Account> selectAccountManage(Set<Integer> depIdSet, Set<Integer> excludeAccountIdSet, int pageNow, int pageSize);
    
    /**
     * 更新 Account 用户实体
     * @return
     */
	int update(Account account);
	
	/**
     * 获取用户列表
     * @param 
     * @return List
     */
	List<Account> safeSelectAll();
}
