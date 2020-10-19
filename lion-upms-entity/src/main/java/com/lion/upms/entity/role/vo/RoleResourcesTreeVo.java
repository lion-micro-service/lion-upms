package com.lion.upms.entity.role.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 角色权限树形结构
 * @author: mr.liu
 * @create: 2020-09-25 20:35
 **/
@Data
@ApiModel(description = "角色权限树形结构")
public class RoleResourcesTreeVo {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * key
     */
    @ApiModelProperty(value = "key")
    private Long key;

    /**
     * 子节点
     */
    @ApiModelProperty(value = "子节点")
    private List<RoleResourcesTreeVo> children;
}

