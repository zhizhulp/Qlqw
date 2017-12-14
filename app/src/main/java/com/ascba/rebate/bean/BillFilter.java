package com.ascba.rebate.bean;
/**
 * Created by 李平 on 2017/9/19 11:38
 * Describe: 账单筛选实体类
 */

public class BillFilter {

    /**
     * type_value : 0
     * type_text : 全部
     */
    private boolean select;
    private String type_value;
    private String type_text;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getType_value() {
        return type_value;
    }

    public void setType_value(String type_value) {
        this.type_value = type_value;
    }

    public String getType_text() {
        return type_text;
    }

    public void setType_text(String type_text) {
        this.type_text = type_text;
    }
}
