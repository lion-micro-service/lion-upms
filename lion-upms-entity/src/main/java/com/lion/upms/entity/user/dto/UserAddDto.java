package com.lion.upms.entity.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.upms.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author mr.liu
 * @Description: 用户新增模型
 * @date 2020/8/29下午3:28
 */
@Data
@JsonIgnoreProperties(value={"isAccountNonExpired","isAccountNonLocked","isCredentialsNonExpired","isEnabled","createDateTime","updateDateTime","createUserId","updateUserId","version"})
@Schema(description = "用户新增")
public class UserAddDto extends User {
}
