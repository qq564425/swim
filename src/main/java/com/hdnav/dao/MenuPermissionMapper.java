package com.hdnav.dao;

import java.util.List;

import com.hdnav.entity.MenuPermission;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             菜单权限dao接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋             
 * Create Date：                           2017-6-7
*/
public interface MenuPermissionMapper {
	/**
     * 获取菜单权限Set
     * @param menuId
     * @return List
     */
	public List<Integer> selectPermissionIdSet(int menuId);

	/**
     * 根据权限ID删除条目
     * @param perId
     * @return
     */
	public void deleteByPerId(int perId);

	/**
     * 根据菜单ID删除条目
     * @param menuId
     * @return
     */
	public void deleteByMenuId(int menuId);

	/**
     * 添加条目
     * @param menuPermission
     * @return int
     */
	public int addMenuPermission(MenuPermission menuPermission);
}
