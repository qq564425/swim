package com.hdnav.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.RoleCreateVO;
import com.hdnav.entity.vo.RoleEditVO;
import com.hdnav.service.RoleService;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             角色controller        
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣               
 * Create Date：                           2017-6-7 
*/ 

@RestController
@RequestMapping("/main/role")
public class RoleController {
	

    @Resource
    private RoleService roleService;

    //跳转到角色管理页面
    @RequestMapping("/manage")
    public String manage() {
        return "main/role/manage";
    }

    /**
     * 获取所有角色
     * @param PagerReqVO 分页对象
	 * @return Pager
     */
    @RequestMapping(value = "/show-roles")
    public String getShowRoles(PagerReqVO pagerReqVO) {
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        Pager<Map<String,Object>> vo = roleService.getShowRoles(pagerReqVO,Integer.parseInt(principal.toString()));
        return new Gson().toJson(vo);
    }
    
     /**
     * 获取所有角色树
     * @param
	 * @return ResultVO
     */
    @GetMapping(value="/get-all-roles")
    public String getAllDeparts() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        ResultVO vo = roleService.getRoleTree(Integer.parseInt(principal.toString()));
        return new Gson().toJson(vo);
    }
    
    /**
    * 创建角色
    * @param RoleCreateVO 创建的实体
	 * @return ResultVO
    */

    @PostMapping(value = "/create")
    public ResultVO createRole(@Valid @ModelAttribute RoleCreateVO createVO, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO resultVO = new ResultVO(true);

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }
        Subject subject = SecurityUtils.getSubject();
        Object principal = subject.getPrincipal();
        resultVO = roleService.createRole(createVO, Integer.parseInt(principal.toString()));
        //resultVO = roleService.createRole(createVO, 1);
        return resultVO;
    }

    /**
    * 编辑角色
    * @param RoleEditVO 编辑后的对象实体
	 * @return ResultVO
    */
    @PostMapping(value = "/edit")
    public ResultVO editRole(@Valid @ModelAttribute RoleEditVO editVO, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO resultVO = new ResultVO(true);

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }
        resultVO = roleService.editRole(editVO);
        return resultVO;
    }

    /**
    * 删除角色
    * @param roleIds 要删除的角色Id集合
	 * @return ResultVO
    */
    @PostMapping(value = "/del")
    public ResultVO delRole(@RequestBody Map<Object, Object> params) {
    	@SuppressWarnings("unchecked")
		ArrayList<Integer> roleIds = (ArrayList<Integer>) params.get("roleIdStr");
        ResultVO resultVO = roleService.deleteRole(roleIds);
        return resultVO;
    }

    /**
    * 角色授权
    * @param id 角色Id, peridArray 当前角色下的权限Id
	 * @return ResultVO
    */
    @PostMapping(value = "/grant")
    //@RequestParam int id,@RequestParam(value = "peridArray[]",required = false) Integer []peridArray
    public ResultVO grantRolePermission(@RequestBody Map<Object, Object> params) {
    	int id = (int) params.get("id");
    	@SuppressWarnings("unchecked")
		ArrayList<Integer> peridArray = (ArrayList<Integer>) params.get("peridStr");
    	System.out.println(peridArray.get(0)+5);
        ResultVO resultVO = roleService.grantPermissions(id,peridArray);
        return resultVO;
    }
}
