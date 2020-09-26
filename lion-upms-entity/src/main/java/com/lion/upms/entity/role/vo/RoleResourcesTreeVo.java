package com.lion.upms.entity.role.vo;

import lombok.Data;

import java.util.List;

/**
 * @description: 角色权限树形结构
 * @author: mr.liu
 * @create: 2020-09-25 20:35
 **/
@Data
public class RoleResourcesTreeVo {

    /**
     * 标题
     */
    private String title;

    /**
     * key
     */
    private Long key;

    /**
     * 子节点
     */
    private List<RoleResourcesTreeVo> children;
}

