package com.ascba.rebate.bean;
/**
 * Created by 李平 on 2017/12/7 9:52
 * Describe: 商家资料界面
 */

public class SellerDet {

    private String perfect_url;
    private SellerBean seller;

    private int seller_error_status;
    private String seller_error_status_text;

    public String getPerfect_url() {
        return perfect_url;
    }

    public void setPerfect_url(String perfect_url) {
        this.perfect_url = perfect_url;
    }

    public SellerBean getSeller() {
        return seller;
    }

    public void setSeller(SellerBean seller) {
        this.seller = seller;
    }

    public int getSeller_error_status() {
        return seller_error_status;
    }

    public void setSeller_error_status(int seller_error_status) {
        this.seller_error_status = seller_error_status;
    }

    public String getSeller_error_status_text() {
        return seller_error_status_text;
    }

    public void setSeller_error_status_text(String seller_error_status_text) {
        this.seller_error_status_text = seller_error_status_text;
    }

    public static class SellerBean {
        /**
         * seller_name : 愤怒大鱿鱼
         * seller_cover_logo : http://3demo.oss-cn-beijing.aliyuncs.com/public/uploads/seller/logo/991/991.png
         * seller_image : http://3demo.oss-cn-beijing.aliyuncs.com/public/uploads/seller/image/991/991.png
         * seller_taglib : 餐饮
         * seller_business_hours : 09:00~23:00
         * seller_tel :
         * seller_address :
         * seller_localhost :
         * seller_description :
         */

        private String seller_name;
        private String seller_cover_logo;
        private String seller_image;
        private String seller_taglib;
        private String seller_business_hours;
        private String seller_tel;
        private String seller_address;
        private String seller_localhost;
        private String seller_description;
        private String region_name;
        private int seller_status;
        private int region_id;

        public int getRegion_id() {
            return region_id;
        }

        public void setRegion_id(int region_id) {
            this.region_id = region_id;
        }

        public int getSeller_status() {
            return seller_status;
        }

        public void setSeller_status(int seller_status) {
            this.seller_status = seller_status;
        }

        public String getRegion_name() {
            return region_name;
        }

        public void setRegion_name(String region_name) {
            this.region_name = region_name;
        }

        public String getSeller_name() {
            return seller_name;
        }

        public void setSeller_name(String seller_name) {
            this.seller_name = seller_name;
        }

        public String getSeller_cover_logo() {
            return seller_cover_logo;
        }

        public void setSeller_cover_logo(String seller_cover_logo) {
            this.seller_cover_logo = seller_cover_logo;
        }

        public String getSeller_image() {
            return seller_image;
        }

        public void setSeller_image(String seller_image) {
            this.seller_image = seller_image;
        }

        public String getSeller_taglib() {
            return seller_taglib;
        }

        public void setSeller_taglib(String seller_taglib) {
            this.seller_taglib = seller_taglib;
        }

        public String getSeller_business_hours() {
            return seller_business_hours;
        }

        public void setSeller_business_hours(String seller_business_hours) {
            this.seller_business_hours = seller_business_hours;
        }

        public String getSeller_tel() {
            return seller_tel;
        }

        public void setSeller_tel(String seller_tel) {
            this.seller_tel = seller_tel;
        }

        public String getSeller_address() {
            return seller_address;
        }

        public void setSeller_address(String seller_address) {
            this.seller_address = seller_address;
        }

        public String getSeller_localhost() {
            return seller_localhost;
        }

        public void setSeller_localhost(String seller_localhost) {
            this.seller_localhost = seller_localhost;
        }

        public String getSeller_description() {
            return seller_description;
        }

        public void setSeller_description(String seller_description) {
            this.seller_description = seller_description;
        }
    }
}
