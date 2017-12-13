package com.ascba.rebate.bean;

import android.support.annotation.ColorInt;

import com.ascba.rebate.adapter.AgentAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Jero on 2017/12/11 0011.
 */

public class AgentItem implements MultiItemEntity {
    private int type;
    private int color;
    private String title;
    private String content;
    private String nextTitle;
    private int nextType;// 1 现金账单  2 礼品份账单 3 福利券账单
    private String listType;

    public AgentItem(@ColorInt int color, String title) {
        setType(AgentAdapter.TITLE_TYPE);
        setColor(color);
        setTitle(title);
    }

    public AgentItem(@ColorInt int color, String title, String content) {
        setType(AgentAdapter.NUM_TYPE);
        setColor(color);
        setTitle(title);
        setContent(content);
    }

    public AgentItem(@ColorInt int color, String title, String content, String nextTitle, int nextType, String listType) {
        this(color, title, content);
        setNextTitle(nextTitle);
        setNextType(nextType);
        setListType(listType);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
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

    public String getNextTitle() {
        return nextTitle;
    }

    public void setNextTitle(String nextTitle) {
        this.nextTitle = nextTitle;
    }

    public int getNextType() {
        return nextType;
    }

    public void setNextType(int nextType) {
        this.nextType = nextType;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
