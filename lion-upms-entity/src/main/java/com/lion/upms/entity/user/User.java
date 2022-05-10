package com.lion.upms.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.time.LocalDate;

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
@Schema(description = "用户")
public class User extends BaseEntity {


    private static final long serialVersionUID = -98097429L;

    @Schema(description = "用户登陆账号")
    @Column(name = "username", unique = true, nullable = false, updatable = false)
    @NotBlank(message = "用户登陆账号不能为空", groups = {Validator.Insert.class})
    @Length(min = 3, max = 30, message = "账号为{min}-{max}个字符", groups = {Validator.Insert.class})
    @Pattern(regexp = "[A-Za-z0-9\\-]{3,30}", message = "账号只能是3-30个(英文/数字)字符", groups = {Validator.Insert.class})
    private String username;

    @Setter(AccessLevel.PUBLIC)
    @Schema(description = "密码（md5密码)")
    @Column(name = "password", nullable = false)
    @NotBlank(message = "密码不能为空", groups = {Validator.Insert.class})
    @Pattern(regexp = "[a-zA-Z0-9]{32}", message = "请输入正确的密码(32的MD5密文)", groups = {Validator.Insert.class})
    private String password;

    @Schema(description = "姓名")
    @Column(name = "name")
    @Length(min = 0, max = 30, message = "姓名不能超过{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String name;

    @Schema(description = "邮箱")
    @Column(name = "email")
    @Email(message = "请输入正确的邮箱地址", groups = {Validator.Insert.class, Validator.Update.class})
    @Length(min = 0, max = 30, message = "邮箱地址不能超过{max}个字符", groups = {Validator.Insert.class, Validator.Update.class})
    private String email;

    @Schema(description = "年龄")
    @Column(name = "age")
    @Min(value = 1 ,message = "年龄不能小于{value}岁", groups = {Validator.Insert.class, Validator.Update.class})
    @Max(value = 200 ,message = "年龄不能大于{value}岁", groups = {Validator.Insert.class, Validator.Update.class})
    private Integer age;

    @Schema(description = "出生日期")
    @Column(name = "birthday")
    @Past(message = "出生日期不能大于/等于当前日期", groups = {Validator.Insert.class, Validator.Update.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Schema(description = "头像（文件id）")
    @Column(name = "head_portrait")
    private Long headPortrait;

    @Schema(description = "创建人电话")
    private String phoneNumber;

    @Schema(hidden = true,description = "账号是否未过期")
    @Column(name = "is_account_non_expired", nullable = false)
    private Boolean isAccountNonExpired = false;

    @Schema(hidden = true,description = "账号是否未锁定")
    @Column(name = "is_account_non_locked", nullable = false)
    private Boolean isAccountNonLocked = false;

    @Schema(hidden = true,description = "账号凭证是否未过期")
    @Column(name = "is_credentials_non_expired", nullable = false)
    private Boolean isCredentialsNonExpired = false;

    @Schema(hidden = true,description = "账号是否可用")
    @Column(name = "is_nabled", nullable = false)
    private Boolean isEnabled =true;


}
