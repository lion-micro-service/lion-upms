package com.lion.upms.entity.role;

import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.upms.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-02-09 17:07
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_role_user",indexes = {@Index(columnList = "role_id"),@Index(columnList = "user_id")})

@DynamicInsert
@Data
@Schema(description = "角色资源关联表")
public class RoleUser extends BaseEntity {

    private static final long serialVersionUID = 1834558062801001562L;

    @Schema(description = "角色ID")
    @Column(name = "role_id",nullable = false)
    @NotNull(message = "角色ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long roleId;

    @Schema(description = "用户ID")
    @Column(name = "user_id", nullable = false)
    @NotNull(message = "用户ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long userId;

}
