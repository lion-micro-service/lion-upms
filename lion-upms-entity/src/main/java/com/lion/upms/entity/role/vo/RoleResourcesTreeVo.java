package com.lion.upms.entity.role.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.List;

/**
 * @description: 角色权限树形结构
 * @author: mr.liu
 * @create: 2020-09-25 20:35
 **/
@Data
@Schema(description = "角色权限树形结构")
public class RoleResourcesTreeVo {

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * key
     */
    @Schema(description = "key")
    private Long key;

    /**
     * 子节点
     */
    @Schema(description = "子节点")
    private List<RoleResourcesTreeVo> children;
}

