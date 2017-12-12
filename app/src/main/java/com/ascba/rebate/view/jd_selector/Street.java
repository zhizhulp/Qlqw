package com.ascba.rebate.view.jd_selector;

import com.alibaba.fastjson.annotation.JSONField;

public class Street {
    @JSONField(name = "region_id")
    private int id;
    private String name;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
