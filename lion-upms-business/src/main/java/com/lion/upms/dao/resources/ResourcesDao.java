package com.lion.upms.dao.resources;

import com.lion.core.common.enums.State;
import com.lion.core.persistence.curd.BaseDao;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.resources.enums.Scope;
import com.lion.upms.entity.resources.enums.Type;

import javax.validation.constraints.NotNull;
import java.util.List;

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
     * 根据父节点ID查找子资源
     * @param parentId
     * @param state
     * @return
     */
    public List<Resources> findByParentIdAndStateOrderBySort(Long parentId, State state);

    /**
     * 根据编码查询资源
     * @param code
     * @return
     */
    public Resources findFirstByCode(String code);

    /**
     * 根据名称查询资源
     * @param name
     * @return
     */
    public Resources findFirstByName(String name);

    /**
     * 根据url查询资源
     * @param url
     * @return
     */
    public Resources findFirstByUrl(String url);
}
