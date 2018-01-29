package com.ascba.rebate.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by 李平 on 2017/12/4 10:24
 * Describe:商家选择类型
 */

public class MctType {
    @JSONField(name = "primary_class_key")
    private int id;
    @JSONField(name = "name")
    private String text;


    public MctType() {
    }

    public MctType(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
