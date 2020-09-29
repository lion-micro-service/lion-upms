package com.lion.upms.dao.role;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.role.Role;

/**
 * @author mr.liu
 * @title: RoleDao
 * @description: 角色dao
 * @date 2020/8/15下午5:16
 */
public interface RoleDao extends BaseDao<Role> ,RoleDaoEx {

    /**
     * 根据名称查询角色
     * @param name
     * @return
     */
    public Role findFirstByName(String name);

    /**
     * 根据编码查询角色
     * @param code
     * @return
     */
    public Role findFirstByCode(String code);


}
