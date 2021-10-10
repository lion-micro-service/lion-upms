package com.lion.upms.entity.role.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 添加角色与资源数据模型
 * @author: mr.liu
 * @create: 2020-09-26 16:07
 **/
@Data
@Schema(description = "添加角色与资源数据模型")
public class AddRoleResourcesdDto {

    @Schema(description = "角色id不能为空",required = true)
    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @Schema(description = "资源id")
    List<Long> resourcesId;
}
