package com.lion.upms.dao.department;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.department.Department;

import java.util.List;

/**
 * @author mr.liu
 * @title: DepartmentDao
 * @description: 部门dao
 * @date 2020/8/15下午5:35
 */
public interface DepartmentDao extends BaseDao<Department> ,DepartmentDaoEx {


    /**
     * 根据父节点ID查询部门
     * @param parentId
     * @return
     */
    public List<Department> findByParentId(Long parentId);

    /**
     * 根据父节点ID和名称查询同节点部门
     * @param parentId
     * @param name
     * @return
     */
    public Department findFirstByParentIdAndName(Long parentId,String name);
}
