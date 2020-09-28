package com.lion.upms.service.department;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.department.DepartmentUser;
import com.lion.upms.entity.department.dto.AddDepartmentUserDto;

import java.util.List;

/**
 * @author mr.liu
 * @title: DepartmentUserService
 * @description: 部门与用户关联service
 * @date 2020/8/15下午5:59
 */
public interface DepartmentUserService extends BaseService<DepartmentUser> {

    /**
     * 保存部门与用户的关联
     * @param addDepartmentUserDto
     */
    public void saveUser(AddDepartmentUserDto addDepartmentUserDto);

    /**
     * 根据部门ID和用户ID查询在此部门所关联的用户
     * @param departmentId
     * @param userId
     * @return
     */
    public List<DepartmentUser> findDepartmentUser(Long departmentId, List<Long> userId);

    /**
     * 根据部门ID和用户ID查询不在此部门的用户却在其它部门的用户
     * @param departmentId
     * @param userId
     * @return
     */
    public List<DepartmentUser> findNotInDepartmentUser(Long departmentId,List<Long> userId);
}
