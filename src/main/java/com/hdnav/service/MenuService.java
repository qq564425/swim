package com.hdnav.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hdnav.entity.Menu;
import com.hdnav.entity.vo.MenuCreateVO;
import com.hdnav.entity.vo.MenuEditVO;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management
 * Comments:             菜单service接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-7 
*/ 
public interface MenuService {
	
    /**
     * 获取显示菜单
     * @param rootId
     * @return List
     */
    List<Map<String,Object>> selectShowMenus(Integer rootId);

    /**
     * 创建菜单
     * @param createVO
     * @return ResultVO
     */
    ResultVO createMenu(MenuCreateVO createVO);

	/**
     * 删除菜单
     * @param menuIds
     * @return ResultVO
     */
    ResultVO deleteMenu(ArrayList<Integer> menuIds);

    /**
     * 编辑菜单
     * @param MenuEditVO
     * @return ResultVO
     */
    ResultVO editMenu(MenuEditVO editVO);

	/**
     * 菜单授权
     * @param menuId, perIdArray
     * @return ResultVO
     */
    ResultVO grantPermissions(int menuId, Integer[] perIdArray);

	/**
     * 获取菜单列表
     * @param PagerReqVO, TreeNode
     * @return Pager
     */
    Pager<Map<String, Object>> getPageMenus(PagerReqVO pagerReqVO, TreeNode tree);

	/**
     * 获取菜单树
     * @param
     * @return ResultVO
     */
	ResultVO getMenuTree();

	/**
     * 获取船舶显示菜单
     * @param rootId
     * @return List
     */
	List<Map<String, Object>> selectShipShowMenus(Integer rootId);
	List<Menu> getMenusAndPermission();
}
