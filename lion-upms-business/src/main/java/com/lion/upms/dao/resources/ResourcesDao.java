package com.lion.upms.dao.resources;

import com.lion.core.common.enums.State;
import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.resources.enums.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author mr.liu
 * @title: ResourcesDao
 * @description: 资源dao
 * @date 2020/8/15下午5:31
 */
public interface ResourcesDao extends BaseDao<Resources> ,ResourcesDaoEx {

    /**
     * 查找所有根结点资源
     * @param parentId
     * @param state
     * @param scope
     * @return
     */
    public List<Resources> findByParentIdAndStateAndScopeOrderBySort(Long parentId,State state, Scope scope);

    /**
     * 查找所有根结点资源
     * @param parentId
     * @param state
     * @param scope
     * @param resourcesId
     * @return
     */
    public List<Resources> findByParentIdAndStateAndScopeAndIdInOrderBySort(Long parentId,State state, Scope scope,List resourcesId);

    /**
     * 根据父节点ID查找子资源
     * @param parentId
     * @param state
     * @return
     */
    public List<Resources> findByParentIdAndStateOrderBySort(Long parentId, State state);

    /**
     * 根据父节点ID查找子资源
     * @param parentId
     * @param state
     * @return
     */
    public List<Resources> findByParentIdAndStateAndIdInOrderBySort(Long parentId, State state,List resourcesId);

    /**
     * 根据编码查询资源
     * @param code
     * @return
     */
    public Optional<Resources> findFirstByCode(String code);

    /**
     * 根据名称和父节点ID查询资源
     * @param name
     * @param parentId
     * @return
     */
    public Optional<Resources> findFirstByNameAndParentId(String name,Long parentId);

    /**
     * 根据名称查询资源
     * @param name
     * @return
     */
    public Optional<Resources> findFirstByName(String name);


    /**
     * 根据url查询资源
     * @param url
     * @return
     */
    public Optional<Resources> findFirstByUrl(String url);

    /**
     * 根据id查询父节点
     * @param id
     * @return
     */
    @Query("select r from Resources r where r.id = (select r1.parentId from Resources r1 where r1.id =:id)")
    public List<Resources> findParentResourcesById(@Param("id") Long id, Pageable pageable);

    /**
     * 根据用户ID查询所关联的角色所有的资源
     * @param userId
     * @return
     */
    @Query( "select r from Resources r join RoleResources rr on r.id = rr.resourcesId join RoleUser ru on rr.roleId = ru.roleId where ru.userId = :userId")
    public List<Resources> findAllResources(@Param("userId") Long userId);

    /**
     * 根据id查询某个type的资源
     * @param type
     * @param id
     * @return
     */
    public List<Resources> findByTypeAndIdIn(Type type,List<Long> id);


}
