package com.lion.upms.entity.role;

import com.lion.core.common.enums.State;
import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.upms.entity.common.enums.Scope;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @description: 角色表
 * @author: Mr.Liu
 * @create: 2020-02-09 16:05
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_role",indexes = {@Index(columnList = "name")})

@DynamicInsert
@Data
@Schema(description = "角色")
public class Role extends BaseEntity {

    private static final long serialVersionUID = -6912212114524590397L;

    @Schema(description = "角色编码")
    @Column(name = "code",unique = true,nullable = false)
    @NotBlank(message = "角色编码不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Size(message = "角色编码为{min}-{max}个字符",max = 30,min = 3,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^\\w+$",message = "角色编码只能是数字、英文字母或者下划线组成的字符串",groups = {Validator.Update.class, Validator.Insert.class})
    private String code;

    @Schema(description = "角色名称")
    @Column(name = "name",unique = false)
    @NotBlank(message = "角色名称不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Size(message = "角色名称为{min}-{max}个字符",max = 30,min = 3,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^[\\u4E00-\\u9FA5\\w+]+$",message = "角色名称不能包含特殊字符",groups = {Validator.Update.class, Validator.Insert.class})
    private String name;

    @Schema(description = "作用域（0:app,1:后台,2:前端,3:微信）")
    @Column(name = "scope",nullable = false)
    @Convert(converter = Scope.ScopeConverter.class)
    private Scope scope;

    @Schema(description = "是否默认角色（0：否，1：是）默认角色不能删除")
    @Column(name = "is_default" ,nullable = false)
    private Boolean isDefault;

    @Schema(description = "备注")
    @Column(name = "remark")
    private String remark;

    @Schema(description = "状态（0：禁用，1：启用）")
    @Column(name = "state",nullable = false)
    @Convert(converter = State.StateConverter.class)
    private State state;
}
