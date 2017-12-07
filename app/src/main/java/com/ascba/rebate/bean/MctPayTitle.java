package com.ascba.rebate.bean;

public class MctPayTitle extends MctBasePay {
    private String title;
    @Override
    public int getItemType() {
        return ITEM_TYPE_TITLE;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MctPayTitle(String title) {
        this.title = title;
    }
}
