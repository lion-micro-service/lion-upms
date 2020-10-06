package com.lion.upms.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import springfox.documentation.annotations.ApiIgnore;

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
        ,indexes = {@Index(columnList = "name"),@Index(columnList = "email"),@Index(columnList = "create_date_time") })
@DynamicUpdate
@DynamicInsert
@Data
@JsonIgnoreProperties(ignoreUnknown = true,value = {"password","createDateTime","updateDateTime","createUserId","updateUserId"})
@ApiModel(value = "用户")
public class User extends BaseEntity {


    private static final long serialVersionUID = -98097429L;

    @ApiModelProperty(name = "用户登陆账号",notes = "用户登陆账号")
    @Column(name = "username", unique = true, nullable = false, updatable = false, columnDefinition = "varchar(30) comment '用户登陆账号'")
    @NotBlank(message = "用户登陆账号不能为空", groups = {Validator.Insert.class})
    @Length(min = 3, max = 30, message = "账号为{min}-{max}个字符", groups = {Validator.Insert.class})
    @Pattern(regexp = "[A-Za-z0-9\\-]{3,30}", message = "账号只能是3-30个(英文/数字)字符", groups = {Validator.Insert.class})
    private String username;

    @Setter(AccessLevel.PUBLIC)
    @ApiModelProperty(name = "密码（md5密码)）",notes = "密码（md5密码)")
    @Column(name = "password", nullable = false, columnDefinition = "varchar(255) comment '密码'")
    @NotBlank(message = "密码不能为空", groups = {Validator.Insert.class})
    @Pattern(regexp = "[a-zA-Z0-9]{32}", message = "请输入正确的密码(32的MD5密文)", groups = {Validator.Insert.class})
    private String password;

    @ApiModelProperty(name = "姓名",notes = "姓名")
    @Column(name = "name", columnDefinition = " varchar(30) comment '姓名' ")
    @Length(min = 0, max = 30, message = "姓名不能超过{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String name;

    @ApiModelProperty(name = "邮箱",notes = "邮箱")
    @Column(name = "email", columnDefinition = " varchar(30) comment '邮箱地址' ")
    @Email(message = "请输入正确的邮箱地址", groups = {Validator.Insert.class, Validator.Update.class})
    @Length(min = 0, max = 30, message = "邮箱地址不能超过{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String email;

    @ApiModelProperty(name = "年龄",notes = "年龄")
    @Column(name = "age", columnDefinition = " int(3) comment '年龄' ")
    @Min(value = 1 ,message = "年龄不能小于{value}岁", groups = {Validator.Insert.class, Validator.Update.class})
    @Max(value = 200 ,message = "年龄不能大于{value}岁", groups = {Validator.Insert.class, Validator.Update.class})
    private Integer age;

    @ApiModelProperty(name = "出生日期",notes = "出生日期")
    @Column(name = "birthday", columnDefinition = " date comment '出生日期' ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "出生日期不能大于/等于当前日期", groups = {Validator.Insert.class, Validator.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty(name = "头像（文件id）",notes = "头像（文件id）")
    @Column(name = "head_portrait", columnDefinition = " BIGINT(18) comment '头像（文件id）' ")
    private Long headPortrait;

    @ApiModelProperty(hidden = true)
    @Getter(AccessLevel.NONE)
    @Column(name = "is_account_non_expired", nullable = false, columnDefinition = " bit(1) default b'1' comment '账号是否未过期'")
    private Boolean isAccountNonExpired;

    @ApiModelProperty(hidden = true)
    @Getter(AccessLevel.NONE)
    @Column(name = "is_account_non_locked", nullable = false,  columnDefinition = " bit(1) default b'1' comment '账号是否未锁定'")
    private Boolean isAccountNonLocked;

    @ApiModelProperty(hidden = true)
    @Getter(AccessLevel.NONE)
    @Column(name = "is_credentials_non_expired", nullable = false,  columnDefinition = " bit(1) default b'1' comment '账号凭证是否未过期'")
    private Boolean isCredentialsNonExpired;

    @ApiModelProperty(hidden = true)
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
