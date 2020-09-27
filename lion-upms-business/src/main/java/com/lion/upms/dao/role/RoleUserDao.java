package com.lion.upms.dao.role;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.role.RoleUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author mr.liu
 * @title: RoleUserDao
 * @description: 角色与用户关联dao
 * @date 2020/8/15下午5:14
 */
public interface RoleUserDao extends BaseDao<RoleUser> ,RoleUserDaoEx {

    /**
     * 根据角色id和用户id删除角色关联的用户
     * @param roleId
     * @param userId
     * @return
     */
    public int deleteByRoleIdAndUserIdIn(Long roleId, List<Long> userId);

    /**
     * 根据角色id和用户id查询角色所关联的用户
     * @param roleId
     * @param userId
     * @return
     */
    public List<RoleUser> findByRoleIdAndUserIdIn(Long roleId, List<Long> userId);
}
