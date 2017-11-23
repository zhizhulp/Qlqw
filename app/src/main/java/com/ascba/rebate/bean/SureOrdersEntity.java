package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by lenovo on 2017/7/5.
 */

public class SureOrdersEntity {

    /**
     * iden_info : {"title":"待确认总额","total_money":114,"total":4,"tip":"仅统计待确认交易账单，其它请查看流水记录"}
     * data_list : [{"scan_qrcode_id":589,"avatar":"/public/base/images/default.jpg","money":"+1.00","pay_type_text":"现金付款","order_status_text":"待确认","create_time":1499398972},{"fivepercent_log_id":590,"avatar":"/public/base/images/default.jpg","money":"+1.00","pay_type_text":"现金付款","order_status_text":"待确认","create_time":1499399086},{"fivepercent_log_id":612,"avatar":"/public/base/images/default.jpg","money":"+111.00","pay_type_text":"现金付款","order_status_text":"待确认","create_time":1499409210},{"fivepercent_log_id":618,"avatar":"/public/base/images/default.jpg","money":"+1.00","pay_type_text":"现金付款","order_status_text":"待确认","create_time":1499411801}]
     */

    private InfoBean iden_info;
    private List<DataListBean> data_list;

    public InfoBean getIden_info() {
        return iden_info;
    }

    public void setIden_info(InfoBean iden_info) {
        this.iden_info = iden_info;
    }

    public List<DataListBean> getData_list() {
        return data_list;
    }

    public void setData_list(List<DataListBean> data_list) {
        this.data_list = data_list;
    }

    public static class InfoBean {
        /**
         * title : 待确认总额
         * total_money : 114
         * total : 4
         * tip : 仅统计待确认交易账单，其它请查看流水记录
         */

        private String title;
        private int total_money;
        private int total;
        private String tip;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTotal_money() {
            return total_money;
        }

        public void setTotal_money(int total_money) {
            this.total_money = total_money;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }
    }

    public static class DataListBean {
        /**
         * fivepercent_log_id : 589
         * avatar : /public/base/images/default.jpg
         * money : +1.00
         * pay_type_text : 现金付款
         * order_status_text : 待确认
         * create_time : 1499398972
         */

        private int scan_qrcode_id;
        private String avatar;
        private String money;
        private String pay_type_text;
        private String order_status_text;
        private String create_time;

        public int getScan_qrcode_id() {
            return scan_qrcode_id;
        }

        public void setScan_qrcode_id(int scan_qrcode_id) {
            this.scan_qrcode_id = scan_qrcode_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getPay_type_text() {
            return pay_type_text;
        }

        public void setPay_type_text(String pay_type_text) {
            this.pay_type_text = pay_type_text;
        }

        public String getOrder_status_text() {
            return order_status_text;
        }

        public void setOrder_status_text(String order_status_text) {
            this.order_status_text = order_status_text;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }
    }

}
