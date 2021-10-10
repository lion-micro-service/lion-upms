package com.lion.upms.entity.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @description: 用户查询数据模型
 * @author: mr.liu
 * @create: 2020-09-29 15:35
 **/
@Data
@Schema(description = "用户列表查询参数")
public class UserSearchDto {

    /**
     * 登陆账号
     */
    @Schema(description = "登陆用户名")
    private String username;

    /**
     * 用户姓名
     */
    @Schema(description = "用户姓名")
    private String name;

    /**
     * 年龄
     */
    @Schema(description = "年龄")
    private Integer age;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱")
    private String email;

    /**
     * 生日
     */
    @Schema(description = "生日(1986-08-06)")
    private LocalDate birthday;

    /**
     * 部门Id
     */
    @Schema(description = "部门Id")
    private List<Long> departmentId;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;
}
