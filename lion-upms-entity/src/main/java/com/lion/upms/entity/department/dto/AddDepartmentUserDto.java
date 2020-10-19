package com.lion.upms.entity.department.dto;

import com.lion.upms.entity.department.Department;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description: 添加部门关联用户模型
 * @author: mr.liu
 * @create: 2020-09-28 16:44
 **/
@Data
@ApiModel(description = "添加部门关联用户模型")
public class AddDepartmentUserDto {

    /**
     * 部门ID
     */
    @ApiModelProperty(value = "部门ID")
    @NotNull(message = "部门id不能为空")
    private Long departmentId;

    /**
     * 新选择的用户
     */
    @ApiModelProperty(value = "新选择的用户")
    private List<Long> newUserId;

    /**
     * 新选择的用户之前的数据，用于删除老数据后新增（newUserId）数据
     */
    @ApiModelProperty(value = "新选择的用户之前的数据")
    private List<Long> oldUserId;
}
