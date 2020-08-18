package com.lion.upms.dao.role;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.role.RoleUser;

/**
 * @author mr.liu
 * @title: RoleUserDao
 * @description: 角色与用户关联dao
 * @date 2020/8/15下午5:14
 */
public interface RoleUserDao extends BaseDao<RoleUser> ,RoleUserDaoEx {
}
