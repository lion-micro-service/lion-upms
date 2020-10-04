package com.lion.upms.service.role;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.role.RoleUser;
import com.lion.upms.entity.role.dto.AddRoleUserDto;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * @author mr.liu
 * @title: RoleUserService
 * @description: 角色与用户关联Service
 * @date 2020/8/15下午5:04
 */
public interface RoleUserService extends BaseService<RoleUser> {

    /**
     * 保存角色关联的用户
     * @param addRoleUserDto
     */
    public void saveRoleUser(AddRoleUserDto addRoleUserDto);

    /**
     * 根据角色id和用户id查询角色所关联的用户
     * @param roleId
     * @param userId
     * @return
     */
    public List<RoleUser> findRoleUser(Long roleId, List<Long> userId);

    /**
     * 根据用户删除角色与用户的关联
     * @param userId
     */
    public void deleteByUserId(Long userId);

    /**
     * 根据角色id删除角色与用户的关联
     * @param roleId
     */
    public void deleteByRoleId(Long roleId);
}
