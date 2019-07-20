package com.hdnav.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdnav.dao.MenuMapper;
import com.hdnav.dao.MenuPermissionMapper;
import com.hdnav.dao.PermissionMapper;
import com.hdnav.dao.RoleMapper;
import com.hdnav.dao.RolePermissionMapper;
import com.hdnav.entity.Menu;
import com.hdnav.entity.Permission;
import com.hdnav.entity.Role;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.PermissionCreateVO;
import com.hdnav.entity.vo.PermissionEditVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.service.PermissionService;
import com.hdnav.utils.Constants;
import com.hdnav.utils.StrKit;

/**
 * CopyRight:           海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             查询权限service接口实现类    
 * JDK version used:     JDK1.8                             
 * Author：                                        高仲秋               
 * Create Date：                           2017-6-5
*/
@Service
public class PermissionServiceImpl implements PermissionService{

	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	@Autowired
	private RoleMapper roleMapper;
	
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
	@Autowired
	private MenuServiceImpl menuServiceImpl;
	
	@Autowired
	private RoleServiceImpl roleServiceImpl;
	
	@Autowired
	private MenuPermissionMapper menuPermissionMapper;
	
	/**
     * 获取显示权限
     * @param PagerReqVO, TreeNode
     * @return Pager
     */
	@Override
	public Pager<Map<String, Object>> getShowPermissions(PagerReqVO pagerReqVO, TreeNode tree) {
		 List<Map<String,Object>> resultPerms = new ArrayList<Map<String,Object>>();
         List<Permission> perms = permissionMapper.selectAll();
         int size = 0;
         if(pagerReqVO==null||pagerReqVO.getLimit()==0){
         	for(Permission perm : perms){
         		resultPerms.add(getPermsMap(perms,perm));
         	}
         	size = perms.size();
         }else if(tree!=null && StrKit.notBlank(tree.getId())&&!Constants.SUPER_TREE_NODE.equals(tree.getId())){
             //获取所有部门
             int child = Integer.parseInt(tree.getId());
             //获取当前部门
             Permission selectdepart = this.get(perms,child);
             resultPerms.add(getPermsMap(perms,selectdepart));
             //获取当前部门的子部门
             List<Permission> childrens = getChildrenPermissions(perms,child);
             
             size = childrens.size();
             if(childrens!=null&&size>0){
                 if(childrens.size()<pagerReqVO.getLimit()){
                 	for(Permission perm : childrens)
                 		resultPerms.add(getPermsMap(perms,perm));
                 }else{
                     for(int i = (pagerReqVO.getPageNo()-1)*pagerReqVO.getLimit();i<Math.min(pagerReqVO.getLimit()*pagerReqVO.getPageNo(),size);i++){
                    	 resultPerms.add(getPermsMap(perms,childrens.get(i)));
                     }
                 }
             }
         }else{
         	size = perms.size();
         	List<Permission> _perms = permissionMapper.selectPage(pagerReqVO.getLimit(),pagerReqVO.getOffset());
         	for(Permission perm : _perms){
         		resultPerms.add(getPermsMap(perms,perm));
         	}

         }
 		return new Pager(resultPerms,size);
	}

    /**
     * 获取菜单显示权限
     *
     * @param menuId, chkDisabled
     * @return ResultVO
     */
	@Override
	public ResultVO getMenuShowPermissions(int menuId, boolean chkDisabled) {
		 ResultVO resultVO = new ResultVO(true);
	        
	        List<TreeNode> mapList = new ArrayList<TreeNode>();
	        
	        //获取所有菜单
	        List<Menu> menus = menuMapper.selectAll();
	        Menu menu = menuServiceImpl.get(menus, menuId);
	        if (menu == null) {
	            resultVO.setOk(false);
	            resultVO.setMsg("菜单不存在");
	            return resultVO;
	        }
	        
	        //获取所有权限
	        List<Permission> permissionList = permissionMapper.selectAll();
	        
	        List<Permission> filterPermissionList = filterChildNode(permissionList);
	        
	        //获取菜单拥有的权限id
	        List<Integer> permissionIdSet = menuPermissionMapper.selectPermissionIdSet(menuId);
	        
	        TreeNode superTree = new TreeNode();
	        superTree.setId(Constants.SUPER_TREE_NODE);
	        String perList = "权限列表";
	        superTree.setName(perList);
	        superTree.setOpen(true);
	        superTree.setNocheck(true);
	        mapList.add(superTree);
	        
	        for (Permission permission : filterPermissionList) {
	        	TreeNode treeNode = new TreeNode();
	        	treeNode.setId(permission.getId().toString());
	        	treeNode.setChkDisabled(chkDisabled);
	        	treeNode.setName(permission.getName());
	        	treeNode.setChecked(permissionIdSet.contains(permission.getId()));
	        	treeNode.setOpen(true);
	        	if(permission.getParentId()!=null)
	        		treeNode.setpId(permission.getParentId().toString());
	            else{
	            	treeNode.setpId(Constants.SUPER_TREE_NODE);
	            }
	            mapList.add(treeNode);
	        }
	        resultVO.setData(mapList);

	        return resultVO;
	}

