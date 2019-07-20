package com.hdnav.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdnav.dao.AccountMapper;
import com.hdnav.dao.AccountRoleMapper;
import com.hdnav.dao.DepartmentAccountMapper;
import com.hdnav.dao.DepartmentMapper;
import com.hdnav.dao.PermissionMapper;
import com.hdnav.dao.RoleMapper;
import com.hdnav.dao.RolePermissionMapper;
import com.hdnav.entity.Account;
import com.hdnav.entity.AccountRole;
import com.hdnav.entity.Department;
import com.hdnav.entity.DepartmentAccount;
import com.hdnav.entity.Permission;
import com.hdnav.entity.Role;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.entity.vo.UserCreateVO;
import com.hdnav.entity.vo.UserEditVO;
import com.hdnav.service.AccountService;
import com.hdnav.utils.Constants;
import com.hdnav.utils.StrKit;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             账户service接口实现类    
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋               
 * Create Date：                             2017-6-5
*/
@Service
public class AccountServiceImpl implements AccountService{

    @Autowired
    private PermissionMapper permissionMapper;
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private AccountRoleMapper accountRoleMapper;
    
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    
    @Autowired
    private AccountMapper accountMapper;
    
    @Autowired
    private DepartmentMapper departmentMapper;
    
    @Autowired
    private DepartmentAccountMapper departmentAccountMapper;
    
    @Autowired
    private PermissionServiceImpl permissionServiceImpl;

    @Autowired
    private DepartmentServiceImpl departmentServiceImpl;
    
    @Autowired
    private RoleServiceImpl roleServiceImpl;
	
	@Override
	public Account getAccountByAccount(String account) {
		return accountMapper.getByAccount(account);
	}

