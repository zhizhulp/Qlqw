package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by 李平 on 2017/9/13 15:02
 * Describe: 银行卡
 */

public class BankCard {

    private List<BankListBean> bank_list;

    public List<BankListBean> getBank_list() {
        return bank_list;
    }

    public void setBank_list(List<BankListBean> bank_list) {
        this.bank_list = bank_list;
    }

    public static class BankListBean {
        /**
         * id : 31
         * bank : 建设银行
         * type : 龙卡储蓄卡
         * nature : 借记卡
         * bank_card : 2527
         * is_default : 1
         */

        private int id;
        private String bank;
        private String type;
        private String nature;
        private String bank_card;
        private int is_default;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNature() {
            return nature;
        }

        public void setNature(String nature) {
            this.nature = nature;
        }

        public String getBank_card() {
            return bank_card;
        }

        public void setBank_card(String bank_card) {
            this.bank_card = bank_card;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }
    }
}
