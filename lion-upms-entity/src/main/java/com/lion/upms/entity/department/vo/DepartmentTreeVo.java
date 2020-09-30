package com.lion.upms.entity.department.vo;

import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;
import lombok.Data;

import java.util.List;

/**
 * @description: 部门树形结构VO(双向链表数据结构)
 * @author: mr.liu
 * @create: 2020-09-28 14:17
 **/
@Data
public class DepartmentTreeVo extends Department {

    /**
     * 父节点
     */
    private Department  parentDepartment;

    /**
     * 子节点
     */
    private List<DepartmentTreeVo> children;


}
