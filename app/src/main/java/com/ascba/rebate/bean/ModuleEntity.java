package com.ascba.rebate.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by Jero on 2017/10/10 0010.
 */

public class ModuleEntity implements MultiItemEntity {
    private int itemType = 1;

    private int isSelect = 0;

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int type) {
        itemType = type;
    }

    public int isSelect() {
        return isSelect;
    }

    public void setSelect(int select) {
        isSelect = select;
    }

    /**
     * category_name : 资金往来
     * category_label : Capital
     */

    private String category_name;
    private String category_label;

    public ModuleEntity(String name, String label) {
        category_name = name;
        category_label = label;
        itemType = 4;
    }

    /**
     * nav_name : 充值中心
     * nav_icon : http://apidebug.qlqwp2p.com/public/uploads/product/2017-10-11/59ddc275a6479.png
     * nav_label : Recharge
     * nav_id : 2
     * is_read : 0 (0:没有,1:小红点,2:最新)
     * nav_activity : http://apidebug.qlqwp2p.com/public/uploads/goods/2017-10-23/59edbcd5b89f1.png
     * httpurl : http://cn.mikecrm.com/WivVpt7
     */

    private String httpurl;
    private String nav_activity;
    private String nav_name;
    private String nav_icon;
    private String nav_label;
    private int nav_id;
    private int is_read;
    private int is_disappear;

    public ModuleEntity() {

    }

    public int getIs_disappear() {
        return is_disappear;
    }

    public void setIs_disappear(int is_disappear) {
        this.is_disappear = is_disappear;
    }

    public String getNav_name() {
        return nav_name;
    }

    public void setNav_name(String nav_name) {
        this.nav_name = nav_name;
    }

    public String getNav_icon() {
        return nav_icon;
    }

    public void setNav_icon(String nav_icon) {
        this.nav_icon = nav_icon;
    }

    public String getNav_label() {
        return nav_label;
    }

    public void setNav_label(String nav_label) {
        this.nav_label = nav_label;
    }

    public int getNav_id() {
        return nav_id;
    }

    public void setNav_id(int nav_id) {
        this.nav_id = nav_id;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_label() {
        return category_label;
    }

    public void setCategory_label(String category_label) {
        this.category_label = category_label;
    }

    public String getNav_activity() {
        return nav_activity;
    }

    public void setNav_activity(String nav_activity) {
        this.nav_activity = nav_activity;
    }

    public String getHttpurl() {
        return httpurl;
    }

    public void setHttpurl(String httpurl) {
        this.httpurl = httpurl;
    }
}