    /**
     * 获取角色显示权限
     *
     * @param roleId, chkDisabled
     * @return ResultVO
     */
	@Override
	public ResultVO getRoleShowPermissions(int roleId, boolean chkDisabled) {
		ResultVO resultVO = new ResultVO(true);

        List<TreeNode> mapList = new ArrayList<TreeNode>();

        //获取所有角色
        List<Role> roles = roleMapper.selectAll();
        Role role = roleServiceImpl.get(roles, roleId);
        if (role == null) {
            resultVO.setOk(false);
            resultVO.setMsg("角色不存在");
            return resultVO;
        }
        //获取所有权限
        List<Permission> permissionList = permissionMapper.selectAll();
        
        //获取角色拥有的权限id
        List<Integer> permissionIdSet = rolePermissionMapper.getPermissionIdSetByRoleId(roleId);


        TreeNode superTree = new TreeNode();
        superTree.setId(Constants.SUPER_TREE_NODE);
        String perList = "权限列表";
        superTree.setName(perList);
        superTree.setOpen(true);
        superTree.setNocheck(true);
        mapList.add(superTree);
        
        for (Permission permission : permissionList) {
        	TreeNode treeNode = new TreeNode();
        	treeNode.setId(permission.getId().toString());
        	treeNode.setChkDisabled(chkDisabled);
        	treeNode.setName(permission.getName());
        	treeNode.setChecked(permissionIdSet.contains(permission.getId()));
        	treeNode.setOpen(true);
        	if(permission.getParentId()!=null)
        		treeNode.setpId(permission.getParentId().toString());
            else{
            	treeNode.setpId(Constants.SUPER_TREE_NODE);
            }
            mapList.add(treeNode);
        }
        resultVO.setData(mapList);
        return resultVO;
	}

    /**
     * 创建权限
     *
     * @param permissionCreateVO
     * @return ResultVO
     */
	@Override
	public ResultVO cratePermission(PermissionCreateVO permissionCreateVO) {
        ResultVO resultVO = new ResultVO(true);
        //所有权限
        List<Permission> permissionList = permissionMapper.selectAll();
        //查看父级权限是否存在
        Integer parentId = permissionCreateVO.getParentId();
        if (parentId != null) {
            Permission permission = this.get(permissionList, parentId.intValue());
            if (permission == null) {
                resultVO.setOk(false);
                resultVO.setMsg("父级权限不存在");
                return resultVO;
            }
        }
        //判断权限键值是否存在
        if (isKeyExist(permissionList, permissionCreateVO.getKey())) {
            resultVO.setOk(false);
            resultVO.setMsg("权限键值已存在");
            return resultVO;
        }
        
        Permission permission = new Permission();
        permission.setKey(permissionCreateVO.getKey());
        permission.setName(permissionCreateVO.getName());
        permission.setParentId(permissionCreateVO.getParentId());
        permission.setOrder(permissionCreateVO.getOrder());
        if(permissionCreateVO.getMenuId()!=null)
        permission.setMenuId(Integer.parseInt(permissionCreateVO.getMenuId()));
        permissionMapper.createPermission(permission);

        resultVO.setMsg("权限创建成功");
        resultVO.setFlag(1);
        return resultVO;
	}

