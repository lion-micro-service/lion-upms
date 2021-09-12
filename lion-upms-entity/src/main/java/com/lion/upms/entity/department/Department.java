package com.lion.upms.entity.department;

import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
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
 * @title: Department
 * @description: 部门表
 * @date 2020/8/14下午2:05
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_department",indexes = {@Index(columnList = "parent_id"),@Index(columnList = "name")})

@DynamicInsert
@Data
@ApiModel(description = "部门")
public class Department extends BaseEntity {

    private static final long serialVersionUID = -7780210746115490265L;

    @ApiModelProperty(value = "父节点ID")
    @Column(name = "parent_id",nullable = false)
    @NotNull(message = "父节点ID不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    private Long parentId;

    @ApiModelProperty(value = "部门名称")
    @Column(name = "name",nullable = false)
    @NotBlank(message = "部门名称不能为空",groups = {Validator.Update.class, Validator.Insert.class})
    @Length(message = "部门名称为{min}-{max}个字符",max = 30,min = 3,groups = {Validator.Update.class, Validator.Insert.class})
    @Pattern(regexp = "^[\\u4E00-\\u9FA5\\w+]+$",message = "部门名称不能包含特殊字符",groups = {Validator.Update.class, Validator.Insert.class})
    private String name;

    @ApiModelProperty(value = "备注")
    @Column(name = "remark")
    private String remark;
}
