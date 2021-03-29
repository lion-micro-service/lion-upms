package com.lion.upms.entity.resources;

import com.lion.core.common.enums.State;
import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.upms.entity.common.enums.Scope;
import com.lion.upms.entity.resources.enums.Type;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author mr.liu
 * @title: Menu
 * @description: 目录菜单
 * @date 2020/8/13下午2:43
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_resources",indexes = {@Index(columnList = "name"),@Index(columnList = "parent_id")})
@DynamicUpdate
@DynamicInsert
@Data
@ApiModel(description = "用户更新")
public class Resources extends BaseEntity {

    private static final long serialVersionUID = 4121945476090393825L;

    @ApiModelProperty(value = "父节点ID不能为空")
    @Column(name = "parent_id",nullable = false)
    @NotNull(message = "父节点ID不能为空",groups = {Validator.Insert.class})
    private Long parentId;

    @ApiModelProperty(value = "资源编码")
    @Column(name = "code",unique = true,nullable = false)
    @NotBlank(message = "资源编码不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Length(message = "资源编码为{min}-{max}个字符",max = 100,min = 1,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^\\w+$",message = "资源编码只能是数字、英文字母或者下划线组成的字符串",groups = {Validator.Update.class, Validator.Insert.class})
    private String code;

    @ApiModelProperty(value = "资源名称")
    @Column(name = "name",nullable = false)
    @NotBlank(message = "资源名称不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Length(message = "资源名称为{min}-{max}个字符",max = 30,min = 1,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^[\\u4E00-\\u9FA5\\w+]+$",message = "资源名称不能包含特殊字符",groups = {Validator.Update.class, Validator.Insert.class})
    private String name;

    @ApiModelProperty(value = "作用域（0:app,1:后台,2:前端,3:微信）")
    @Column(name = "scope",nullable = false)
    @Convert(converter = Scope.ScopeConverter.class)
    private Scope scope;

    @ApiModelProperty(value = "类型（0:目录,1:菜单,2:功能）")
    @Column(name = "type",nullable = false)
    @Convert(converter = Type.TypeConverter.class)
    private Type type;

    @ApiModelProperty(value = "排序")
    @Column(name = "sort",nullable = false)
    private Integer sort;

    @ApiModelProperty(value = "状态（0：禁用，1：启用）")
    @Column(name = "state",nullable = false)
    @Convert(converter = State.StateConverter.class)
    private State state;

    @ApiModelProperty(value = "是否默认资源（0：否，1：是）默认资源不能删除")
    @Column(name = "is_default" ,nullable = false)
    private Boolean isDefault;

    @ApiModelProperty(value = "url")
    @Column(name = "url")
    private String url;

    @ApiModelProperty(value = "备注")
    @Column(name = "remark")
    private String remark;
}
