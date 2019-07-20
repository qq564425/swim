package com.hdnav.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hdnav.entity.Menu;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             菜单dao接口        
 * JDK version used:     JDK1.8                             
 * Author：                                          高仲秋             
 * Create Date：                             2017-6-7
*/
public interface MenuMapper {
	
	/**
     * 分页获取菜单
     * @param limit, offset
     * @return List
     */
	public List<Menu> selectPage(@Param("limit")int limit, @Param("offset")int offset);

	/**
     * 获取菜单
     * @param
     * @return List
     */
	public List<Menu> selectAll();

	/**
     * 创建菜单
     * @param menu
     * @return int
     */
	public int createMenu(Menu menu);

	/**
     * 删除菜单
     * @param id
     * @return int
     */
	public int deleteMenu(int id);

	/**
     * 修改菜单
     * @param menu
     * @return int
     */
	public int updateMenu(Menu menu);
	public List<Menu> getMenusAndPermission();
}
