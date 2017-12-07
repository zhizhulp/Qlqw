package com.ascba.rebate.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;


public class MctBasePay implements MultiItemEntity {
    public static int ITEM_TYPE_TITLE =0;
    public static int ITEM_TYPE_CLASS =1;
    public static int ITEM_TYPE_DESC =2;
    @Override
    public int getItemType() {
        return ITEM_TYPE_TITLE;
    }
}
