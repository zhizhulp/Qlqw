package com.ascba.rebate.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Administrator on 2018/1/31.
 */
public class STBase implements MultiItemEntity {
    private int span;
    @Override
    public int getItemType() {
        return 0;
    }

    public int getSpan(){
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }
}
