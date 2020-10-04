package com.lion.upms.dao.role;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.role.RoleResources;

import java.util.List;

/**
 * @author mr.liu
 * @title: RoleResourcesDao
 * @description: 角色与资源关联dao
 * @date 2020/8/15下午5:18
 */
public interface RoleResourcesDao extends BaseDao<RoleResources> ,RoleResourcesDaoEx {

    /**
     * 根据角色id删除权限（资源）
     * @param roleId
     */
    public void deleteByRoleId(Long roleId);

    /**
     * 根据角色id获取所有权限（资源）
     * @param roleId
     * @return
     */
    public List<RoleResources> findAllByRoleId(Long roleId);


    /**
     * 根据资源ID删除角色与权限(资源)的关联
     * @param resourcesId
     */
    public void deleteByResourcesId(Long resourcesId);
}
