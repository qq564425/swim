package com.hdnav.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hdnav.entity.vo.PermissionCreateVO;
import com.hdnav.entity.vo.PermissionEditVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.service.PermissionService;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             权限controller        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-7 
*/ 
@RequestMapping("/main/permission")
@RestController
public class PermissionController {
    @Resource
    private PermissionService permissionService;

	/**
     * 创建权限
     * @param PermissionCreateVO, BindingResult
	 * @return ResultVO
     */
    @PostMapping(value = "/create")
    public ResultVO createPermission(@Valid @ModelAttribute PermissionCreateVO createVO, BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO resultVO = new ResultVO(true);

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }

        resultVO = permissionService.cratePermission(createVO);
        return resultVO;
    }


	/**
     * 删除权限
     * @param permIds
	 * @return ResultVO
     */
    @PostMapping(value = "/del")
    public ResultVO delPermission(@RequestBody Map<Object, Object> params) {
    	@SuppressWarnings("unchecked")
		ArrayList<Integer> permIds = (ArrayList<Integer>) params.get("permIdStr");
        ResultVO resultVO = permissionService.delPermission(permIds);
        return resultVO;
    }

    /**
     * 编辑权限
     * @param permIds
	 * @return ResultVO
     */
    @PostMapping(value = "/edit")
    public ResultVO editPermission(@Valid @ModelAttribute PermissionEditVO editVO, BindingResult bindingResult) {
        ResultVO resultVO = new ResultVO(true);
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }
        resultVO = permissionService.editPermission(editVO);
        return resultVO;
    }
}

