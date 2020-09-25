package com.lion.upms.service.role;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.role.Role;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author mr.liu
 * @title: 角色Service
 * @description: 角色service
 * @date 2020/8/15下午5:02
 */
@Validated
public interface RoleService extends BaseService<Role> {

    /**
     * 根据名称查询角色
     * @param name
     * @return
     */
    public Role findByName(String name);

    /**
     * 根据编码查询角色
     * @param code
     * @return
     */
    public Role findByCode(String code);

    /**
     * 检查角色是否存在
     * @param role
     */
    public void checkIsExist(Role role);

    /**
     * 检查名称是否存在
     * @param name
     * @param id
     */
    public boolean checkNameIsExist(String name, Long id);

    /**
     * 检查编码是否存在
     * @param code
     * @param id
     */
    public boolean checkCodeIsExist(String code, Long id);

    /**
     * 检查名称是否存在
     * @param name
     */
    public boolean checkNameIsExist(String name);

    /**
     * 检查编码是否存在
     * @param code
     */
    public boolean checkCodeIsExist(String code);
}
