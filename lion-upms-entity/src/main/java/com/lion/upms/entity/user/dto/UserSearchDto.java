package com.lion.upms.entity.user.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @description: 用户查询数据模型
 * @author: mr.liu
 * @create: 2020-09-29 15:35
 **/
@Data
public class UserSearchDto {

    /**
     * 登陆账号
     */
    private String username;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 生日
     */
    private LocalDate birthday;

    /**
     * 部门Id
     */
    private List<Long> departmentId;

    /**
     * 角色ID
     */
    private Long roleId;
}
