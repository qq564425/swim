package com.hdnav.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hdnav.entity.vo.DepartmentCreateVO;
import com.hdnav.entity.vo.DepartmentEditVO;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.service.DepartmentService;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             部门controller        
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣                
 * Create Date：                           2017-6-7 
*/ 
@RestController
@RequestMapping("/main/department")
public class DepartmentController {
	
	@Autowired
    private DepartmentService departmentService;

    //跳转到部门管理
    @RequestMapping("/manage")
    public String departmentManage() {
        return "main/department/manage";
    }

    /**
     * 获取显示的部门
     * @param PagerReqVO 分页对象 TreeNode树对象
	 * @return Pager
     */
    @GetMapping(value="/get-list-departments")
    public String getListDeparts(PagerReqVO pagerReqVO,TreeNode tree) {
    	Pager<Map<String,Object>> vo = departmentService.getPageDepartments(pagerReqVO,tree);
        return new Gson().toJson(vo);
    }

    /**
     * 获取所有部门树
     * @param
	 * @return ResultVO
     */
    @GetMapping(value="/get-all-departments")
    public String getAllDeparts() {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        ResultVO vo = departmentService.getDeptTree(Integer.parseInt(principal.toString()));
        return new Gson().toJson(vo);
    }


    /**
     * 创建部门
     * @param createVO 新建对象实体
	 * @return ResultVO
     */
    @PostMapping(value = "/create")
    public ResultVO createDepartment(@Valid @ModelAttribute DepartmentCreateVO createVO, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO resultVO = new ResultVO(true);

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }

        resultVO = departmentService.createDepartment(createVO);
        return resultVO;
    }

    /**
     * 编辑部门
     * @param DepartmentEditVO 修改后的对象实体
	 * @return ResultVO
     */
    @PostMapping(value = "/edit")
    public ResultVO editDepartment(@Valid @ModelAttribute DepartmentEditVO createVO, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO resultVO = new ResultVO(true);

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }
        Object principal = SecurityUtils.getSubject().getPrincipal();
        resultVO = departmentService.editDepartment(createVO,Integer.parseInt(principal.toString()));
        //resultVO = departmentService.editDepartment(createVO,1);
        return resultVO;
    }

    /**
     * 删除部门
     * @param depIds 要删除的部门Id集合
	 * @return ResultVO
     */
    @PostMapping(value = "/del")
    public ResultVO delDepartment(@RequestBody Map<Object, Object> params) {
    	ArrayList<Integer> depIds = (ArrayList<Integer>) params.get("depIdStr");
        Object principal = SecurityUtils.getSubject().getPrincipal();
        ResultVO resultVO = departmentService.deleteDep(depIds, Integer.parseInt(principal.toString()));
        //ResultVO resultVO = departmentService.deleteDep(depIds, 1);
        return resultVO;
    }
}
