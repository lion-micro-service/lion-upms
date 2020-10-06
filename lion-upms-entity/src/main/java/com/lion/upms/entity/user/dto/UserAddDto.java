package com.lion.upms.entity.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.annotation.swagger.LionApiModelPropertyIgnore;
import com.lion.upms.entity.user.User;
import lombok.Data;

/**
 * @author mr.liu
 * @Description: 用户新增模型
 * @date 2020/8/29下午3:28
 */
@Data
@JsonIgnoreProperties(value={"isAccountNonExpired","isAccountNonLocked","isCredentialsNonExpired","isEnabled"})
@LionApiModelPropertyIgnore(propertyIgnore = {"id","createDateTime","createUserId","updateDateTime","updateUserId","version"})
public class UserAddDto extends User {
}
