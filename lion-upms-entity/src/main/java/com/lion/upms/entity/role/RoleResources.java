package com.lion.upms.entity.role;

import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author mr.liu
 * @title: RoleResources
 * @description: 角色资源关联表
 * @date 2020/8/13下午4:25
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_upms_role_resources",indexes = {@Index(columnList = "resources_id"),@Index(columnList = "role_id")})
@DynamicUpdate
@DynamicInsert
@Data
@ApiModel(description = "角色资源关联表")
public class RoleResources extends BaseEntity {

    private static final long serialVersionUID = -772859768017737144L;

    @ApiModelProperty(value = "角色ID")
    @Column(name = "role_id", nullable = false)
    @NotNull(message = "角色ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long roleId;

    @ApiModelProperty(value = "资源ID")
    @Column(name = "resources_id", nullable = false)
    @NotNull(message = "资源ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long resourcesId;

    @ApiModelProperty(value = "是否选中（针对前端显示用）")
    @Column(name = "is_checked", nullable = false)
    private Boolean isChecked;
}
