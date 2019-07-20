package com.hdnav.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hdnav.entity.Department;

/**
 * CopyRight:            海大船舶导航                         
 * Module ID:            authority-management    
 * Comments:             部门Dao        
 * JDK version used:     JDK1.8                             
 * Author：                                         车广圣               
 * Create Date：                           2017-6-7 
*/ 
public interface DepartmentMapper {

    /**
     * 分页查询
     * @param limit每页条数,offset偏移量
     * @return list
     */
	List<Department> selectPage(@Param("limit")int limit,@Param("offset")int offset);
    /**
     * 查询所有
     * @param 
     * @return list
     */
    List<Department> selectAll();

//    Department get(List<Department> list, int id);
    /**
     * 创建
     * @param Department 创建的部门实体
     * @return int
     */
    int create(Department department);
    /**
     * 删除
     * @param  depId 部门Id
     * @return int
     */
    int delete(int depId);
    /**
     * 更新
     * @param Department 部门实体
     * @return int
     */
    int update(Department department);
    /**
     * 根据用户ID查询其所在部门
     * @param int 用户id
     * @return 部门实体
     */
    Department selectAccountDep(@Param("accountId") Integer accountId);
}
