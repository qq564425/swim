package com.hdnav.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hdnav.entity.Role;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             角色dao接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋             
 * Create Date：                           2017-6-7
*/
public interface RoleMapper {
	
	Role get(List<Role> roles, int id);

	/**
     * 获取角色
     * @param
     * @return List
     */
    List<Role> selectAll();

    /**
     * 创建角色
     * @param role
     * @return int
     */
    int createRole(Role role);

    /**
     * 删除角色
     * @param roleId
     * @return int
     */
    int deleteRole(int roleId);

    /**
     * 更新角色
     * @param role
     * @return int
     */
    int updateRole(Role role);

    /**
     * 查询单个角色
     * @param key
     * @return Role
     */
    public Role queryRoleInfo(@Param("key")String key);
}
