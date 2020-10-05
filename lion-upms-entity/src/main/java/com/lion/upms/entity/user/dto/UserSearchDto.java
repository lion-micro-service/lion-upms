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
@ApiModel(description = "查询参数")
public class UserSearchDto {

    /**
     * 登陆账号
     */
    @ApiModelProperty(name = "登陆用户名")
    private String username;

    /**
     * 用户姓名
     */
    @ApiModelProperty(name = "用户姓名")
    private String name;

    /**
     * 年龄
     */
    @ApiModelProperty(name = "年龄")
    private Integer age;

    /**
     * 邮箱
     */
    @ApiModelProperty(name = "邮箱")
    private String email;

    /**
     * 生日
     */
    @ApiModelProperty(name = "生日(1986-08-06)")
    private LocalDate birthday;

    /**
     * 部门Id
     */
    @ApiModelProperty(name = "部门Id")
    private List<Long> departmentId;

    /**
     * 角色ID
     */
    @ApiModelProperty(name = "角色ID")
    private Long roleId;
}
