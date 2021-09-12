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
@Table(name = "t_user"
        ,indexes = {@Index(columnList = "name"),@Index(columnList = "email"),@Index(columnList = "create_date_time") })

@DynamicInsert
@Data
@JsonIgnoreProperties(ignoreUnknown = true,value = {"password","createDateTime","updateDateTime","createUserId","updateUserId"})
@ApiModel(description = "用户")
public class User extends BaseEntity {


    private static final long serialVersionUID = -98097429L;

    @ApiModelProperty(notes = "用户登陆账号")
    @Column(name = "username", unique = true, nullable = false, updatable = false)
    @NotBlank(message = "用户登陆账号不能为空", groups = {Validator.Insert.class})
    @Length(min = 3, max = 30, message = "账号为{min}-{max}个字符", groups = {Validator.Insert.class})
    @Pattern(regexp = "[A-Za-z0-9\\-]{3,30}", message = "账号只能是3-30个(英文/数字)字符", groups = {Validator.Insert.class})
    private String username;

    @Setter(AccessLevel.PUBLIC)
    @ApiModelProperty(notes = "密码（md5密码)")
    @Column(name = "password", nullable = false)
    @NotBlank(message = "密码不能为空", groups = {Validator.Insert.class})
    @Pattern(regexp = "[a-zA-Z0-9]{32}", message = "请输入正确的密码(32的MD5密文)", groups = {Validator.Insert.class})
    private String password;

    @ApiModelProperty(notes = "姓名")
    @Column(name = "name")
    @Length(min = 0, max = 30, message = "姓名不能超过{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String name;

    @ApiModelProperty(notes = "邮箱")
    @Column(name = "email")
    @Email(message = "请输入正确的邮箱地址", groups = {Validator.Insert.class, Validator.Update.class})
    @Length(min = 0, max = 30, message = "邮箱地址不能超过{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String email;

    @ApiModelProperty(notes = "年龄")
    @Column(name = "age")
    @Min(value = 1 ,message = "年龄不能小于{value}岁", groups = {Validator.Insert.class, Validator.Update.class})
    @Max(value = 200 ,message = "年龄不能大于{value}岁", groups = {Validator.Insert.class, Validator.Update.class})
    private Integer age;

    @ApiModelProperty(notes = "出生日期")
    @Column(name = "birthday")
    @Past(message = "出生日期不能大于/等于当前日期", groups = {Validator.Insert.class, Validator.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @ApiModelProperty(notes = "头像（文件id）")
    @Column(name = "head_portrait")
    private Long headPortrait;

    @ApiModelProperty(hidden = true,notes = "账号是否未过期")
    @Column(name = "is_account_non_expired", nullable = false)
    private Boolean isAccountNonExpired = false;

    @ApiModelProperty(hidden = true,notes = "账号是否未锁定")
    @Column(name = "is_account_non_locked", nullable = false)
    private Boolean isAccountNonLocked = false;

    @ApiModelProperty(hidden = true,notes = "账号凭证是否未过期")
    @Column(name = "is_credentials_non_expired", nullable = false)
    private Boolean isCredentialsNonExpired = false;

    @ApiModelProperty(hidden = true,notes = "账号是否可用")
    @Column(name = "is_nabled", nullable = false)
    private Boolean isEnabled =true;


}
