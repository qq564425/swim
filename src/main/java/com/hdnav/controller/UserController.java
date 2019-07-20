package com.hdnav.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hdnav.entity.Account;
import com.hdnav.entity.Permission;
import com.hdnav.entity.vo.LoginVO;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.entity.vo.UserCreateVO;
import com.hdnav.entity.vo.UserEditDepVO;
import com.hdnav.entity.vo.UserEditVO;
import com.hdnav.ip2region.DataBlock;
import com.hdnav.ip2region.DbConfig;
import com.hdnav.ip2region.DbSearcher;
import com.hdnav.redis.RedisManager;
import com.hdnav.service.AccountService;
import com.hdnav.service.PermissionService;
import com.hdnav.utils.AuthConfigPropertyUtils;
import com.hdnav.utils.Constants;
import com.hdnav.utils.DataUtil;
import com.hdnav.utils.PathKit;

import redis.clients.jedis.Jedis;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             用户controller        
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣               
 * Create Date：                           2017-6-7 
*/ 
@RestController
@RequestMapping("/user")
public class UserController{
	private Jedis jedis;
	@Value("${jedis.pool.host}")
    private String jedisIp;
    
    @Value("${jedis.pool.port}")
    private Integer JedisPort;
	@Autowired
    private AccountService accountService;
    
    /* @Resource
       private JmsTemplate jmsTemplate;
    */
	@Resource(name = "authRedisManager")
	private RedisManager redisManager;
    
    @Resource
    private  PermissionService permissionService;

    //跳转到用户管理页面
    @RequestMapping("/manage")
    public String manage() {
        return "user/manage";
    }
    
    /**
    * 用户管理  获取数据
    * @param PagerReqVO 分页对象实体 TreeNode树节点实体
	 * @return Pager
    */
    @GetMapping(value = "/get-manage-user")
    public String getManageUser(PagerReqVO pagerReqVO,TreeNode tree) {
    	Pager<Map<String,Object>> vo = accountService.getPageUsers(pagerReqVO,tree);
        return new Gson().toJson(vo);
    }

