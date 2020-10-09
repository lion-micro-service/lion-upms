package com.lion.upms.entity.user.vo;

import com.lion.core.persistence.entity.BaseEntity;
import com.lion.upms.entity.user.User;
import lombok.Data;

/**
 * @description: 用户详情vo
 * @author: mr.liu
 * @create: 2020-10-09 09:47
 **/
@Data
public class UserVo extends User {

    /**
     * 头像
     */
    private BaseEntity headPortraitVo;
}

