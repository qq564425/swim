package com.hdnav.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hdnav.entity.Permission;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.PermissionCreateVO;
import com.hdnav.entity.vo.PermissionEditVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management
 * Comments:             权限service接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-7 
*/ 
public interface PermissionService {
	
    /**
     * 获取显示权限
     * @param PagerReqVO, TreeNode
     * @return Pager
     */
	Pager<Map<String,Object>> getShowPermissions(PagerReqVO pagerReqVO,TreeNode tree);

	/**
     * 获取菜单显示权限
     * @param menuId, chkDisabled
     * @return ResultVO
     */
    ResultVO getMenuShowPermissions(int menuId,boolean chkDisabled);

    /**
     * 获取角色显示权限
     * @param roleId, chkDisabled
     * @return ResultVO
     */
    ResultVO getRoleShowPermissions(int roleId,boolean chkDisabled);

    /**
     * 创建权限
     * @param PermissionCreateVO
     * @return ResultVO
     */
    ResultVO cratePermission(PermissionCreateVO permissionCreateVO);

    /**
     * 更新权限
     * @param PermissionEditVO
     * @return ResultVO
     */
    ResultVO editPermission(PermissionEditVO permissionEditVO);

    /**
     * 删除权限
     * @param permIds
     * @return ResultVO
     */
    ResultVO delPermission(ArrayList<Integer> permIds);

    /**
     * 获取权限树
     * @param permIds
     * @return ResultVO
     */
	ResultVO getPermissionTree();
	
	/**
     * 获取登录用户权限有无情况
     * @param accountId
     * @return
     */
    List<Permission> isExitPermission(Integer accountId);
    
    /*
     * 获取所有vts权限
     * @param
     * @return List
     */
    public List<Permission> getVtsPermissions();
}
