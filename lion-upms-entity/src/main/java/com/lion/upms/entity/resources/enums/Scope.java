package com.lion.upms.entity.resources.enums;

import com.lion.core.IEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.liu
 * @title: Scope
 * @description: 资源类型
 * @date 2020/8/14上午11:29
 */
public enum Scope implements IEnum {
    APP(0, "app"), CONSOLE(1, "后台"),FRONT(2,"前端"),WE_CHAT(3,"微信");

    private final int key;

    private final String desc;

    private Scope(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getDesc(){
        return desc;
    }

    @Override
    public Map<String, Object> jsonValue() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("desc", desc);
        map.put("name", getName());
        return map;
    }
}