    /**
     * 根据账号id获取授权
     *
     * @param accountId
     * @return
     */
	@Override
    public SimpleAuthorizationInfo getAccountRolePermission(int accountId) {
        SimpleAuthorizationInfo token = new SimpleAuthorizationInfo();
        //获取所有权限
        List<Permission> permissionList = permissionMapper.selectAll();
        //用户角色名字
        Set<String> roleNameSet = new HashSet<String>();
        //权限名字
        Set<String> perNameSet = new HashSet<String>();
        //获取所有角色
        List<Role> roles = roleMapper.selectAll();
        //获取用户角色id
        List<Integer> roleIdSet = accountRoleMapper.selectRoleIdSet(accountId);
        if (roleIdSet != null && !roleIdSet.isEmpty()) {
            for (Integer roleId : roleIdSet) {
                Role role = roleServiceImpl.get(roles, roleId);
                if (role != null) {
                    roleNameSet.add(role.getKey());
                }
                //获取权限id集合
                List<Integer> permissionIdSet = rolePermissionMapper.getPermissionIdSetByRoleId(roleId);
                if (permissionIdSet != null && !permissionIdSet.isEmpty()) {
                    for (Integer permissionId : permissionIdSet) {
                        //获取权限
                        Permission permission = permissionServiceImpl.get(permissionList, permissionId);
                        String key = permission.getKey();
                        if (StringUtils.isNotBlank(key)) {
                            perNameSet.add(key);
                        }
                    }
                }
            }
        }
        //设置角色权限
        token.setRoles(roleNameSet);
        token.setStringPermissions(perNameSet);
        return token;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Pager<Map<String,Object>> getPageUsers(PagerReqVO pagerReqVO,TreeNode tree){
   	    List<Map<String,Object>> resultAccounts = new ArrayList<Map<String,Object>>();
   	    //获取所有部门
        List<Department> departments  = departmentMapper.selectAll();
        //获取所有用户
   	    List<Account> accounts = accountMapper.selectAll();
   	    //获取所有角色
        List<Role> roles = roleMapper.selectAll();
        //获取所有部门用户关系
        List<DepartmentAccount> departAccounts = departmentAccountMapper.selectAll();
        
        int size = 0;
        if(tree!=null && StrKit.notBlank(tree.getId())&&!Constants.SUPER_TREE_NODE.equals(tree.getId())){
            int child = Integer.parseInt(tree.getId());
            //获取当前部门
            Department selectdepart = departmentServiceImpl.get(departments,child);
            
            //获取当前部门的子部门
            List<Department> depts = DepartmentServiceImpl.getChildrenDepartments(departments,child);
            depts.add(selectdepart);
            
            //获取当前部门的子部门
            List<DepartmentAccount> childrens = getChildrenDepartAccounts(departAccounts,depts);
            size = childrens.size();
            if(childrens!=null&&size>0){
                if(childrens.size()<pagerReqVO.getLimit()){
               	 for(DepartmentAccount departmentAccount : childrens){
               		 Account account = this.get(accounts, departmentAccount.getAccountId());
               		 resultAccounts.add(getAccountDepartRoles(accounts,departments,roles,departmentAccount,account));
               	 }
                }else{
                    for(int i = (pagerReqVO.getPageNo()-1)*pagerReqVO.getLimit();i<Math.min(pagerReqVO.getLimit()*pagerReqVO.getPageNo(),size);i++){
                   	 Account account = this.get(accounts, childrens.get(i).getAccountId());
                        resultAccounts.add(getAccountDepartRoles(accounts,departments,roles,childrens.get(i),account));
                    }
                }
            }
        }else{
       	 size = accounts.size();
       	 List<Account> _accounts = accountMapper.selectPage(pagerReqVO.getLimit(),pagerReqVO.getOffset());
       	 for(Account account:_accounts){
       		 DepartmentAccount departmentAccount = this.getByAccountId(departAccounts, account.getId());
       		 resultAccounts.add(getAccountDepartRoles(accounts,departments,roles,departmentAccount,account));
       	 }
        }
		return new Pager(resultAccounts,size);
   }
    
    public DepartmentAccount getByAccountId(List<DepartmentAccount> list,int accountId){
    	for (DepartmentAccount departmentAccount : list) {
            if (departmentAccount.getAccountId() == accountId) {
            	return departmentAccount;
            }
        }
        return null;
    }
    
    /**
     * 获取用户
     *
     * @param list
     * @param id
     * @return
     */
    public Account get(List<Account> list, int id) {
        for (Account account : list) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null;
    }
   
   public Map<String,Object> getAccountDepartRoles(List<Account> accounts,List<Department> departments,List<Role> roles,DepartmentAccount departmentAccount,Account account){
   	Map<String,Object> map = new HashMap<String,Object>();
   	map.put("id", account.getId());
		map.put("account", account.getAccount());
		map.put("password", account.getPassword());
		map.put("dep_name", departmentServiceImpl.get(departments,departmentAccount.getDepId()).getName());
		map.put("dep_id",departmentAccount.getDepId());
		//用户角色id集合
       List<Integer> roleIdSet = accountRoleMapper.selectRoleIdSet(departmentAccount.getAccountId());
       String roleNames = "";
       String roleIds = "";
       for (Integer roleId : roleIdSet) {
           Role role = roleServiceImpl.get(roles, roleId);
           if(role!=null){
               roleNames += "," + role.getName();
               roleIds += "," + role.getId();
           }
       }
       if (StringUtils.isNotBlank(roleNames)) {
           roleNames = roleNames.substring(1);
           roleIds = roleIds.substring(1);
       }
       map.put("roleNames", roleNames);
       map.put("roleIds", roleIds);
       return map;
   }
   
   /**
    * 获取子部门用户
    * @param list
    * @param depId
    * @return
    */
   public static List<DepartmentAccount> getChildrenDepartAccounts(List<DepartmentAccount> list, List<Department> depts) {
       List<DepartmentAccount> depIdList = new ArrayList<DepartmentAccount>();
       for(Department dept : depts){
       	for (DepartmentAccount departAccount : list) {
               Integer departmentId = departAccount.getDepId();
               if(dept.getId()==departmentId){
               	depIdList.add(departAccount);
               }
           }
       }
       return depIdList;
   }

   /**
    * 账号管理
    *
    * @return
    */
//   public ResultVO selectAccount(int userId, int pageNow, int pageSize) {
//       ResultVO resultVO = new ResultVO(true);
//       Map<String, Object> map = new HashMap<String, Object>();
//       //获取用户部门id
//       Integer depIdByAccountId = departmentAccountMapper.getDepIdByAccountId(userId);
//       if (depIdByAccountId == null) {
//           resultVO.setOk(false);
//           resultVO.setMsg("用户部门不存在");
//           return resultVO;
//       }
//       //获取所有部门
//       List<Department> departments = departmentMapper.selectAll();
//       //获取所有子级部门id集合
//       Set<Integer> childrenDepIds = DepartmentServiceImpl.getChildrenDepIds(departments, depIdByAccountId);
//       childrenDepIds.add(depIdByAccountId);
//       //排除不查询的账号id
//       Set<Integer> excludeAccountIdSet = new HashSet<>();
//       excludeAccountIdSet.add(userId);
//
//
//       PageResult<Account> pageInfo = accountMapper.selectAccountManage(childrenDepIds, excludeAccountIdSet, pageNow, pageSize);
//       List<Account> mapList = pageInfo.getResults();
//
//       //获取所有角色
//       List<Role> roles = roleMapper.selectAll();
//       for (Account m : mapList) {
//           Object idObj = m.get("id");
//           //用户角色id集合
//           List<Integer> roleIdSet = accountRoleMapper.selectRoleIdSet(Integer.parseInt(idObj.toString()));
//           String roleNames = "";
//           for (Integer roleId : roleIdSet) {
//               Role role = roleMapper.get(roles, roleId);
//               roleNames += "," + role.getName();
//           }
//           if (StringUtils.isNotBlank(roleNames)) {
//               roleNames = roleNames.substring(1);
//           }
//           m.set("roleNames", roleNames);
//       }
//       map.put("total", pageInfo.getResultCount());
//       map.put("rows", mapList);
//       resultVO.setData(map);
//       return resultVO;
//   }
   
   /**
    * 添加账号
    *
    * @param createVO
    * @return
    */
   @Override
   public ResultVO saveUser(UserCreateVO createVO) {
       ResultVO resultVO = new ResultVO(true);
       Account byAccount = accountMapper.getByAccount(createVO.getAccount());
       if (byAccount != null) {
           resultVO.setOk(false);
           resultVO.setMsg("账号已存在");
           return resultVO;
       }

       //判断选择 部门是否合法
       if(createVO.getDep()==0){
       	 resultVO.setMsg("不可以选择所有部门！");
       	 return resultVO;
       }
       	Account account = new Account();
           account.setAccount(createVO.getAccount());
           account.setPassword(createVO.getPassword());
           account.setRegisterTime(new Date());
           accountMapper.save(account);
           
           byAccount = accountMapper.getByAccount(createVO.getAccount());
           DepartmentAccount departmentAccount = new DepartmentAccount();
       	departmentAccount.setAccountId(byAccount.getId());
       	departmentAccount.setDepId(createVO.getDep());
       	departmentAccountMapper.save(departmentAccount);
       	resultVO.setMsg("注册成功");
       	resultVO.setFlag(1);
        return resultVO;
   }
   
   /**
    * 修改账号
    *
    * @param createVO
    * @return
    */
   @Override
   public ResultVO editUser(UserEditVO editVO) {
       ResultVO resultVO = new ResultVO(true);
       //Account account = accountMapper.getById(editVO.getId());
	   Account account=new Account();
       //修改
       account.setAccount(editVO.getAccount());
       account.setPassword(editVO.getPassword());
       account.setRegisterTime(new Date());
       int count = accountMapper.update(account);
     
       //DepartmentAccount departmentAccount = departmentAccountMapper.getByAccountId(account.getId());
       //departmentAccount.setDepId(editVO.getDep());
       
       //判断选择 部门是否合法 lixiao
       //if(editVO.getDep()==0){
      			//resultVO.setMsg("不可以选择所有部门！");
       //}else{
       //departmentAccountMapper.update(departmentAccount);
       //resultVO.setMsg("编辑用户成功");
       //resultVO.setFlag(1);
       //}
       if(count == 1) {
    	   resultVO.setMsg("success");
       }else {
    	   resultVO.setMsg("failed");
       }
       return resultVO;
   }

   /**
    * 删除用户
    * @param userId
    * @return
    */
   @Override
   public ResultVO deleteUser(ArrayList<Integer> userIds) {
       ResultVO resultVO = new ResultVO(true);
       for(int userId : userIds){
       	//删除账号
           accountMapper.delete(userId);
           //删除账号与角色关联
           accountRoleMapper.deleteByAccountId(userId);
           //删除账号与部门关联
           departmentAccountMapper.deleteByAccountId(userId);
       }
       resultVO.setMsg("删除账号成功");
       return resultVO;
   }

   /**
    * 修改用户部门
    * @param userId
    * @param depId
    * @return
    */
   @Override
   public ResultVO updateUserDep(int userId, int depId) {
   	 ResultVO resultVO = new ResultVO(true);
   	 
       if(depId==0){
      	 resultVO.setMsg("不可以选择所有部门！");
      }else{
   	   
       Integer depIdByAccountId = departmentAccountMapper.getDepIdByAccountId(userId);
       if(depIdByAccountId!=null){
           departmentAccountMapper.deleteByAccountId(userId);
       }
       DepartmentAccount departmentAccount = new DepartmentAccount();
       departmentAccount.setDepId(depId);
       departmentAccount.setAccountId(userId);
    
     //判断选择 部门是否合法 lixiao
     
       	 departmentAccountMapper.save(departmentAccount);
       	 resultVO.setMsg("分配用户部门成功");
       	 resultVO.setFlag(1);
       }
      
       return resultVO;
   }

	@Override
	public ResultVO grantRoles(int userId, ArrayList<Integer> roleArray) {

       ResultVO resultVO = new ResultVO(true);
       //获取所有角色
       List<Account> accounts = accountMapper.selectAll();
       //获取角色
       Account account = this.get(accounts, userId);
       if (account == null) {
           resultVO.setOk(false);
           resultVO.setMsg("用户不存在");
           return resultVO;
       }

       Set<Integer> roleIdSet = new HashSet<Integer>();
       if(roleArray != null && roleArray.size() > 0){
    	   for (Integer id : roleArray) {
    		   roleIdSet.add(id);
    	   }
       }

       //删除用户角色
       accountRoleMapper.deleteByAccountId(userId);
       //授权
       if (!roleIdSet.isEmpty()) {
           for (Integer roleId : roleIdSet) {
               AccountRole accountRole = new AccountRole();
               accountRole.setAccountId(userId);
               accountRole.setRoleId(roleId);
               accountRoleMapper.createAccountRole(accountRole);
           }
       }
       resultVO.setMsg("角色分配成功");
       resultVO.setFlag(1);
       return resultVO;
	}

	/**
	  * 获取所有角色
	  * @return List
	 */
	@Override
	public List<Account> getAllAccounts() {
		//获取所有角色
	    return accountMapper.safeSelectAll();
	}

}
