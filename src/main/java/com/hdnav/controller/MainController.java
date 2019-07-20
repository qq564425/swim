package com.hdnav.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.service.MenuService;
import com.hdnav.service.PermissionService;

/**
 * CopyRright:           海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             获取权限controller        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-7 
*/ 
@RestController
@RequestMapping("/main")
public class MainController {
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionService permissionService;

	/**
     * 获取显示权限
     * @param PagerReqVO, TreeNode
	 * @return Pager
     */
    @GetMapping("/get-show-permissions")
    public String getShowPermissons(PagerReqVO pagerReqVO,TreeNode tree) {
    	Pager<Map<String,Object>> vo = permissionService.getShowPermissions(pagerReqVO,tree);
        return new Gson().toJson(vo);
    }
    
    /**
     * 获取所有部门树
     * @param
	 * @return ResultVO
     */
    @GetMapping(value="/get-all-permissions")
    public String getAllDeparts() {
        ResultVO vo = permissionService.getPermissionTree();
        return new Gson().toJson(vo);
    }

    /**
     * 获取菜单显示权限
     * @param menuId
	 * @return ResultVO
     */
    @GetMapping("/get-menu-show-permissions")
    public String getMenuShowPermissons(@RequestParam int menuId) {
         ResultVO resultVO = permissionService.getMenuShowPermissions(menuId,true);
         return new Gson().toJson(resultVO);
     }
    
    /**
     * 获取菜单查看权限
     * @param menuId
	 * @return ResultVO
     */
    @GetMapping("/get-menu-check-permissions")
    public String getMenucheckPermissons(@RequestParam int menuId) {
         ResultVO resultVO = permissionService.getMenuShowPermissions(menuId,false);
         return new Gson().toJson(resultVO);
     }

    /**
     * 获取角色显示权限
     * @param roleId
	 * @return ResultVO
     */
    @GetMapping("/get-role-show-permissions")
    public String getRoleShowPermissons(@RequestParam int roleId) {
        ResultVO resultVO = permissionService.getRoleShowPermissions(roleId,true);
        return new Gson().toJson(resultVO);
    }
    
    /**
     * 获取角色显示权限
     * @param roleId
	 * @return ResultVO
     */
    @GetMapping("/get-role-check-permissions")
    public String getRoleCheckPermissons(@RequestParam int roleId) {
    	ResultVO resultVO = permissionService.getRoleShowPermissions(roleId,false);
    	return new Gson().toJson(resultVO);
    }

}

