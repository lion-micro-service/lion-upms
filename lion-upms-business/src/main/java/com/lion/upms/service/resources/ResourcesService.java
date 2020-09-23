package com.lion.upms.service.resources;

import com.lion.core.service.BaseService;
import com.lion.upms.entity.resources.Resources;
import com.lion.upms.entity.resources.enums.Scope;
import com.lion.upms.entity.resources.vo.ResourcesTreeVo;

import javax.persistence.Id;
import java.util.List;

/**
 * @author mr.liu
 * @title: resources
 * @description: 资源service
 * @date 2020/8/15下午5:24
 */
public interface ResourcesService extends BaseService<Resources> {

    /**
     * 资源树形结构
     * @param scope
     * @return
     */
    public List<ResourcesTreeVo> listTree(Scope scope);

    /**
     * 根据编码查询资源
     * @param code
     * @return
     */
    public Resources findByCode(String code);

    /**
     * 根据名称查询资源
     * @param name
     * @return
     */
    public Resources findByName(String name);

    /**
     * 根据url查询资源
     * @param url
     * @return
     */
    public Resources findByUrl(String url);

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
     * @return
     */
    public Boolean checkNameIsExist(String name, Long id);

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
     * @return
     */
    public Boolean checkNameIsExist(String name);

    /**
     * 检查url是否存在
     * @param url
     * @return
     */
    public Boolean checkUrlIsExist(String url);
}
