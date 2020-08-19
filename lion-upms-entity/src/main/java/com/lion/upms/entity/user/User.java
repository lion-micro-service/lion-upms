package com.lion.upms.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Date;

/**
 * @description: 用户
 * @author: Mr.Liu
 * @create: 2020-01-04 20:07
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_upms_user"
        ,uniqueConstraints = {@UniqueConstraint(columnNames = {"username"}),@UniqueConstraint(columnNames = {"email"})}
        ,indexes = {@Index(columnList = "name")})
@DynamicUpdate
@DynamicInsert
@Data
@JsonIgnoreProperties(ignoreUnknown = true,value = {"password","username","isDelete","createDateTime","updateDateTime","createUserId","updateUserId"})
public class User extends BaseEntity {


    private static final long serialVersionUID = -98097429L;

    @Column(name = "username", unique = true, nullable = false, updatable = false, columnDefinition = "varchar(30) comment '用户登陆账号'")
    @NotBlank(message = "用户登陆账号不能为空", groups = {Validator.Insert.class})
    @Length(min = 6, max = 30, message = "账号为{min}-{max}个字符", groups = {Validator.Insert.class})
    @Pattern(regexp = "[A-Za-z0-9\\-]{5,30}", message = "账号只能是英文/数字", groups = {Validator.Insert.class})
    private String username;

    @Column(name = "password", nullable = false, columnDefinition = "varchar(255) comment '密码'")
    @NotBlank(message = "密码不能为空", groups = {Validator.Insert.class, Validator.Update.class})
    @Pattern(regexp = "[a-zA-Z]\\w{0,32}$", message = "请输入正确的密码", groups = {Validator.Insert.class, Validator.Update.class})
    private String password;

    @Column(name = "name", columnDefinition = " varchar(30) comment '姓名' ")
    @Length(min = 0, max = 30, message = "姓名不能超过{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String name;

    @Column(name = "email", columnDefinition = " varchar(30) comment '邮箱地址' ")
    @Email(message = "请输入正确的邮箱地址", groups = {Validator.Insert.class, Validator.Update.class})
    @Length(min = 0, max = 30, message = "邮箱地址不能超过{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String email;

    @Column(name = "age", columnDefinition = " int(3) comment '年龄' ")
    @Min(value = 1 ,message = "年龄不能小于{value}岁", groups = {Validator.Insert.class, Validator.Update.class})
    @Max(value = 200 ,message = "年龄不能大于{value}岁", groups = {Validator.Insert.class, Validator.Update.class})
    private Integer age;

    @Column(name = "birthday", columnDefinition = " date comment '出生日期' ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "出生日期不能大于当前日期", groups = {Validator.Insert.class, Validator.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Getter(AccessLevel.NONE)
    @Column(name = "is_account_non_expired", nullable = false, columnDefinition = " bit(1) default b'1' comment '账号是否未过期'")
    private Boolean isAccountNonExpired;

    @Getter(AccessLevel.NONE)
    @Column(name = "is_account_non_locked", nullable = false,  columnDefinition = " bit(1) default b'1' comment '账号是否未锁定'")
    private Boolean isAccountNonLocked;

    @Getter(AccessLevel.NONE)
    @Column(name = "is_credentials_non_expired", nullable = false,  columnDefinition = " bit(1) default b'1' comment '账号凭证是否未过期'")
    private Boolean isCredentialsNonExpired;

    @Getter(AccessLevel.NONE)
    @Column(name = "is_nabled", nullable = false,  columnDefinition = " bit(1) default b'1' comment '账号是否可用'")
    private Boolean isEnabled;

    public Boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    public Boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    public Boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    public Boolean isEnabled() {
        return isEnabled;
    }

}
