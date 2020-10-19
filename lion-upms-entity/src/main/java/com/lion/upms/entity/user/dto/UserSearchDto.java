package com.lion.upms.entity.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @description: 用户查询数据模型
 * @author: mr.liu
 * @create: 2020-09-29 15:35
 **/
@Data
@ApiModel(description = "用户列表查询参数")
public class UserSearchDto {

    /**
     * 登陆账号
     */
    @ApiModelProperty(value = "登陆用户名")
    private String username;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名")
    private String name;

    /**
     * 年龄
     */
    @ApiModelProperty(value = "年龄")
    private Integer age;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日(1986-08-06)")
    private LocalDate birthday;

    /**
     * 部门Id
     */
    @ApiModelProperty(value = "部门Id")
    private List<Long> departmentId;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
}
