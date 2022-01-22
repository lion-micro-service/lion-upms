package com.lion.upms.service.role;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.role.Role;
import com.lion.upms.entity.role.vo.RoleResourcesTreeVo;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

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
    public Optional<Role> findByName(String name);

    /**
     * 根据编码查询角色
     * @param code
     * @return
     */
    public Optional<Role> findByCode(String code);

    /**
     * 根据用户id查询用户的角色
     * @param userId
     * @return
     */
    public List findByUserId(Long userId);

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

    /**
     * 根据作用于获取资源（角色配置权限专用）
     * @param scope
     * @return
     */
    public List<RoleResourcesTreeVo> roleResources(Scope scope);
}
