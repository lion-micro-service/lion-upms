package com.lion.upms.service.department;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.department.vo.DepartmentTreeVo;
import javassist.runtime.Inner;

import java.util.List;
import java.util.Optional;

/**
 * @author mr.liu
 * @title: DepartmentService
 * @description: 部门service
 * @date 2020/8/15下午5:36
 */
public interface DepartmentService extends BaseService<Department> {

    /**
     * 获取部门树形结构
     * @return
     */
    public List<DepartmentTreeVo> listTree();

    /**
     * 根据父节点ID和名称查询同节点部门
     * @param parentId
     * @param name
     * @return
     */
    public Optional<Department> findDepartment(Long parentId, String name);

    /**
     * 根据父节点ID检查同节点的名称是否存在
     * @param parentId
     * @param name
     * @param id
     * @return
     */
    public Boolean checkNameIsExist(Long parentId,String name,Long id);

    /**
     * 删除部门
     * @param id
     * @return
     */
    public void delete(Long id);

    /**
     * 获取所有子节点
     * @param id
     * @return
     */
    public List<Department> findAllChilder(Long id);

    /**
     * 获取所有父节点(链表结构)
     * @param parentId
     * @return
     */
    public DepartmentTreeVo findTreeParentDepartment(Long parentId);

    /**
     * 获取所有父节点
     * @param parentId
     * @return
     */
    public List<Department> findAllParentDepartment(Long parentId);
}