	/**
     * 编辑权限
     *
     * @param PermissionEditVO
     * @return ResultVO
     */
	@Override
	public ResultVO editPermission(PermissionEditVO permissionEditVO) {
		 ResultVO resultVO = new ResultVO(true);
	        //获取所有权限
	        List<Permission> permissionList = permissionMapper.selectAll();

	        Permission permission = this.get(permissionList, permissionEditVO.getId());
	        if (permission == null) {
	            resultVO.setOk(false);
	            resultVO.setMsg("权限不存在");
	            return resultVO;
	        }

	        Permission permissionParent = this.get(permissionList, permissionEditVO.getParentId());
	        if (permissionParent == null) {
	            resultVO.setOk(false);
	            resultVO.setMsg("上级权限不存在");
	            return resultVO;
	        }

	        if (isKeyExist(permissionList, permissionEditVO.getKey())) {
	            if (!permission.getKey().equals(permissionEditVO.getKey())) {
	                resultVO.setOk(false);
	                resultVO.setMsg("权限键值已存在");
	                return resultVO;
	            }
	        }

	        List<Integer> childrenPermissionIds = getChildrenPermissionIds(permissionEditVO.getId(), permissionList);
	        childrenPermissionIds.add(permissionEditVO.getId());
	        if(childrenPermissionIds.contains(permissionEditVO.getParentId())){
	            resultVO.setOk(false);
	            resultVO.setMsg("所在权限的上级不能为自己所在权限或者下级权限");
	            return resultVO;
	        }

	        Permission update = new Permission();
	        update.setId(permissionEditVO.getId());
	        update.setParentId(permissionEditVO.getParentId());
	        update.setName(permissionEditVO.getName());
	        update.setKey(permissionEditVO.getKey());
	        update.setOrder(permissionEditVO.getOrder());
	        int num = permissionMapper.updatePermission(update);
	        if (num == 1) {
	            resultVO.setMsg("更新权限成功");
	            resultVO.setFlag(1);
	            return resultVO;
	        } else {
	            resultVO.setOk(false);
	            resultVO.setMsg("更新权限失败");
	            return resultVO;
	        }
	}

    /**
     * 删除权限
     *
     * @param permIds
     * @return ResultVO
     */
	@Override
	public ResultVO delPermission(ArrayList<Integer> permIds) {
		ResultVO resultVO = new ResultVO(true);
        //获取所有权限
        List<Permission> permissionList = permissionMapper.selectAll();
	    for(int perId : permIds){
	        //获取子级权限id集合
	        List<Integer> childrenPermissionIds = getChildrenPermissionIds(perId, permissionList);
	
	        //删除权限
	        int num = permissionMapper.deletePermission(perId);
	
	        for (Integer id : childrenPermissionIds) {
	            num = permissionMapper.deletePermission(id);
	            if (num == 1) {
	                rolePermissionMapper.deleteByPerId(id);	
	            }
	        }
	        rolePermissionMapper.deleteByPerId(perId);
	        menuPermissionMapper.deleteByPerId(perId);
        }
        resultVO.setMsg("删除权限成功");
        return resultVO;
	}

    /**
     * 获取权限树
     * @param
     * @return ResultVO
     */
	@Override
	public ResultVO getPermissionTree() {
		List<TreeNode> trees = new ArrayList<TreeNode>();
        Pager<Map<String,Object>> pager = this.getShowPermissions(null,null);
        List<Map<String,Object>> list = pager.getRows();
        TreeNode superTree = new TreeNode();
        superTree.setId(Constants.SUPER_TREE_NODE);
        String allPerm = "所有权限";
        superTree.setName(allPerm);
        superTree.setOpen(true);
        trees.add(superTree);
        for(Map<String,Object> perm:list){
            TreeNode tree = new TreeNode();
            tree.setId(perm.get("id").toString());
            tree.setName(perm.get("name").toString());
            tree.setOpen(true);
            if(perm.get("parentId")!=null)
            	tree.setpId(perm.get("parentId").toString());
            else{
            	tree.setpId(Constants.SUPER_TREE_NODE);
            }
            trees.add(tree);
        }
        ResultVO resultVO = new ResultVO(true);
        resultVO.setData(trees);
		return resultVO;
	}
	
