package com.ascba.rebate.bean;

import com.ascba.rebate.adapter.InvoiceSelectAdapter;
import com.ascba.rebate.utils.CommonMethodUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Jero on 2017/11/28 0028.
 */

public class InvoiceSelect implements MultiItemEntity {
    /**
     * bill_id : 546
     * money : 16
     * remark : 酒融行居间服务费
     * log_time : 今天 16:44
     * year : 2017
     * month : 11
     * money_text : -16.00
     */

    private int bill_id;
    private float money;
    private String remark;
    private String log_time;
    private String year;
    private String month;
    private String money_text;
    private boolean isSelect;
    private int type;

    public InvoiceSelect() {
        setType(InvoiceSelectAdapter.TYPE_ITEM);
    }

    public InvoiceSelect(String mm, String yy) {
        setMonth(CommonMethodUtils.getMonthString(mm, yy));
        setType(InvoiceSelectAdapter.TYPE_HEAD);
    }

    public InvoiceSelect setHead(String mm, String yy) {
        setMonth(CommonMethodUtils.getMonthString(mm, yy));
        setType(InvoiceSelectAdapter.TYPE_HEAD);
        return this;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
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

    public String getMoney_text() {
        return money_text;
    }

    public void setMoney_text(String money_text) {
        this.money_text = money_text;
    }
}
