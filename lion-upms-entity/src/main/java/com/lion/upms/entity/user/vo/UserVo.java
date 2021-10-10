package com.lion.upms.entity.user.vo;

import com.lion.core.persistence.entity.BaseEntity;
import com.lion.upms.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

/**
 * @description: 用户详情vo
 * @author: mr.liu
 * @create: 2020-10-09 09:47
 **/
@Data
@Schema(description="用户列表VO")
public class UserVo extends User {

    /**
     * 头像
     */
    @Schema(description ="用户头像")
    private String headPortraitUrl;
}

