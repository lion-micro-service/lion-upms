package com.lion.upms.entity.resources;

import com.lion.core.common.enums.State;
import com.lion.core.common.enums.StateConverter;
import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.upms.entity.resources.enums.Scope;
import com.lion.upms.entity.resources.enums.ScopeConverter;
import com.lion.upms.entity.resources.enums.Type;
import com.lion.upms.entity.resources.enums.TypeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author mr.liu
 * @title: Menu
 * @description: 目录菜单
 * @date 2020/8/13下午2:43
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_upms_resources",uniqueConstraints ={@UniqueConstraint(columnNames = "code")},indexes = {@Index(columnList = "name"),@Index(columnList = "parent_id")})
@DynamicUpdate
@DynamicInsert
@Data
public class Resources extends BaseEntity {

    private static final long serialVersionUID = 4121945476090393825L;

    @Column(name = "parent_id",nullable = false,columnDefinition = " BIGINT(20) default 0 comment '父节点ID' ")
    @NotNull(message = "父节点ID不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    private Long parentId;

    @Column(name = "code",unique = true,nullable = false,columnDefinition = " varchar(30) comment '资源编码' ")
    @NotBlank(message = "资源编码不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Length(message = "资源编码为{min}-{max}个字符",max = 30,min = 3,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^\\w+$",message = "资源编码只能是数字、英文字母或者下划线组成的字符串",groups = {Validator.Update.class, Validator.Insert.class})
    private String code;

    @Column(name = "name",nullable = false,columnDefinition = " varchar(30) comment '资源名称' ")
    @NotBlank(message = "资源名称不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Length(message = "资源名称为{min}-{max}个字符",max = 30,min = 3,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^[\\u4E00-\\u9FA5\\w+]+$",message = "资源名称不能包含特殊字符",groups = {Validator.Update.class, Validator.Insert.class})
    private String name;

    @Column(name = "scope",nullable = false,columnDefinition = " int default 1 comment '作用域（0:app,1:后台,2:前端,3:微信）'")
    @Convert(converter = ScopeConverter.class)
    private Scope scope;

    @Column(name = "type",nullable = false,columnDefinition = " int default 0 comment '作用域（0:目录,1:菜单,2:功能）'")
    @Convert(converter = TypeConverter.class)
    private Type type;

    @Column(name = "state",nullable = false,columnDefinition = " bit(1) default b'1' comment '状态（0：禁用，1：启用）'")
    @Convert(converter = StateConverter.class)
    private State state;

    @Column(name = "url",columnDefinition = " varchar(250) comment 'url' ")
    private String url;

    @Column(name = "remark",columnDefinition = " varchar(500) comment '备注' ")
    private String remark;
}
