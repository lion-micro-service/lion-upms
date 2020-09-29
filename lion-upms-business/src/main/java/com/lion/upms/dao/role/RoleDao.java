package com.lion.upms.dao.role;

import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.role.Role;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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

    /**
     * 根据用户id查询角色
     * @param userId
     * @return
     */
    @Query(" select u from Role u join RoleUser ru on u.id = ru.roleId where ru.userId in (:userId)")
    public List<Role> findByUserId(Long userId);
}
