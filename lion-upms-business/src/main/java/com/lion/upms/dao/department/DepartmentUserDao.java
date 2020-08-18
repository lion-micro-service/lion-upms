package com.lion.upms.dao.department;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.department.DepartmentUser;

/**
 * @author mr.liu
 * @title: DepartmentUserDao
 * @description: 部门与用户关联dao
 * @date 2020/8/15下午5:55
 */
public interface DepartmentUserDao extends BaseDao<DepartmentUser> ,DepartmentUserDaoEx {
}
