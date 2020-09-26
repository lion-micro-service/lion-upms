package com.lion.upms.entity.role.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description:
 * @author: mr.liu
 * @create: 2020-09-26 16:07
 **/
@Data
public class AddRoleResourcesdDto {

    @NotNull(message = "角色id不能为空")
    private Long roleId;

    List<Long> resourcesId;
}
