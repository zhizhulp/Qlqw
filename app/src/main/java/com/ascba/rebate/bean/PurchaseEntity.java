package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by Jero on 2017/10/16 0016.
 */

public class PurchaseEntity {

    /**
     * money_config : [{"cz_money":1000,"cz_rate_money":100000,"cz_desc":"普卡A"},{"cz_money":3000,"cz_rate_money":300000,"cz_desc":"普卡B"},{"cz_money":5000,"cz_rate_money":2500000,"cz_desc":"银卡A"},{"cz_money":7000,"cz_rate_money":3500000,"cz_desc":"银卡B"},{"cz_money":10000,"cz_rate_money":10000000,"cz_desc":"金卡A"},{"cz_money":20000,"cz_rate_money":20000000,"cz_desc":"金卡B"}]
     * money : 25899.00
     * balance_status : 1  (余额状态:  '0无法余额充值 '1可以)
     * is_level_pwd : 1  ('1有支付密码'0无支付密码)
     * tips : 礼品包价值（温馨提示：充值越多赠送越多）
     * agreement_url : http://app.qlqwgw.com/PurchaseAgreement
     */

    private String money;
    private int balance_status;
    private int is_level_pwd;
    private String tips;
    private List<MoneyConfigBean> money_config;
    private String agreement_url;

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

    public static class MoneyConfigBean {
        /**
         * cz_money : 1000
         * cz_rate_money : 100000
         * cz_desc : 普卡A
         */

        private String cz_money;
        private int cz_rate_money;
        private String cz_desc;
        private int only_price;

        public int getOnly_price() {
            return only_price;
        }

        public void setOnly_price(int only_price) {
            this.only_price = only_price;
        }

        public String getMoney() {
            if (getOnly_price() == 1) {
                return "";
            } else if (getOnly_price() == 0) {
                return getCz_money();
            }
            return "";
        }

        public String getCz_money() {
            return cz_money;
        }

        public void setCz_money(String cz_money) {
            this.cz_money = cz_money;
        }

        public int getCz_rate_money() {
            return cz_rate_money;
        }

        public void setCz_rate_money(int cz_rate_money) {
            this.cz_rate_money = cz_rate_money;
        }

        public String getCz_desc() {
            return cz_desc;
        }

        public void setCz_desc(String cz_desc) {
            this.cz_desc = cz_desc;
        }
    }
}
