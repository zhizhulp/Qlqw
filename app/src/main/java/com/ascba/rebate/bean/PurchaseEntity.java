package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by Jero on 2017/10/16 0016.
 */

public class PurchaseEntity {

    /**
     * money_config : [{"level_id": 1,"cz_money": "110.00","cz_desc": "普通卡3","cz_rate_money": 10000,"only_price": 1},
     * {"level_id": 2,"cz_money": "80.00","cz_desc": "普通卡2","cz_rate_money": 16000,"only_price": 1},
     * {"level_id": 3,"cz_money": "98.00","cz_original_money": "100.00","cz_desc": "普通卡1","cz_rate_money": 10000,"only_price": 0,"cz_corner": "9.8折","cz_tip": "折扣价"}]
     * money : 25899.00
     * balance_status : 1  (余额状态:  '0无法余额充值 '1可以)
     * is_level_pwd : 1  ('1有支付密码'0无支付密码)
     * tips : 礼品包价值（温馨提示：充值越多赠送越多）
     * agreement_url : http://app.qlqwgw.com/PurchaseAgreement
     * back_money_url : http://www.baidu.com
     * back_money_text : 退款保障
     * back_money : 20
     * back_money_tips : 20元/次，7天内可申请退款，退款额为礼品包金额的95%
     * invoice_url : http://www.baidu.com
     * invoice : 5
     * invoice_text : 发票报销
     * invoice_tips : 可开具全额普通发票，税点为5%
     * is_invoice : 1
     */

    private String money;
    private int balance_status;
    private int is_level_pwd;
    private String tips;
    private List<MoneyConfigBean> money_config;
    private String agreement_url;

    private String back_money_url;
    private String back_money_text;
    private int back_money;
    private String back_money_tips;
    private String invoice_url;
    private int invoice;
    private String invoice_text;
    private String invoice_tips;
    private int is_invoice;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getBalance_status() {
        return balance_status;
    }

    public void setBalance_status(int balance_status) {
        this.balance_status = balance_status;
    }

    public int getIs_level_pwd() {
        return is_level_pwd;
    }

    public void setIs_level_pwd(int is_level_pwd) {
        this.is_level_pwd = is_level_pwd;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<MoneyConfigBean> getMoney_config() {
        return money_config;
    }

    public void setMoney_config(List<MoneyConfigBean> money_config) {
        this.money_config = money_config;
    }

    public String getAgreement_url() {
        return agreement_url;
    }

    public void setAgreement_url(String agreement_url) {
        this.agreement_url = agreement_url;
    }

    public String getBack_money_url() {
        return back_money_url;
    }

    public void setBack_money_url(String back_money_url) {
        this.back_money_url = back_money_url;
    }

    public String getBack_money_text() {
        return back_money_text;
    }

    public void setBack_money_text(String back_money_text) {
        this.back_money_text = back_money_text;
    }

    public int getBack_money() {
        return back_money;
    }

    public void setBack_money(int back_money) {
        this.back_money = back_money;
    }

    public String getBack_money_tips() {
        return back_money_tips;
    }

    public void setBack_money_tips(String back_money_tips) {
        this.back_money_tips = back_money_tips;
    }

    public String getInvoice_url() {
        return invoice_url;
    }

    public void setInvoice_url(String invoice_url) {
        this.invoice_url = invoice_url;
    }

    public int getInvoice() {
        return invoice;
    }

    public void setInvoice(int invoice) {
        this.invoice = invoice;
    }

    public String getInvoice_text() {
        return invoice_text;
    }

    public void setInvoice_text(String invoice_text) {
        this.invoice_text = invoice_text;
    }

    public String getInvoice_tips() {
        return invoice_tips;
    }

    public void setInvoice_tips(String invoice_tips) {
        this.invoice_tips = invoice_tips;
    }

    public int getIs_invoice() {
        return is_invoice;
    }

    public void setIs_invoice(int is_invoice) {
        this.is_invoice = is_invoice;
    }

    public static class MoneyConfigBean {
        /**
         * level_id : 1
         * cz_money : 110.00
         * cz_desc : 普通卡3
         * cz_rate_money : 10000
         * only_price : 1
         * cz_original_money : 100.00
         * cz_corner : 9.8折
         * cz_tip : 折扣价
         */

        private int level_id;
        private String cz_money;
        private String cz_rate_money;
        private String cz_desc;
        private int only_price;
        private String cz_original_money;
        private String cz_corner;
        private String cz_tip;

        public int getLevel_id() {
            return level_id;
        }

        public void setLevel_id(int level_id) {
            this.level_id = level_id;
        }

        public String getCz_money() {
            return cz_money;
        }

        public void setCz_money(String cz_money) {
            this.cz_money = cz_money;
        }

        public String getCz_desc() {
            return cz_desc;
        }

        public void setCz_desc(String cz_desc) {
            this.cz_desc = cz_desc;
        }

        public String getCz_rate_money() {
            return cz_rate_money;
        }

        public void setCz_rate_money(String cz_rate_money) {
            this.cz_rate_money = cz_rate_money;
        }

        public int getOnly_price() {
            return only_price;
        }

        public void setOnly_price(int only_price) {
            this.only_price = only_price;
        }

        public String getCz_original_money() {
            return cz_original_money;
        }

        public void setCz_original_money(String cz_original_money) {
            this.cz_original_money = cz_original_money;
        }

        public String getCz_corner() {
            return cz_corner;
        }

        public void setCz_corner(String cz_corner) {
            this.cz_corner = cz_corner;
        }

        public String getCz_tip() {
            return cz_tip;
        }

        public void setCz_tip(String cz_tip) {
            this.cz_tip = cz_tip;
        }
    }
}
