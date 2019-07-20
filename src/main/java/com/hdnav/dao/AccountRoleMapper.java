package com.hdnav.dao;

import java.util.List;

import com.hdnav.entity.AccountRole;
/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             用户-角色Dao接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣               
 * Create Date：                           2017-6-7 
*/ 

public interface AccountRoleMapper {
    /**
     * 根据账号id获取角色id
     * @param accountId 用户Id
     * @return
     */
	List<Integer> selectRoleIdSet(int accountId);
    /**
     * 创建账号角色
     * @param AccountRole 用户角色实体
     * @return
     */
    int createAccountRole(AccountRole accountRole);
    /**
     * 根据角色删除
     * @param roleId 角色Id
     * @return
     */
    int deleteByRoleId(int roleId);
    /**
     *   根据账号删除
     * @param accountId 用户id
     * @return
     */
    int deleteByAccountId(int accountId);

}