    //判断是否登陆
    @PostMapping("/getLoginStatusFromSession")
    public ResultVO getLoginStatusFromSession(@RequestBody Map<String, Object> params) {
    	ResultVO resultVO = new ResultVO(true);
    	resultVO.setMsg("hasNoLogin");
    	//处理session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
        Account account = new Account();
        if(params.get("account")!=null) {
        	account = accountService.getAccountByAccount(params.get("account").toString());
        }
        if(account != null) {
        	for(Session session:sessions){
                //清除该用户以前登录时保存的session
                if(account.getId().toString().equals(String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY)))) {
                	resultVO.setMsg("hasLogin");
                	break;
                }
            }
        }
    	return resultVO;
    }
    
  //判断是否登陆
    @PostMapping("/getLoginStatus")
    public ResultVO getLoginStatus() {
    	ResultVO resultVO = new ResultVO(true);
    	if(SecurityUtils.getSubject() == null || SecurityUtils.getSubject().getSession(false) == null){
    		resultVO.setOk(true);
	        resultVO.setMsg("noLogin");
    	}else{
    		resultVO.setOk(true);
	        resultVO.setMsg("hasLogin");
    	}
    	return resultVO;
    }
    

    /**
    * 退出
    * @param
	 * @return ResultVO
    */
    @RequestMapping("/exit")
    public ResultVO exit(@RequestBody Map<String, Object> params) { 
    	jedis = new Jedis(jedisIp, JedisPort);
    	jedis.select(1);
        try {
        	//session过期以后，点击退出按钮
			if(SecurityUtils.getSubject() == null || SecurityUtils.getSubject().getSession(false) == null){
				ResultVO resultVO = new ResultVO(true);
		        resultVO.setOk(true);
		        resultVO.setMsg("退出登录成功");
		        return resultVO;
			}
			//session没过期，正常删除redis中的session
        	//redisManager.del("loginIn_"+SecurityUtils.getSubject().getSession(false).getId(),1);
	        jedis.del(params.get("userName")+"_MIS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        SecurityUtils.getSubject().logout();
        ResultVO resultVO = new ResultVO(true);
        resultVO.setOk(true);
        resultVO.setMsg("退出登录成功");
        return resultVO;
    }


    /**
    * 验证登录
    * @param
	 * @return ResultVO
    */
    @RequestMapping(value = "/check-login", method = RequestMethod.POST)
    public
    @ResponseBody
    //@Valid @ModelAttribute final LoginVO loginVO
    ResultVO checkLogin(@Valid @RequestBody LoginVO loginVO, BindingResult bindingResult, HttpSession session, HttpServletRequest request) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO resultVO = new ResultVO(true);
        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }       
        //对密码进行Base64加密
        loginVO.setPassword(Base64.encodeToString(loginVO.getPassword().getBytes()));

        //验证登录
        UsernamePasswordToken token = new UsernamePasswordToken(loginVO.getAccount(), loginVO.getPassword(),loginVO.isRememberMe());
        
        //token.setRememberMe(loginVO.isRememberMe()); 
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            e.printStackTrace();
            resultVO.setOk(false);
            resultVO.setMsg("账号或者密码错误");
            return resultVO;
        }
        
        session.setAttribute(Constants.DEFAULT_SESSION_USERNAME, loginVO.getAccount());
        session.setAttribute("supervise", "all");

        final String ip = DataUtil.getIpAddr(request);
        
        DataBlock fdata;
        try {
        	DbSearcher _searcher = new DbSearcher(new DbConfig(), PathKit.getRootClassPath()+"ip2region"+File.separator+"ip2region.db");
        	fdata = _searcher.binarySearch(ip);
		} catch (Exception e) {
			//e.printStackTrace();
			
		}   
        
        //获取用户具有哪些权限，不具有哪些权限
		Account account = accountService.getAccountByAccount(loginVO.getAccount());
		HashMap<String, Boolean> permissionMap = new HashMap<String, Boolean>();
		if(account != null){
			List<Permission> list = permissionService.isExitPermission(account.getId());
			for(Permission permission : list){
				String key= permission.getKey().replace(":", "_");
				permissionMap.put(key, Boolean.parseBoolean(permission.getIsExit()));
			}
			resultVO.setData(permissionMap);
		}
		
		if(account != null){
			//sessionID存入缓存
			//redisManager.set("loginIn_"+session.getId(), account.getId(), 1);
			session.setAttribute("username", account.getAccount());
		}
		resultVO.setServerIp(AuthConfigPropertyUtils.ip);
		resultVO.setServerPort(Integer.parseInt(AuthConfigPropertyUtils.port));
		resultVO.setWebsocketPort(Integer.parseInt(AuthConfigPropertyUtils.websocketPort));
		resultVO.setServerUrl(AuthConfigPropertyUtils.loginUrl);
        return resultVO;
    }

    /**
    * 添加用户
    * @param UserCreateVO 添加的用户实体
	 * @return ResultVO
    */
    @PostMapping(value = "/create")
    public ResultVO create(@Valid @ModelAttribute UserCreateVO createVO, BindingResult bindingResult, HttpSession session, HttpServletRequest request) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO resultVO = new ResultVO(true);

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }       
        //对密码进行Base64加密
        createVO.setPassword(Base64.encodeToString(createVO.getPassword().getBytes()));
        
        resultVO = accountService.saveUser(createVO);
        return resultVO;
    }
    
    /**
    * 编辑用户 UserEditVO 编辑后的用户实体
    * @param
	 * @return ResultVO
    */
    @PostMapping(value = "/edit")
    public ResultVO editDepartment(@Valid @ModelAttribute UserEditVO editVO, BindingResult bindingResult) {
    	ResultVO resultVO = new ResultVO(true);      
        //对密码进行Base64加密
        editVO.setPassword(Base64.encodeToString(editVO.getPassword().getBytes()));
        
        resultVO = accountService.editUser(editVO);
        return resultVO;
    }

    /**
    * 修改用户部门 UserEditDepVO 修改后的用户部门实体
    * @param
	 * @return ResultVO
    */
    @PostMapping(value = "/edit-user-dep")
    public ResultVO editUserDep(@Valid @ModelAttribute UserEditDepVO userEditDepVO, BindingResult bindingResult, HttpSession session, HttpServletRequest request) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        ResultVO resultVO = new ResultVO(true);

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String defaultMessage = fieldErrors.get(0).getDefaultMessage();
            resultVO.setOk(false);
            resultVO.setMsg(defaultMessage);
            return resultVO;
        }
        resultVO=accountService.updateUserDep(userEditDepVO.getUserId(),userEditDepVO.getDepId());
        
        return resultVO;
    }


    /**
    * 删除用户 userIds 删除对象的Id集合
    * @param
	 * @return ResultVO
    */
    @PostMapping(value = "/delete")
    public ResultVO delete(@RequestBody Map<Object, Object> params) {
    	@SuppressWarnings("unchecked")
		ArrayList<Integer> userIds = (ArrayList<Integer>) params.get("userIdStr");
        ResultVO resultVO = accountService.deleteUser(userIds);
        return resultVO;
    }

    /**
    * 角色分配
    * @param userId 用户的id roleArray 当前用户的角色id 没有角色即为空
	 * @return ResultVO
    */
    @PostMapping(value = "/grant")
    //@RequestParam int userId,@RequestParam(value = "roleArray[]",required = false) Integer [] roleArray
    public ResultVO grantUserRoles(@RequestBody Map<Object, Object> params) {
    	int userId = (int) params.get("userId");
    	@SuppressWarnings("unchecked")
		ArrayList<Integer> roleArray = (ArrayList<Integer>) params.get("roleStr");
        ResultVO resultVO = accountService.grantRoles(userId,roleArray);
        return resultVO;
    }
}
