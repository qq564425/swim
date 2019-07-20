package com.hdnav.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hdnav.entity.Permission;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             权限dao接口        
 * JDK version used:     JDK1.8                             
 * Author：                                        高仲秋             
 * Create Date：                          2017-6-7
*/
public interface PermissionMapper {

	/**
     * 获取权限
     * @param
     * @return List
     */
    public List<Permission> selectAll();

    /**
     * 创建权限
     * @param permission
     * @return int
     */
    public int createPermission(Permission permission);

    /**
     * 删除权限
     * @param perId
     * @return int
     */
    public int deletePermission(int perId);

    /**
     * 修改权限
     * @param permission
     * @return int
     */
    public int updatePermission(Permission permission);

    /**
     * 分页获取权限
     * @param limit, pageNo
     * @return List
     */
    public List<Permission> selectPage(@Param("limit")int limit, @Param("offset")int pageNo);
    
    /**
     * 获取登录用户权限有无情况
     * @param accountId
     * @return
     */
    public List<Permission> isExitPermission(@Param("accountId") Integer accountId);
    
    /*
     * 获取所有vts权限
     * @param
     * @return List
     */
    public List<Permission> getVtsPermissions();
}
