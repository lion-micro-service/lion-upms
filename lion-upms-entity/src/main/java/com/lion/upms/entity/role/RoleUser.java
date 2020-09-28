package com.lion.upms.entity.role;

import com.lion.core.persistence.Validator;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.upms.entity.user.User;
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
@Table(name = "t_upms_role_user",indexes = {@Index(columnList = "role_id"),@Index(columnList = "user_id")})
@DynamicUpdate
@DynamicInsert
@Data
public class RoleUser extends BaseEntity {

    private static final long serialVersionUID = 1834558062801001562L;

    @Column(name = "role_id",columnDefinition = " BIGINT(18) comment '角色ID' ", nullable = false)
    @NotNull(message = "角色ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long roleId;

    @Column(name = "user_id",columnDefinition = " BIGINT(18) comment '用户ID' ", nullable = false)
    @NotNull(message = "用户ID不能为空", groups = {Validator.Insert.class,Validator.Update.class})
    private Long userId;

}
