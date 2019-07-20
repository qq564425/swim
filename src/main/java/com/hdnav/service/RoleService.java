package com.hdnav.service;

import java.util.ArrayList;
import java.util.Map;

import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.RoleCreateVO;
import com.hdnav.entity.vo.RoleEditVO;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management
 * Comments:             角色service接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-8 
*/ 
public interface RoleService {
	
	/**
     * 获取显示角色
     * @param PagerReqVO, userId
     * @return Pager
     */
    Pager<Map<String,Object>> getShowRoles(PagerReqVO pagerReqVO,int userId);

    /**
     * 创建角色
     * @param RoleCreateVO, userId
     * @return ResultVO
     */
    ResultVO createRole(RoleCreateVO createVO, int userId);

    /**
     * 删除角色
     * @param RoleCreateVO, userId
     * @return ResultVO
     */
    ResultVO deleteRole(ArrayList<Integer> roleIds);

    /**
     * 编辑角色
     * @param RoleCreateVO, userId
     * @return ResultVO
     */
    ResultVO editRole(RoleEditVO editVO);

    /**
     * 授权
     * @param RoleCreateVO, userId
     * @return ResultVO
     */
    ResultVO grantPermissions(int roleId, ArrayList<Integer> perIdArray);
    
    /**
     * 获取权限树
     * @param userId
     * @return ResultVO
     */
    ResultVO getRoleTree(int userId);
}
