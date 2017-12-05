package com.ascba.rebate.bean;

/**
 * Created by Jero on 2017/12/5 0005.
 */

public class ArroundEntity {
    /**
     * seller_id : 35
     * seller_name : 钱来钱往实体店体验
     * seller_taglib : 办公
     * seller_cover_logo : http://3demo.oss-cn-beijing.aliyuncs.com/public/uploads/seller/logo/85/85.png
     * seller_business_hours : 08:00~20:00
     * seller_description : 韩诺医疗门诊部由国内上市的第三方医学检验机构——迪安诊断与韩国HANARO医疗财团联手打造，引进“早预防、早发现、早治疗、重管理”韩国先进医学理念，通过体检+专家门诊的服务体系，专注为追求高品质医疗服务的人群提供疾病的早期诊断和预防的健康管理服务。
     * seller_address : 湖北省武汉市硚口区园博园东路武汉园博园
     * earth_radius : 4438120
     * is_news : 0
     */

    private int seller_id;
    private String seller_name;
    private String seller_taglib;
    private String seller_cover_logo;
    private String seller_business_hours;
    private String seller_description;
    private String seller_address;
    private String earth_radius;
    private int is_news;
    private boolean isOpen = false;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getSeller_taglib() {
        return seller_taglib;
    }

    public void setSeller_taglib(String seller_taglib) {
        this.seller_taglib = seller_taglib;
    }

    public String getSeller_cover_logo() {
        return seller_cover_logo;
    }

    public void setSeller_cover_logo(String seller_cover_logo) {
        this.seller_cover_logo = seller_cover_logo;
    }

    public String getSeller_business_hours() {
        return seller_business_hours;
    }

    public void setSeller_business_hours(String seller_business_hours) {
        this.seller_business_hours = seller_business_hours;
    }

    public String getSeller_description() {
        return seller_description;
    }

    public void setSeller_description(String seller_description) {
        this.seller_description = seller_description;
    }

    public String getSeller_address() {
        return seller_address;
    }

    public void setSeller_address(String seller_address) {
        this.seller_address = seller_address;
    }

    public String getEarth_radius() {
        return earth_radius;
    }

    public void setEarth_radius(String earth_radius) {
        this.earth_radius = earth_radius;
    }

    public int getIs_news() {
        return is_news;
    }

    public void setIs_news(int is_news) {
        this.is_news = is_news;
    }
}
