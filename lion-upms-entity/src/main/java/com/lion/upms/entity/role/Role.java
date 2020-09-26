package com.lion.upms.entity.role;

import com.lion.core.common.enums.State;
import com.lion.core.common.enums.StateConverter;
import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.common.enums.ScopeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.ConvertGroup;

/**
 * @description: 角色表
 * @author: Mr.Liu
 * @create: 2020-02-09 16:05
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_upms_role",uniqueConstraints ={@UniqueConstraint(columnNames = "code")},indexes = {@Index(columnList = "name")})
@DynamicUpdate
@DynamicInsert
@Data
public class Role extends BaseEntity {

    private static final long serialVersionUID = -6912212114524590397L;

    @Column(name = "code",unique = true,nullable = false,columnDefinition = " varchar(30) comment '角色编码' ")
    @NotBlank(message = "角色编码不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Size(message = "角色编码为{min}-{max}个字符",max = 30,min = 3,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^\\w+$",message = "角色编码只能是数字、英文字母或者下划线组成的字符串",groups = {Validator.Update.class, Validator.Insert.class})
    private String code;

    @Column(name = "name",unique = false,columnDefinition = " varchar(30) comment '角色名称' ")
    @NotBlank(message = "角色名称不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Size(message = "角色名称为{min}-{max}个字符",max = 30,min = 3,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^[\\u4E00-\\u9FA5\\w+]+$",message = "角色名称不能包含特殊字符",groups = {Validator.Update.class, Validator.Insert.class})
    private String name;

    @Column(name = "scope",nullable = false,columnDefinition = " int default 1 comment '作用域（0:app,1:后台,2:前端,3:微信）'")
    @Convert(converter = ScopeConverter.class)
    private Scope scope;

    @Column(name = "is_default" ,nullable = false,columnDefinition = " bit(1) default b'0' comment '是否默认角色（0：否，1：是）默认角色不能删除'")
    private Boolean isDefault;

    @Column(name = "remark",columnDefinition = " varchar(500) comment '备注' ")
    private String remark;

    @Column(name = "state",nullable = false,columnDefinition = " bit(1) default b'1' comment '状态（0：禁用，1：启用）'")
    @Convert(converter = StateConverter.class)
    private State state;
}