    /**
     * 根据权限id获取权限
     * @param List, id
     * @return Permission
     */
    public Permission get(List<Permission> permissionList,int id) {
        for(Permission permission:permissionList){
            if(permission.getId().intValue()==id){
                return permission;
            }
        }
        return null;
    }
    
    /**
     * 过滤子节点
     * @param permissionList
     * @return List
     */
    public List<Permission> filterChildNode(List<Permission> permissionList){
    	List<Permission> filterPermissionList = new ArrayList<Permission>();
    	for(Permission perm : permissionList){
    		if(perm.getId() == 134){
    			System.out.print(111);
    		}
    		boolean isNotLeaf = notLeaf(permissionList,perm.getId());
    		if(isNotLeaf)
    			filterPermissionList.add(perm);
    	}
		return filterPermissionList;
    }
    
    /**
     * 判断是否为leaf节点
     * @param permissionList, perId
     * @return boolean
     */
    private boolean notLeaf(List<Permission> permissionList, Integer perId){
    	boolean notLeaf = false;
    	for(Permission perm : permissionList){
    		Integer parentId = perm.getParentId();
    		if(parentId != null && parentId.intValue() == perId.intValue()){
    			notLeaf = true;
    			break;
    		}
    			
    	}
    	return notLeaf;
    }
    
    /**
     * 获取权限map
     * @param List, Permission
     * @return Map
     */
    private Map<String,Object> getPermsMap(List<Permission> perms,Permission perm){
    	Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", perm.getId());
		m.put("name", perm.getName());
		m.put("order", perm.getOrder());
		m.put("key", perm.getKey());
		m.put("parentId", perm.getParentId());
		if(perm.getParentId()!=null&&!Constants.SUPER_TREE_NODE.equals(perm.getParentId().toString()))
			m.put("parentName", this.get(perms,perm.getParentId()).getName());
		return m;
    }
    
    /**
     * 获取子权限
     * @param List, permId
     * @return List
     */
    public static List<Permission> getChildrenPermissions(List<Permission> list, Integer permId) {
        List<Permission> permIdList = new ArrayList<Permission>();
        for (Permission perm : list) {
            Integer permissionId = perm.getParentId();
            if ((permissionId == null && permId == null) || (permissionId != null && permId != null && permissionId.intValue() == permId.intValue())) {
            	permIdList.add(perm);
            	permIdList.addAll(getChildrenPermissions(list, perm.getId()));
            }
        }
        return permIdList;
    }
    
    /**
     * 判断键值是否存在
     * @param List, key
     * @return boolean
     */
    private static boolean isKeyExist(List<Permission> list, String key) {
        for (Permission permission : list) {
            if (permission.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 获取子级权限id集合
     * @param parentId, List
     * @return List
     */
    public static List<Integer> getChildrenPermissionIds(Integer parentId, List<Permission> permissions) {
        List<Integer> list = new ArrayList<Integer>();
        for (Permission permission : permissions) {
            if ((parentId == null && permission.getParentId() == null) || (parentId != null && permission.getParentId() != null && parentId.intValue() == permission.getParentId().intValue())) {
                list.add(permission.getId());
                list.addAll(getChildrenPermissionIds(permission.getId(), permissions));
            }
        }
        return list;
    }
    
    /**
     * 获取根权限
     *
     * @param List
     * @return List
     */
    private List<Permission> getRootPermissions(List<Permission> permissionList) {
        List<Permission> rootPermissions = new ArrayList<Permission>();
        for (Permission permission : permissionList) {
            //如果父级id是null
            if (permission.getParentId() == null) {
                rootPermissions.add(permission);
                continue;
            }
            Permission parentPermission = this.get(permissionList, permission.getParentId().intValue());
            if (parentPermission == null) {
                rootPermissions.add(permission);
            }
        }
        return rootPermissions;
    }

    /**
     * 获取登录用户权限有无情况
     * @param accountId
     * @return
     */
	@Override
	public List<Permission> isExitPermission(Integer accountId) {
		// TODO Auto-generated method stub
		return permissionMapper.isExitPermission(accountId);
	}
	
	/*
     * 获取所有vts权限
     * @param
     * @return List
     */
    public List<Permission> getVtsPermissions(){
    	return permissionMapper.getVtsPermissions();
    }

}
