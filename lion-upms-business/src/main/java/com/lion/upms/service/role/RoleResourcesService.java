package com.lion.upms.service.role;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.role.RoleResources;
import com.lion.upms.entity.role.dto.AddRoleResourcesdDto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mr.liu
 * @title: RoleResourcesService
 * @description: 角色与资源关联Service
 * @date 2020/8/15下午5:04
 */
public interface RoleResourcesService extends BaseService<RoleResources> {

    /**
     * 保存角色权限（资源）
     * @param addRoleResourcesdDto
     */
    public void saveRoleResources(AddRoleResourcesdDto addRoleResourcesdDto);

    /**
     * 根据角色id获取所有权限（资源）
     * @param roleId
     * @return
     */
    public List<RoleResources> findAllRoleResources(Long roleId);

    /**
     * 根据角色ID删除角色与权限(资源)的关联
     * @param roleId
     */
    public void deleteByRoleId(Long roleId);

    /**
     * 根据资源ID删除角色与权限(资源)的关联
     * @param resourcesId
     */
    public void deleteByResourcesId(Long resourcesId);
}
