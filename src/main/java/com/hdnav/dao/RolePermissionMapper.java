package com.hdnav.dao;

import java.util.List;

import com.hdnav.entity.RolePermission;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             菜单dao接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋             
 * Create Date：                           2017-6-7
*/
public interface RolePermissionMapper {
	
	/**
     * 根据角色id获取权限列表
     * @param roleId
     * @return List
     */
	List<Integer> getPermissionIdSetByRoleId(int roleId);

	/**
     * 根据权限id删除条目
     * @param perId
     * @return
     */
    void deleteByPerId(int perId);

    /**
     * 根据角色id删除条目
     * @param roleId
     * @return
     */
    void deleteByRoleId(int roleId);

    /**
     * 添加条目
     * @param rolePermission
     * @return int
     */
    int addRolePermission(RolePermission rolePermission);
}
