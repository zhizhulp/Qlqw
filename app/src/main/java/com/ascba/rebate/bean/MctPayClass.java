package com.ascba.rebate.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class MctPayClass extends MctBasePay {
    @JSONField(name = "level_id")
    private int id;
    @JSONField(name = "level_title")
    private String title;
    @JSONField(name = "level_desc")
    private String content;
    @JSONField(name = "level_present_price")
    private String after;
    @JSONField(name = "level_original_price")
    private String before;
    @Override
    public int getItemType() {
        return ITEM_TYPE_CLASS;
    }

    public MctPayClass() {
    }

    public MctPayClass(String title, String content, String after, String before) {
        this.title = title;
        this.content = content;
        this.after = after;
        this.before = before;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }
}
