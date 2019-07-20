package com.hdnav.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdnav.dao.MenuMapper;
import com.hdnav.dao.MenuPermissionMapper;
import com.hdnav.dao.PermissionMapper;
import com.hdnav.entity.Menu;
import com.hdnav.entity.MenuPermission;
import com.hdnav.entity.Permission;
import com.hdnav.entity.vo.MenuCreateVO;
import com.hdnav.entity.vo.MenuEditVO;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.service.MenuService;
import com.hdnav.utils.Constants;
import com.hdnav.utils.StrKit;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             查询菜单service接口实现类    
 * JDK version used:     JDK1.8                             
 * Author：                                        高仲秋               
 * Create Date：                           2017-6-5
*/
@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuMapper menuMapper;
	
	@Autowired
	private PermissionMapper permissionMapper;
	
	@Autowired
	private PermissionServiceImpl permissionServiceImpl;
	
	@Autowired
	private MenuPermissionMapper menuPermissionMapper;
	
	/**
     * 获取菜单map
     * @param List, Menu
     * @return Map
     */
	private Map<String,Object> getMenuMap(List<Menu> menus,Menu menu){
    	Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", menu.getId());
		m.put("name", menu.getName());
		m.put("order", menu.getOrder());
		m.put("icon", menu.getIcon());
		m.put("parentId", menu.getParentId());
		m.put("url", menu.getUrl());
		if(menu.getParentId()!=null&&!Constants.SUPER_TREE_NODE.equals(menu.getParentId().toString()))
		if(this.get(menus,menu.getParentId())==null){
			System.out.println();
		}
		if(menu.getParentId()!=null&&!Constants.SUPER_TREE_NODE.equals(menu.getParentId().toString()))
			m.put("parentName", this.get(menus,menu.getParentId()).getName());
		return m;
    }
	
    /**
     * 获取子部门
     * @param list, _menuId
     * @return List
     */
    public static List<Menu> getChildrenMenus(List<Menu> list, Integer _menuId) {
        List<Menu> menuIdList = new ArrayList<Menu>();
        for (Menu menu : list) {
            Integer menuId = menu.getParentId();
            if ((menuId == null && _menuId == null) || (menuId != null && _menuId != null && menuId.intValue() == _menuId.intValue())) {
            	menuIdList.add(menu);
            	menuIdList.addAll(getChildrenMenus(list, menu.getId()));
            }
        }
        return menuIdList;
    }
	
    /**
     * 获取显示菜单
     * @param rootId
     * @return List
     */
    @Override
    public List<Map<String, Object>> selectShowMenus(Integer rootId) {
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        //授权的菜单集合
        List<Menu> menuPermittedList = new ArrayList<Menu>();
        //获取所有菜单
        List<Menu> menuList = menuMapper.selectAll();
        //获取所有权限
        List<Permission> permissionList = permissionMapper.selectAll();

        Subject subject = SecurityUtils.getSubject();
        for (Menu menu : menuList) {
        	List<Integer> permissionIdSet = menuPermissionMapper.selectPermissionIdSet(menu.getId());
            //权限key集合
            List<String> perKeyList = new ArrayList<String>();
            if (permissionIdSet != null && !permissionIdSet.isEmpty()) {
                for (Integer perId : permissionIdSet) {
                    Permission permission = permissionServiceImpl.get(permissionList, perId.intValue());
                    perKeyList.add(permission.getKey());
                }
            }
            //判断权限
            String[] permissions = perKeyList.toArray(new String[]{});
            boolean permittedAll = subject.isPermittedAll(permissions);
            //if (permittedAll) {
                menuPermittedList.add(menu);
            //}
        }
        List<Integer> childrenIdList = null;
        if (rootId != null) {
            childrenIdList = getChildrenMenuIds(menuList, rootId);
            childrenIdList.add(rootId);
        }

        List<Menu> rootMenus = getRootMenus(menuPermittedList);
        for (Menu menu : rootMenus) {
            if (childrenIdList == null || !childrenIdList.contains(menu.getId())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", menu.getId());
                map.put("text", menu.getName());
                map.put("order", menu.getOrder());
                //菜单额外属性
                Map<String, Object> attrMap = new HashMap<String, Object>();
                attrMap.put("url", menu.getUrl());
                map.put("attributes", attrMap);
                map.put("url", menu.getUrl());
                map.put("icon", menu.getIcon());
                List<Map<String, Object>> childrenMenus = getChildrenMenus(menuPermittedList, menu.getId(), childrenIdList);
                map.put("children", childrenMenus);
                map.put("icon", menu.getIcon());
                maps.add(map);
            }
        }
        return maps;
    }

    /**
     * 创建菜单
     *
     * @param createVO
     * @return ResultVO
     */
	@Override
	public ResultVO createMenu(MenuCreateVO createVO) {
        ResultVO resultVO = new ResultVO(true);
        //获取所有菜单
        List<Menu> menuList = menuMapper.selectAll();
        Integer parentId = createVO.getParentId();

        if(Constants.SUPER_TREE_NODE.equals(parentId))
        	parentId=null;
        //菜单名字不能重复
        for(Menu menu:menuList){
            if(menu.getName().equals(createVO.getName())){
                resultVO.setOk(false);
                resultVO.setMsg("菜单名字已存在");
                return resultVO;
            }
        }

        //添加菜单
        Menu menu = new Menu();
        menu.setOrder(createVO.getOrder());
        menu.setName(createVO.getName());
        menu.setParentId(parentId);
        menu.setUrl(createVO.getUrl());
        menu.setIcon(createVO.getIcon());
        menuMapper.createMenu(menu);
        System.out.println(menu.getId());
        resultVO.setMsg("创建菜单成功");
        resultVO.setFlag(1);
        return resultVO;
	}

	/**
     * 删除菜单
     *
     * @param menuIds
     * @return ResultVO
     */
	@Override
	public ResultVO deleteMenu(ArrayList<Integer> menuIds) {
		 ResultVO resultVO = new ResultVO(true);
	        //获取所有菜单
	        List<Menu> menus = menuMapper.selectAll();
	        for(int menuId : menuIds){
		        //获取菜单
		        Menu menu = this.get(menus, menuId);
		        if (menu == null) {
		            resultVO.setOk(false);
		            resultVO.setMsg("菜单不存在");
		            return resultVO;
		        }
	        }
	        
	        for(int menuId : menuIds){
		        //获取子级菜单
		        List<Integer> childrenMenuIds = getChildrenMenuIds(menus, menuId);
		        //删除菜单
		        menuMapper.deleteMenu(menuId);
		        menuPermissionMapper.deleteByMenuId(menuId);
		        for (Integer id : childrenMenuIds) {
		        	menuMapper.deleteMenu(id);
		        	menuPermissionMapper.deleteByMenuId(id);
		        }
	        }
	        resultVO.setMsg("删除菜单成功");
	        resultVO.setFlag(1);
	        return resultVO;
	}

    /**
     * 编辑菜单
     *
     * @param MenuEditVO
     * @return ResultVO
     */
	@Override
	public ResultVO editMenu(MenuEditVO editVO) {
		 ResultVO resultVO = new ResultVO(true);
	        //获取所有菜单
	        List<Menu> menus = menuMapper.selectAll();
	        //获取菜单
	        Menu menu = this.get(menus, editVO.getId());
	        if (menu == null) {
	            resultVO.setOk(false);
	            resultVO.setMsg("菜单不存在");
	            return resultVO;
	        }
	        //父级菜单
	        if (editVO.getParentId() != null) {
	            Menu parentMenu = this.get(menus, editVO.getParentId());
	           /* if (parentMenu == null) {
	                resultVO.setOk(false);
	                resultVO.setMsg("父级菜单不存在");
	                return resultVO;
	            }*/
	            List<Integer> childrenMenuIds = getChildrenMenuIds(menus, editVO.getId());
	            childrenMenuIds.add(editVO.getId());
	            if (childrenMenuIds.contains(editVO.getParentId())) {
	                resultVO.setOk(false);
	                resultVO.setMsg("所在菜单的上级不能为自己所在菜单或者下级菜单");
	                return resultVO;
	            }
	        }
	        //菜单名字不能重复
	        for(Menu m:menus){
	            if(m.getName().equals(editVO.getName())&&m.getId()!=editVO.getId()){
	                resultVO.setOk(false);
	                resultVO.setMsg("菜单名字已存在");
	                return resultVO;
	            }
	        }
	        //更新菜单
	        Menu update = new Menu();
	        update.setUrl(editVO.getUrl());
	        update.setParentId(editVO.getParentId());
	        update.setName(editVO.getName());
	        update.setOrder(editVO.getOrder());
	        update.setId(editVO.getId());
	        update.setIcon(editVO.getIcon());
	        menuMapper.updateMenu(update);
	        resultVO.setMsg("编辑菜单成功");
	        resultVO.setFlag(1);
	        return resultVO;
	}

	/**
     * 菜单授权
     *
     * @param menuId, perIdArray
     * @return ResultVO
     */
	@Override
	public ResultVO grantPermissions(int menuId, Integer[] perIdArray) {
        ResultVO resultVO = new ResultVO(true);
        //获取所有菜单
        List<Menu> menus = menuMapper.selectAll();
        //获取菜单
        Menu menu = this.get(menus, menuId);
        if (menu == null) {
            resultVO.setOk(false);
            resultVO.setMsg("菜单不存在");
            return resultVO;
        }
        
        if(perIdArray==null||perIdArray.length==0){
        	 resultVO.setOk(false);
             resultVO.setMsg("未选择授权页面");
             return resultVO;
        }

        //获取所有权限
        List<Permission> permissions = permissionMapper.selectAll();
        
        String perInfo = checkOwnPermissions(permissions,perIdArray);
        if(StrKit.notBlank(perInfo)){
        	 resultVO.setOk(false);
             resultVO.setMsg("您没有分配["+perInfo.substring(0,perInfo.length()-1)+"]的权限");
             return resultVO;
        }

        Set<Integer> perIdSet = new HashSet<Integer>();

        for (Integer id : perIdArray) {
            perIdSet.add(id);
        /*    List<Integer> childrenPermissionIds = PermissionServiceImpl.getChildrenPermissionIds(id, permissions);
            perIdSet.addAll(childrenPermissionIds);*/
        }

        //删除菜单权限
        menuPermissionMapper.deleteByMenuId(menuId);
        //授权
        if (!perIdSet.isEmpty()) {
            for (Integer perId : perIdSet) {
                MenuPermission menuPermission = new MenuPermission();
                menuPermission.setMenuId(menuId);
                menuPermission.setPermissionId(perId);
                menuPermissionMapper.addMenuPermission(menuPermission);

            }
        }
        resultVO.setMsg("授权成功");
        resultVO.setFlag(1);
        return resultVO;
	}

	/**
     * 获取菜单列表
     *
     * @param PagerReqVO, TreeNode
     * @return Pager
     */
	@Override
    public Pager<Map<String, Object>> getPageMenus(PagerReqVO pagerReqVO, TreeNode tree){
    	List<Map<String,Object>> resultMenus = new ArrayList<Map<String,Object>>();
    	List<Menu> menus = menuMapper.selectAll();
    	int size = 0;
        if(pagerReqVO==null||pagerReqVO.getLimit()==0){
        	for(Menu menu : menus){
        		resultMenus.add(getMenuMap(menus,menu));
        	}
        	size = menus.size();
        }else if(tree!=null && StrKit.notBlank(tree.getId())&&!Constants.SUPER_TREE_NODE.equals(tree.getId())){
            //获取所有菜单
            int child = Integer.parseInt(tree.getId());
            //获取当前菜单
            Menu selectdepart = this.get(menus,child);
            resultMenus.add(getMenuMap(menus,selectdepart));
            //获取当前部门的子菜单
            List<Menu> childrens = getChildrenMenus(menus,child);

            size = childrens.size();
            if(childrens!=null&&size>0){
                if(childrens.size()<pagerReqVO.getLimit()){
                	for(Menu menu : childrens)
                		resultMenus.add(getMenuMap(menus,menu));
                }else{
                    for(int i = (pagerReqVO.getPageNo()-1)*pagerReqVO.getLimit();i<Math.min(pagerReqVO.getLimit()*pagerReqVO.getPageNo(),size);i++){
                    	resultMenus.add(getMenuMap(menus,childrens.get(i)));
                    }
                }
            }
        }else{
        	size = menus.size();
        	List<Menu> _menus = menuMapper.selectPage(pagerReqVO.getLimit(),pagerReqVO.getOffset());
        	for(Menu menu : _menus){
        		resultMenus.add(getMenuMap(menus,menu));
        	}

        }
		return new Pager(resultMenus,size);
    }

	/**
     * 获取菜单树
     *
     * @param
     * @return ResultVO
     */
	@Override
	public ResultVO getMenuTree() {
        List<TreeNode> trees = new ArrayList<TreeNode>();
        Pager<Map<String,Object>> pager = this.getPageMenus(null,null);
        List<Map<String,Object>> list = pager.getRows();
        TreeNode superTree = new TreeNode();
        superTree.setId(Constants.SUPER_TREE_NODE);
        String allMenu = "";
        try {
			allMenu = new String("所有菜单".getBytes("gbk"), "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        superTree.setName(allMenu);
        superTree.setOpen(true);
        trees.add(superTree);
        for(Map<String,Object> menu:list){
            TreeNode tree = new TreeNode();
            tree.setId(menu.get("id").toString());
            tree.setName(menu.get("name").toString());
            tree.setOpen(true);
            if(menu.get("parentId")!=null)
            	tree.setpId(menu.get("parentId").toString());
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
     * 获取船舶显示菜单
     *
     * @param rootId
     * @return List
     */
	@Override
	public List<Map<String, Object>> selectShipShowMenus(Integer rootId) {
		 List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
	        //授权的菜单集合
	        List<Menu> menuPermittedList = new ArrayList<Menu>();
	        //获取所有菜单
	        List<Menu> menuList = menuMapper.selectAll();
	        //获取所有权限
	        List<Permission> permissionList = permissionMapper.selectAll();

	        Subject subject = SecurityUtils.getSubject();
	        for (Menu menu : menuList) {
	        	List<Integer> permissionIdSet = menuPermissionMapper.selectPermissionIdSet(menu.getId());
	            //权限key集合
	            List<String> perKeyList = new ArrayList<String>();
	            if (permissionIdSet != null && !permissionIdSet.isEmpty()) {
	                for (Integer perId : permissionIdSet) {
	                    Permission permission = permissionServiceImpl.get(permissionList, perId.intValue());
	                    perKeyList.add(permission.getKey());
	                }
	            }
	            //判断权限
	            String[] permissions = perKeyList.toArray(new String[]{});
	            boolean permittedAll = subject.isPermittedAll(permissions);
	            if (permittedAll) {
	                menuPermittedList.add(menu);
	            }
	        }
	        List<Integer> childrenIdList = null;
	        if (rootId != null) {
	            childrenIdList = getChildrenMenuIds(menuList, rootId);
	            childrenIdList.add(rootId);
	        }

	        List<Menu> rootMenus = getRootMenus(menuPermittedList);
	        for (Menu menu : rootMenus) {
	        	if(menu.getId() == 26){
	                if (childrenIdList == null || !childrenIdList.contains(menu.getId())) {
	                	maps = getShipChildrenMenus(menuPermittedList, menu.getId(), childrenIdList);
	                }
	        	}
	 
	        }
	        return maps;
	}
	
    /**
     * 获取所有子级菜单id集合
     *
     * @param menuList, parentId
     * @return List
     */
    private List<Integer> getChildrenMenuIds(List<Menu> menuList, Integer parentId) {
        List<Integer> list = new ArrayList<Integer>();
        for (Menu menu : menuList) {
            if ((parentId == null && menu.getParentId() == null) || (parentId != null && menu.getParentId() != null && parentId.intValue() == menu.getParentId().intValue())) {
                list.add(menu.getId().intValue());
                list.addAll(getChildrenMenuIds(menuList, menu.getId().intValue()));
            }
        }
        return list;
    }
    
    /**
     * 获取子级菜单
     *
     * @param menus, parentId
     * @return List
     */
    private List<Map<String, Object>> getChildrenMenus(List<Menu> menus, Integer parentId, List<Integer> excludeChildrenIdList) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (Menu menu : menus) {
            if ((parentId == null && menu.getParentId() == null) || (parentId != null && menu.getParentId() != null && menu.getParentId().intValue() == parentId.intValue())) {
                if (excludeChildrenIdList == null || !excludeChildrenIdList.contains(menu.getId())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", menu.getId());
                    map.put("text", menu.getName());
                    //菜单额外属性
                    Map<String, Object> attrMap = new HashMap<String, Object>();
                    attrMap.put("url", menu.getUrl());
                    map.put("attributes", attrMap);
                    map.put("url", menu.getUrl());
                    map.put("order", menu.getOrder());
                    map.put("icon", menu.getIcon());
                    List<Map<String, Object>> childrenMenus = getChildrenMenus(menus, menu.getId(), excludeChildrenIdList);
                    map.put("children", childrenMenus);
                    map.put("icon", menu.getIcon());
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }

    /**
     * 获取根菜单
     *
     * @param menus
     * @return List
     */
    private List<Menu> getRootMenus(List<Menu> menus) {
        List<Menu> rootMenus = new ArrayList<Menu>();
        for (Menu menu : menus) {
            if (menu.getParentId() == null) {
                rootMenus.add(menu);
                continue;
            }
            Menu parentMenu = this.get(menus, menu.getParentId().intValue());
            if (parentMenu == null) {
                rootMenus.add(menu);
            }
        }
        return rootMenus;
    }
    
    /**
     * 获取菜单
     * @param menus, id
     * @return Menu
     */
    public Menu get(List<Menu> menus, int id) {
        for(Menu menu:menus){
            if(menu.getId().intValue()==id){
                return menu;
            }
        }
        return null;
    }
    
    /**
     * 获取拥有的权限
     * @param List, perIdArray
     * @return String
     */
    public String checkOwnPermissions(List<Permission> permissions,Integer[] perIdArray){
    	//获取我拥有的权限
        List<Permission> myPermissionList = new ArrayList<Permission>();
        Subject subject = SecurityUtils.getSubject();
        for (Permission permission : permissions) {
            String key = permission.getKey();
            boolean permitted = subject.isPermitted(key);
            if (permitted) {
                myPermissionList.add(permission);
            }
        }
        
    	boolean isAlowed = true;
        String perInfo = "";
        for(Integer perId : perIdArray){
        	boolean subAlowed = false;
        	for(Permission permission : myPermissionList){
        		if(permission != null && permission.getId().intValue()==perId.intValue()){
        			subAlowed = true;
        			break;
        		}
        	}
        	if(!subAlowed){
        		Permission perm = permissionServiceImpl.get(permissions, perId);
        		perInfo+=perm.getName()+",";
        		isAlowed = false;
        	}
        }
        
        if(!isAlowed&&StrKit.notBlank(perInfo)){
             return perInfo.substring(0,perInfo.length()-1);
        }
        return null;
    }
    
    /**
     * 获取船舶子级菜单
     * 
     * @param List, parentId, List
     * @return List
     */
    private List<Map<String, Object>> getShipChildrenMenus(List<Menu> menus, Integer parentId, List<Integer> excludeChildrenIdList) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (Menu menu : menus) {
            if ((parentId == null && menu.getParentId() == null) || (parentId != null && menu.getParentId() != null && menu.getParentId().intValue() == parentId.intValue())) {
                if (excludeChildrenIdList == null || !excludeChildrenIdList.contains(menu.getId())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("url", menu.getUrl());
                    map.put("url_name", menu.getName());
                    List<Map<String, Object>> childrenMenus = getShipChildrenMenus(menus, menu.getId(), excludeChildrenIdList);
                    map.put("url_list", childrenMenus);
                    mapList.add(map);
                }
            }
        }
        return mapList;
    }

	@Override
	public List<Menu> getMenusAndPermission() {
		// TODO Auto-generated method stub
		return menuMapper.getMenusAndPermission();
	}

}
