package com.hdnav.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.SimpleAuthorizationInfo;

import com.hdnav.entity.Account;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.entity.vo.UserCreateVO;
import com.hdnav.entity.vo.UserEditVO;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             用户Service
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣               
 * Create Date：                           2017-6-7 
*/ 
public interface AccountService {

    Account getAccountByAccount(String account);

    SimpleAuthorizationInfo getAccountRolePermission(int accountId);
    
    Pager<Map<String,Object>> getPageUsers(PagerReqVO pagerReqVO,TreeNode tree);

    /*ResultVO selectAccount(int userId, int pageNow, int pageSize);*/

    ResultVO  saveUser(UserCreateVO createVO);
    
    ResultVO  editUser(UserEditVO editVO);

    ResultVO deleteUser(ArrayList<Integer> userIds);

    ResultVO updateUserDep(int userId,int depId);

	ResultVO grantRoles(int userId, ArrayList<Integer> roleArray);
	
	List<Account> getAllAccounts();
}
