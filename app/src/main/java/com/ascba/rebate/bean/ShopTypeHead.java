package com.ascba.rebate.bean;


/**
 * Created by Administrator on 2018/1/31.
 */
public class ShopTypeHead extends STBase {
    private int position;
    private String text;

    public ShopTypeHead(String text,int position) {
        this.text = text;
        this.position = position;
    }

    @Override
    public int getSpan() {
        return 3;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
