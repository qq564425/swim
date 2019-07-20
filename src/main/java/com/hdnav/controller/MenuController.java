package com.hdnav.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hdnav.entity.Menu;
import com.hdnav.entity.vo.MenuCreateVO;
import com.hdnav.entity.vo.MenuEditVO;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.service.MenuService;


/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             菜单controller        
 * JDK version used:     JDK1.8                             
 * Author：                                         高仲秋                
 * Create Date：                           2017-6-7 
*/ 
@RequestMapping("/main/menu")
@RestController
public class MenuController {
	
    @Autowired
    private MenuService menuService;
    
	/**
     * 获取菜单列表
     * @param PagerReqVO, TreeNode
	 * @return Pager
     */
    @GetMapping("/get-list-menus")
    public String getListMenus(PagerReqVO pagerReqVO,TreeNode tree) {
    	Pager<Map<String,Object>> vo = menuService.getPageMenus(pagerReqVO,tree);
        return new Gson().toJson(vo);
    }
    
    /**
     * 获取所有菜单树
     * @param
	 * @return ResultVO
     */
    @GetMapping(value="/get-all-menus")
    public String getAllMenus() {
        ResultVO vo = menuService.getMenuTree();
        return new Gson().toJson(vo);
    }
    
    /**
     * 获取显示菜单
     * @param
	 * @return ResultVO
     */
    @PostMapping("/get-show-menus")
    public String getShowMenus() {
        ResultVO resultVO = new ResultVO(true);
        List<Map<String, Object>> maps = menuService.selectShowMenus(null);
        resultVO.setData(maps);
        return new Gson().toJson(resultVO);
    }
    
    /**
     * 船舶home页获取显示菜单
     * @param
	 * @return Map
     */
	@RequestMapping(value = { "/get-ship-show-menus" }, method = {RequestMethod.POST, RequestMethod.GET })
	public String getShipShowMenus(){
		Map<String, Object> shipShowMenus = new HashMap<String, Object>();
		List<Map<String, Object>> maps = menuService.selectShipShowMenus(null);
		shipShowMenus.put("result", "1");
		shipShowMenus.put("info", "请求返回成功。");
		shipShowMenus.put("data", maps);
		return new Gson().toJson(shipShowMenus);
	}

	/**
     * 创建菜单
     * @param MenuCreateVO, BindingResult
	 * @return ResultVO
     */
    @PostMapping(value = "/create")
    public ResultVO createMenu(@Valid @ModelAttribute MenuCreateVO createVO, BindingResult bindingResult) {
        ResultVO resultVO = new ResultVO(true);
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }

        resultVO = menuService.createMenu(createVO);

        return resultVO;
    }

    /**
     * 编辑菜单
     * @param MenuEditVO, BindingResult
	 * @return ResultVO
     */
    @PostMapping(value = "/edit")
    public ResultVO editMenu(@Valid @ModelAttribute MenuEditVO editVO, BindingResult bindingResult) {
        ResultVO resultVO = new ResultVO(true);
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }

        resultVO = menuService.editMenu(editVO);

        return resultVO;
    }

    /**
     * 删除菜单
     * @param menuIds
	 * @return ResultVO
     */
    @PostMapping(value = "/del")
    public ResultVO delMenu(@RequestBody Map<Object, Object> params) {
    	@SuppressWarnings("unchecked")
		ArrayList<Integer> menuIds = (ArrayList<Integer>) params.get("menuIds");
        ResultVO resultVO = menuService.deleteMenu(menuIds);
        return resultVO;
    }

    /**
     * 菜单授权
     * @param id, peridArray
	 * @return ResultVO
     */
    @PostMapping(value = "/grant")
    public ResultVO grantMenuPermission(@RequestParam int id,@RequestParam(value = "peridArray[]",required = false) Integer []peridArray) {
        ResultVO resultVO = menuService.grantPermissions(id,peridArray);
        return resultVO;
    }
    
    @PostMapping("/getMenusAndPermission")
    public String getMenusAndPermission(){
    	ResultVO resultVO = new ResultVO();
    	List<Menu> list = menuService.getMenusAndPermission();
    	resultVO.setData(list);
    	return new Gson().toJson(resultVO);
    }

}

