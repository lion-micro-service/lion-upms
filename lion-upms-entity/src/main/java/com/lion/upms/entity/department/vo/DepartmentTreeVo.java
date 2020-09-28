package com.lion.upms.entity.department.vo;

import com.lion.upms.entity.department.Department;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;
import lombok.Data;

import java.util.List;

/**
 * @description: 部门树形结构VO
 * @author: mr.liu
 * @create: 2020-09-28 14:17
 **/
@Data
public class DepartmentTreeVo extends Department {

    /**
     * 子节点
     */
    private List<DepartmentTreeVo> children;


}
