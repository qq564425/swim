package com.hdnav.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdnav.dao.AccountRoleMapper;
import com.hdnav.dao.PermissionMapper;
import com.hdnav.dao.RoleMapper;
import com.hdnav.dao.RolePermissionMapper;
import com.hdnav.entity.AccountRole;
import com.hdnav.entity.Role;
import com.hdnav.entity.RolePermission;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.RoleCreateVO;
import com.hdnav.entity.vo.RoleEditVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.service.RoleService;
import com.hdnav.utils.Constants;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             查询角色service接口实现类    
 * JDK version used:     JDK1.8                             
 * Author：                                        高仲秋               
 * Create Date：                           2017-6-5
*/
@Service
public class RoleServiceImpl implements RoleService{
	

	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private AccountRoleMapper accountRoleMapper;
	
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
	@Autowired
	private PermissionMapper permissionMapper;
	
    /**
     * 获取显示的角色
     * @param PagerReqVO, userId
     * @return Pager
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Pager<Map<String, Object>> getShowRoles(PagerReqVO pagerReqVO, int userId) {
    	List<Map<String,Object>> resultRoles = new ArrayList<Map<String,Object>>();
    	int size = 0;
    	//我的角色
        List<Map<String,Object>> myRoles = new ArrayList<Map<String,Object>>();
    	//获取所有角色
        List<Role> roles = roleMapper.selectAll();
        for(Role role:roles){
        	Map<String,Object> m = new HashMap<String,Object>();
        	m.put("id", role.getId());
        	m.put("key", role.getKey());
        	m.put("name", role.getName());
            myRoles.add(m);
        }
        size = myRoles.size();
        if(pagerReqVO==null||pagerReqVO.getLimit()==0){
        	resultRoles = myRoles;
        }else{
        	for(int i = (pagerReqVO.getPageNo()-1)*pagerReqVO.getLimit();i<Math.min(pagerReqVO.getLimit()*pagerReqVO.getPageNo(),myRoles.size());i++){
        		resultRoles.add(myRoles.get(i));
            }
        }
		return new Pager(resultRoles,size);
    }

    /**
     * 创建角色
     * @param RoleCreateVO, userId
     * @return ResultVO
     */
	@Override
	public ResultVO createRole(RoleCreateVO createVO, int userId) {
		 ResultVO resultVO = new ResultVO(true);
	        List<Role> roleList = roleMapper.selectAll();
	        if (isKeyExist(roleList,createVO.getKey())) {
	            resultVO.setOk(false);
	            resultVO.setMsg("角色键值已存在");
	            return resultVO;
	        }
	        Role role = new Role();
	        role.setName(createVO.getName());
	        role.setKey(createVO.getKey());
	        roleMapper.createRole(role);
	        String key = role.getKey();
	        int roleId = roleMapper.queryRoleInfo(key).getId();

	        //添加账号角色关联
	        AccountRole accountRole = new AccountRole();
	        accountRole.setAccountId(userId);
	        accountRole.setRoleId(roleId);
	        accountRoleMapper.createAccountRole(accountRole);
	        resultVO.setMsg("创建角色成功");
	        resultVO.setFlag(1);
	        return resultVO;
	}

    /**
     * 删除角色
     * @param roleIds
     * @return ResultVO
     */
	@Override
	public ResultVO deleteRole(ArrayList<Integer> roleIds) {
		  ResultVO resultVO = new ResultVO(true);
	        //获取所有角色
	        List<Role> roles = roleMapper.selectAll();
	        for(int roleId:roleIds){
	        	Role role = this.get(roles, roleId);
		        if (role == null) {
		            resultVO.setOk(false);
		            resultVO.setMsg("角色不存在");
		            return resultVO;
		        }
	        
		        roleMapper.deleteRole(roleId);
		        rolePermissionMapper.deleteByRoleId(roleId);
		        accountRoleMapper.deleteByRoleId(roleId);
		        resultVO.setMsg("删除角色成功");
		        resultVO.setFlag(1);
	        }
	        return resultVO;
	}

    /**
     * 编辑角色
     * @param RoleEditVO
     * @return ResultVO
     */
	@Override
	public ResultVO editRole(RoleEditVO editVO) {
        ResultVO resultVO = new ResultVO(true);
        //获取所有角色
        List<Role> roles = roleMapper.selectAll();
        Role role = this.get(roles, editVO.getId());
        if (role == null) {
            resultVO.setOk(false);
            resultVO.setMsg("角色不存在");
            return resultVO;
        }
        if (!role.getKey().equals(editVO.getKey()) && isKeyExist(roles, editVO.getKey())) {
            resultVO.setOk(false);
            resultVO.setMsg("角色键值已存在");
            return resultVO;
        }
        Role update = new Role();
        update.setId(editVO.getId());
        update.setKey(editVO.getKey());
        update.setName(editVO.getName());
        roleMapper.updateRole(update);
        resultVO.setMsg("编辑角色成功");
        resultVO.setFlag(1);
        return resultVO;
	}

    /**
     * 角色授权
     * @param roleId, perIdArray
     * @return ResultVO
     */
	@Override
	public ResultVO grantPermissions(int roleId, ArrayList<Integer> perIdArray) {

        ResultVO resultVO = new ResultVO(true);
        //获取所有角色
        List<Role> roles = roleMapper.selectAll();
        //获取角色
        Role role = this.get(roles, roleId);
        if (role == null) {
            resultVO.setOk(false);
            resultVO.setMsg("角色不存在");
            return resultVO;
        }

        Set<Integer> perIdSet = new HashSet<Integer>();
        if(perIdArray != null && perIdArray.size()>0){
        	for (Integer id : perIdArray) {
                perIdSet.add(id);
            }
        }

        //删除角色权限
        rolePermissionMapper.deleteByRoleId(roleId);
        //授权
        if (!perIdSet.isEmpty()) {
            for (Integer perId : perIdSet) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setPermissionId(perId);
                rolePermission.setRoleId(roleId);
                rolePermissionMapper.addRolePermission(rolePermission);

            }
        }
        resultVO.setMsg("授权成功");
        resultVO.setFlag(1);
        return resultVO;
	}

    /**
     * 获取角色树
     * @param userId
     * @return ResultVO
     */
	@Override
	public ResultVO getRoleTree(int userId) {
        List<TreeNode> trees = new ArrayList<TreeNode>();
        Pager<Map<String,Object>> pager = this.getShowRoles(null,userId);
        List<Map<String,Object>> list = pager.getRows();
        TreeNode superTree = new TreeNode();
        superTree.setId(Constants.SUPER_TREE_NODE);
		superTree.setName("角色树");		
        superTree.setOpen(true);
        superTree.setNocheck(true);
        trees.add(superTree);
        for(Map<String,Object> role:list){
            TreeNode tree = new TreeNode();
            tree.setId(role.get("id").toString());
            tree.setName(role.get("name").toString());
            tree.setpId(Constants.SUPER_TREE_NODE);
            trees.add(tree);
        }
        ResultVO resultVO = new ResultVO(true);
        resultVO.setData(trees);
		return resultVO;
	}
	
    /**
     * 根据角色id获取角色
     * @param id
     * @return Role
     */
    public Role get(List<Role> roles, int id) {
        for(Role role:roles){
            if(role.getId().intValue()==id){
                return role;
            }
        }
        return null;
    }
    
    /**
     * 判断键值是否存在
     * @param List, key
     * @return boolean
     */
    private static boolean isKeyExist(List<Role> roles,String key){
        for(Role role:roles){
            if(role.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }
	
}
