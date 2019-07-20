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

import com.hdnav.dao.DepartmentAccountMapper;
import com.hdnav.dao.DepartmentMapper;
import com.hdnav.entity.Department;
import com.hdnav.entity.vo.DepartmentCreateVO;
import com.hdnav.entity.vo.DepartmentEditVO;
import com.hdnav.entity.vo.Pager;
import com.hdnav.entity.vo.PagerReqVO;
import com.hdnav.entity.vo.ResultVO;
import com.hdnav.entity.vo.TreeNode;
import com.hdnav.service.DepartmentService;
import com.hdnav.utils.Constants;
import com.hdnav.utils.StrKit;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             部门service接口实现类    
 * JDK version used:     JDK1.8                             
 * Author：                                            高仲秋               
 * Create Date：                             2017-6-5
*/
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
    private DepartmentMapper departmentMapper;
	@Autowired
	private DepartmentAccountMapper departmentAccountMapper;
    
    
    private Map<String,Object> getDepartMap(List<Department> deps,Department dep){
    	Map<String,Object> m = new HashMap<String,Object>();
    	Department tDepartment = new Department();
		m.put("id", dep.getId());
		m.put("name", dep.getName());
		m.put("order", dep.getOrder());
		m.put("parentId", dep.getParentId());
		m.put("administrationLevel", dep.getAdministrationLevel());
		m.put("businessLevel", dep.getBusinessLevel());
		m.put("useMark", dep.getUseMark());
		m.put("inputMan", dep.getInputMan());
		m.put("inputTime", dep.getInputTime());
		if(dep.getParentId()!=null&&!Constants.SUPER_TREE_NODE.equals(dep.getParentId().toString()))
			tDepartment = this.get(deps,dep.getParentId());
		    if(tDepartment!=null)
			m.put("parentName", tDepartment.getName());
		return m;
    }

    /**
     * 返回所有部门或该 部门以及其下的子部门
     * @param pagerReqVO
     * @param tree
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Pager<Map<String,Object>> getPageDepartments(PagerReqVO pagerReqVO,TreeNode tree) {
        List<Map<String,Object>> resultDeparts = new ArrayList<Map<String,Object>>();
        List<Department> deps = departmentMapper.selectAll();
        int size = 0;
        if(pagerReqVO==null||pagerReqVO.getLimit()==0){
        	for(Department dep : deps){
        		resultDeparts.add(getDepartMap(deps,dep));
        	}
        	size = deps.size();
        }else if(tree!=null && StrKit.notBlank(tree.getId())&&!Constants.SUPER_TREE_NODE.equals(tree.getId())){
            //获取所有部门
            int child = Integer.parseInt(tree.getId());
            //获取当前部门
            Department selectdepart = this.get(deps,child);
            resultDeparts.add(getDepartMap(deps,selectdepart));
            //获取当前部门的子部门
            List<Department> childrens = getChildrenDepartments(deps,child);
            
            size = childrens.size();
            if(childrens!=null&&size>0){
                if(childrens.size()<pagerReqVO.getLimit()){
                	for(Department dep : childrens)
                		resultDeparts.add(getDepartMap(deps,dep));
                }else{
                    for(int i = (pagerReqVO.getPageNo()-1)*pagerReqVO.getLimit();i<Math.min(pagerReqVO.getLimit()*pagerReqVO.getPageNo(),size);i++){
                        resultDeparts.add(getDepartMap(deps,childrens.get(i)));
                    }
                }
            }
        }else{
        	size = deps.size();
        	List<Department> _deps = departmentMapper.selectPage(pagerReqVO.getLimit(),pagerReqVO.getOffset());
        	for(Department dep : _deps){
        		resultDeparts.add(getDepartMap(deps,dep));
        	}

        }
		return new Pager(resultDeparts,size);
	}


    /**
     * 创建部门
     *
     * @param createVO
     * @return
     */
    public ResultVO createDepartment(DepartmentCreateVO createVO) {
        ResultVO resultVO = new ResultVO(true);
        Integer parentId = createVO.getParentId();
        /*if (parentId != null) {
            //获取所有部门
            List<Department> departments = departmentDao.selectAll();
            //获取部门
            Department department = departmentDao.get(departments, parentId);
            if (department == null) {
                resultVO.setOk(false);
                resultVO.setMsg("上级部门不存在");
                return resultVO;
            }
        }*/
        if(Constants.SUPER_TREE_NODE.equals(parentId))
        	parentId=null;
        Department department = new Department();
        department.setParentId(parentId);
        department.setName(createVO.getName());
        department.setOrder(createVO.getOrder());
        department.setAdministrationLevel(createVO.getAdministrationLevel());
        department.setBusinessLevel(createVO.getBusinessLevel());
        departmentMapper.create(department);
        resultVO.setMsg("创建部门成功");
        return resultVO;
    }

    /**
     * 编辑部门
     *
     * @param editVO
     * @return
     */
    public ResultVO editDepartment(DepartmentEditVO editVO, int accountId) {
        ResultVO resultVO = new ResultVO(true);
        Integer parentId = editVO.getParentId();
        //获取所有部门
        List<Department> departments = departmentMapper.selectAll();
        Department myDepartment = this.get(departments, editVO.getId());
        if(myDepartment==null){
            resultVO.setOk(false);
            resultVO.setMsg("部门不存在");
            return resultVO;
        }
        //判断是否是自己的部门
        /*Integer myDepId = departmentAccountMapper.getDepIdByAccountId(accountId);
        if (myDepId == null) {
            resultVO.setOk(false);
            resultVO.setMsg("用户部门不存在");
            return resultVO;
        }*/
        //上级id是否在自己所在部门或者下级
        Set<Integer> childrenDepIds = getChildrenDepIds(departments, editVO.getId());
        childrenDepIds.add(editVO.getId());

        if(childrenDepIds.contains(editVO.getParentId())){
            resultVO.setOk(false);
            resultVO.setMsg("所在部门的上级不能为自己所在部门或者下级部门");
            return resultVO;
        }
        if(Constants.SUPER_TREE_NODE.equals(parentId))
        	parentId=null;
        Department department = new Department();
        department.setId(editVO.getId());
        department.setName(editVO.getName());
        department.setOrder(editVO.getOrder());
        department.setParentId(parentId);
        department.setAdministrationLevel(editVO.getAdministrationLevel());
        department.setBusinessLevel(editVO.getBusinessLevel());
        departmentMapper.update(department);
        resultVO.setMsg("编辑部门成功");
        resultVO.setFlag(1);
        return resultVO;
    }

    /**
     * 删除部门
     *
     * @param depId
     * @param accountId
     * @return
     */
    public ResultVO deleteDep(ArrayList<Integer> depIds, int accountId) {
        ResultVO resultVO = new ResultVO(true);
        //获取所有部门
        List<Department> departments = departmentMapper.selectAll();
        List<Integer> DepartmentId = departmentAccountMapper.selectAllDepartmentId();
        boolean bool = false;
        for(int depId : depIds){
	        //获取部门
	        Department department = this.get(departments, depId);
	        if (department == null) {
	            resultVO.setOk(false);
	            resultVO.setMsg("部门不存在!");
	            return resultVO;
	        }
	        //判断是否是自己的部门
	        Integer myDepId = departmentAccountMapper.getDepIdByAccountId(accountId);
	        if (myDepId != null && myDepId.intValue() == depId) {
	            resultVO.setOk(false);
	            resultVO.setMsg("不能删除自己所在部门!");
	            return resultVO;
	        }
	        //查看部门id是否存在	LiuHe
	        bool = DepartmentId.contains(depId);
	        //判断部门下有没有用户  LiuHe
	        if(bool==true){
	        	resultVO.setOk(false);
	        	resultVO.setMsg("此部门下有用户，请先删除此部门下用户，再进行删除操作!");
	        	return resultVO;
	        }
        }
        for(int depId : depIds){
	        //获取子级部门id
	        Set<Integer> childrenDepIds = getChildrenDepIds(departments, depId);
	        //删除
	        for (Integer id : childrenDepIds) {
	        	
	        	bool = DepartmentId.contains(id);
	        	
		        if(bool==true){
		        	resultVO.setOk(false);
		        	resultVO.setMsg("此部门下有用户，请先删除此部门下用户，再进行删除操作!");
		        	return resultVO;
		        }
		        departmentMapper.delete(id);
		        departmentAccountMapper.deleteByDepId(id);
	        }
	        departmentMapper.delete(depId);
	        departmentAccountMapper.deleteByDepId(depId);
        }
        resultVO.setMsg("删除部门成功");
        resultVO.setFlag(1);
        return resultVO;
    }

    /**
     * 获取子部门
     * @param list
     * @param depId
     * @return
     */
    public static List<Department> getChildrenDepartments(List<Department> list, Integer depId) {
        List<Department> depIdList = new ArrayList<Department>();
        for (Department department : list) {
            Integer departmentId = department.getParentId();
            if ((departmentId == null && depId == null) || (departmentId != null && depId != null && departmentId.intValue() == depId.intValue())) {
                depIdList.add(department);
                depIdList.addAll(getChildrenDepartments(list, department.getId()));
            }
        }
        return depIdList;
    }

    /**
     * 获取子部门id
     * @param list
     * @param depId
     * @return
     */
    public static Set<Integer> getChildrenDepIds(List<Department> list, Integer depId) {
        Set<Integer> depIdList = new HashSet<Integer>();
        for (Department department : list) {
            Integer departmentId = department.getParentId();
            if ((departmentId == null && depId == null) || (departmentId != null && depId != null && departmentId.intValue() == depId.intValue())) {
                depIdList.add(department.getId());
                depIdList.addAll(getChildrenDepIds(list, department.getId()));
            }
        }
        return depIdList;
    }

    /**
     * 获取子级部门
     *
     * @param list
     * @param depId
     * @return
     */
    public static List<Map<String, Object>> getChildrenDeps(List<Department> list, Integer depId, Set<Integer> chidlrenDepIdSet,Set<Integer> checkedIdSet) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        for (Department department : list) {
            Integer departmentParentId = department.getParentId();
            if ((departmentParentId == null && depId == null) || (departmentParentId != null && depId != null && departmentParentId.intValue() == depId.intValue())) {
                if (chidlrenDepIdSet == null || !chidlrenDepIdSet.contains(department.getId().intValue())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", department.getId());
                    map.put("text", department.getName());
                    map.put("order", department.getOrder());
                    if(checkedIdSet!=null&&checkedIdSet.contains(department.getId().intValue())){
                        map.put("checked", true);
                   }else{
                        map.put("checked", false);
                    }
                    map.put("children", getChildrenDeps(list, department.getId(), chidlrenDepIdSet, checkedIdSet));
                    mapList.add(map);
                }
            }
        }

        return mapList;
    }

    /**
     * 获取 部门树
     */
	@Override
	public ResultVO getDeptTree(int userId) {
        //int mydepart = departmentAccountMapper.getDepIdByAccountId(userId);
        List<TreeNode> trees = new ArrayList<TreeNode>();
        Pager<Map<String,Object>> pager = this.getPageDepartments(null,null);
        List<Map<String,Object>> list = pager.getRows();
        TreeNode superTree = new TreeNode();
        superTree.setId(Constants.SUPER_TREE_NODE);
        String allDep="所有部门";
        superTree.setName(allDep);
        superTree.setOpen(true);
        trees.add(superTree);
        for(Map<String,Object> depart:list){
            TreeNode tree = new TreeNode();
            tree.setId(depart.get("id").toString());
            tree.setName(depart.get("name").toString());
            if(depart.get("parentId")!=null)
            	tree.setpId(depart.get("parentId").toString());
            else{
            	tree.setpId(Constants.SUPER_TREE_NODE);
            }
            //if(mydepart==Integer.parseInt(depart.get("id").toString()))
            	tree.setOpen(true);
            trees.add(tree);
        }
        ResultVO resultVO = new ResultVO(true);
        resultVO.setData(trees);
		return resultVO;
	}

	@Override
    /**
     * 获取部门
     *
     * @param list
     * @param id
     * @return
     */
    public Department get(List<Department> list, int id) {
        for (Department department : list) {
            if (department.getId() == id) {
                return department;
            }
        }
        return null;
    }

	@Override
	public Department selectAccountDep(Integer accountId) {
		// TODO Auto-generated method stub
		return departmentMapper.selectAccountDep(accountId);
	}
}
