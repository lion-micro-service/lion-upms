package com.lion.upms.entity.role.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 添加角色与资源数据模型
 * @author: mr.liu
 * @create: 2020-09-26 16:07
 **/
@Data
@ApiModel(description = "添加角色与资源数据模型")
public class AddRoleResourcesdDto {

    @ApiModelProperty(value = "角色id不能为空",required = true)
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @ApiModelProperty(value = "资源id")
    List<Long> resourcesId;
}
