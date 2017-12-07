package com.ascba.rebate.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class MctPayDesc extends MctBasePay {
    @JSONField(name = "interests_image")
    private String descImg;
    @JSONField(name = "interests_title")
    private String title;
    @JSONField(name = "interests_desc")
    private String content;
    @Override
    public int getItemType() {
        return ITEM_TYPE_DESC;
    }

    public MctPayDesc() {
    }

    public String getDescImg() {
        return descImg;
    }

    public void setDescImg(String descImg) {
        this.descImg = descImg;
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

    public MctPayDesc(String descImg, String title, String content) {
        this.descImg = descImg;
        this.title = title;
        this.content = content;
    }
}
