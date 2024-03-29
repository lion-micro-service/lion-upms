package com.lion.upms.service.resources;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.resources.enums.Type;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;
import com.lion.upms.entity.role.RoleResources;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author mr.liu
 * @title: resources
 * @description: 资源service
 * @date 2020/8/15下午5:24
 */
@Validated
public interface ResourcesService extends BaseService<Resources> {

    /**
     * 资源树形结构
     * @param scope
     * @return
     */
    public List<ResourcesTreeVo> listTree(Scope scope);

    /**
     * 资源树形结构
     * @param scope
     * @param resourcesId
     * @return
     */
    public List<ResourcesTreeVo> listTree(Scope scope, List<Long> resourcesId);



    /**
     * 根据编码查询资源
     * @param code
     * @return
     */
    public java.util.Optional<Resources> findByCode(String code);

    /**
     * 根据名称查询资源
     * @param name
     * @return
     */
    public java.util.Optional<Resources> findByName(String name);

    /**
     * 根据url查询资源
     * @param url
     * @return
     */
    public Optional<Resources> findByUrl(String url);

    /**
     * 检查code是否存在
     * @param code
     * @param id
     * @return
     */
    public Boolean checkCodeIsExist(String code, Long id);

    /**
     * 检查名称是否存在
     * @param name
     * @param id
     * @param parentId
     * @return
     */
    public Boolean checkNameIsExist(String name, Long id,@NotNull(message = "父节点id不能为空") Long parentId);

    /**
     * 检查url是否存在
     * @param url
     * @param id
     * @return
     */
    public Boolean checkUrlIsExist(String url, Long id);


    /**
     * 检查code是否存在
     * @param code
     * @return
     */
    public Boolean checkCodeIsExist(String code);

    /**
     * 检查名称是否存在
     * @param name
     * @param parentId
     * @return
     */
    public Boolean checkNameIsExist(String name, Long parentId);

    /**
     * 检查url是否存在
     * @param url
     * @return
     */
    public Boolean checkUrlIsExist(String url);

    /**
     * 检查资源是否存在
     * @param resources
     */
    public void checkIsExist(Resources resources);

    /**
     * 根据id删除资源
     * @param id
     * @return
     */
    public Boolean delete(Long id);

    /**
     * 根据id获取所有的父节点
     * @param id
     * @return
     */
    public List<Resources> findAllParentResources(Long id);

    public List<Resources> findAllChilderResources(Long id);

    /**
     * 根据用户关联的角色查询所有资源ID
     * @param userId
     * @return
     */
    public List<Long> findAllResourcesId(Long userId);

    /**
     * 根据用户关联的角色查询所有资源
     * @param userId
     * @return
     */
    public List<Resources> findAllResources(Long userId);

    /**
     * 根据id查询某个type的资源
     * @param type
     * @param id
     * @return
     */
    public List<Resources> findByTypeAndIdIn(Type type, List<Long> id);
}
