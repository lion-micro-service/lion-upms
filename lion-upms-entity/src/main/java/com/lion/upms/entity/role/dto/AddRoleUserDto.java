package com.lion.upms.entity.role.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 添加角色用户模型
 * @author: mr.liu
 * @create: 2020-09-27 20:36
 **/
@Data
@Schema(description = "添加角色用户模型")
public class AddRoleUserDto {

    @NotNull(message = "角色id不能为空")
    @Schema(description = "角色id不能为空",required = true)
    private Long roleId;

    /**
     * 重新选择后的用户
     */
    @Schema(description = "重新选择后的用户",required = true)
    private List<Long> newUserId;

    /**
     * 重新选择前的用户（用户删除数据在保存新的用户-newUserId）
     */
    @Schema(description = "重新选择前的用户")
    private List<Long> oldUserId;

}
