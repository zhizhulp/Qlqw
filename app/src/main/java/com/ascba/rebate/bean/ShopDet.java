package com.ascba.rebate.bean;

/**
 * Created by Administrator on 2018/1/25.
 *  店铺信息
 */
public class ShopDet {


    /**
     * store_type : 1
     * store_name :
     * store_logo :
     * store_telphone :
     * store_description :
     * company_status : 0
     * company_status_text : 未企业认证，请前往认证！
     * primary_class_value : 主营类目，如果无返回null
     * type_can_change : 0
     * tip_status : 0
     */

    private int store_type;
    private String store_name;
    private String store_logo;
    private String store_telphone;
    private String store_description;
    private int company_status;
    private String company_status_text;
    private int type_can_change;
    private int tip_status;
    private String primary_class_value;

    public String getPrimary_class_value() {
        return primary_class_value;
    }

    public void setPrimary_class_value(String primary_class_value) {
        this.primary_class_value = primary_class_value;
    }

    public int getStore_type() {
        return store_type;
    }

    public void setStore_type(int store_type) {
        this.store_type = store_type;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_logo() {
        return store_logo;
    }

    public void setStore_logo(String store_logo) {
        this.store_logo = store_logo;
    }

    public String getStore_telphone() {
        return store_telphone;
    }

    public void setStore_telphone(String store_telphone) {
        this.store_telphone = store_telphone;
    }

    public String getStore_description() {
        return store_description;
    }

    public void setStore_description(String store_description) {
        this.store_description = store_description;
    }

    public int getCompany_status() {
        return company_status;
    }

    public void setCompany_status(int company_status) {
        this.company_status = company_status;
    }

    public String getCompany_status_text() {
        return company_status_text;
    }

    public void setCompany_status_text(String company_status_text) {
        this.company_status_text = company_status_text;
    }

    public int getType_can_change() {
        return type_can_change;
    }

    public void setType_can_change(int type_can_change) {
        this.type_can_change = type_can_change;
    }

    public int getTip_status() {
        return tip_status;
    }

    public void setTip_status(int tip_status) {
        this.tip_status = tip_status;
    }
}
