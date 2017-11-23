package com.ascba.rebate.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by 李平 on 2017/9/18 10:42
 * Describe: 账单
 */

public class Bill implements MultiItemEntity {

    /**
     * type : 1
     * money : +200.00
     * remark : 充值100元
     * log_time : 2016-10-24 13:45:45
     * year : 2016
     * month : 10
     * avatar : http://img.qlqwshop.com/public/app/images/xf.png
     */

    private int type;
    @JSONField(name = "money", alternateNames = {"voucher", "score"})
    private String money;
    private String remark;
    private String log_time;
    private String year;
    private String month;
    private String avatar;
    private String status_text;
    private int itemType;
    private boolean headIconVisible;
    private int bill_id;//提现id
    private int object_id;

    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public void setHeadIconVisible(boolean headIconVisible) {
        this.headIconVisible = headIconVisible;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public boolean isHeadIconVisible() {
        return headIconVisible;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getLog_time() {
        return log_time;
    }

    public void setLog_time(String log_time) {
        this.log_time = log_time;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }
}
