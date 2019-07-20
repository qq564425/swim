package com.hdnav.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hdnav.entity.Department;
import com.hdnav.entity.vo.DepartmentCreateVO;
import com.hdnav.entity.vo.DepartmentEditVO;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             部门service接口        
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣               
 * Create Date：                           2017-6-7 
*/ 

public interface DepartmentService {
	
	Pager<Map<String,Object>> getPageDepartments(PagerReqVO pagerReqVO,TreeNode tree);
    
    /*ResultVO getShowDepartments(int userId, Integer rootId,Integer checkedUserId);*/

    ResultVO createDepartment(DepartmentCreateVO createVO);

    ResultVO editDepartment(DepartmentEditVO createVO, int accountId);

    ResultVO deleteDep(ArrayList<Integer> depIds, int accountId);

    ResultVO getDeptTree(int userId);
    
    Department get(List<Department> list, int id);
    
    Department selectAccountDep(Integer accountId);
}
