package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by 李平 on 2017/9/16 14:45
 * Describe: 提现
 */

public class CashGet {

    /**
     * money : 0.01
     * has_bank : 1
     * bank_info : [{"id":351,"bank":"工商银行","nature":"借记卡","bank_card":"3370","is_default":1},{"id":350,"bank":"中国银行","nature":"借记卡","bank_card":"4064","is_default":0}]
     * take_money_rate : 服务费：0.13元/每笔
     * cash_button_tip : 确认提现
     * cash_intup_tip : 预计48小时内到帐
     */

    private String money;
    private int has_bank;
    private String take_money_rate;
    private String cash_button_tip;
    private String cash_intup_tip;
    private List<BankCard.BankListBean> bank_info;

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getHas_bank() {
        return has_bank;
    }

    public void setHas_bank(int has_bank) {
        this.has_bank = has_bank;
    }

    public String getTake_money_rate() {
        return take_money_rate;
    }

    public void setTake_money_rate(String take_money_rate) {
        this.take_money_rate = take_money_rate;
    }

    public String getCash_button_tip() {
        return cash_button_tip;
    }

    public void setCash_button_tip(String cash_button_tip) {
        this.cash_button_tip = cash_button_tip;
    }

    public String getCash_intup_tip() {
        return cash_intup_tip;
    }

    public void setCash_intup_tip(String cash_intup_tip) {
        this.cash_intup_tip = cash_intup_tip;
    }

    public List<BankCard.BankListBean> getBank_info() {
        return bank_info;
    }

    public void setBank_info(List<BankCard.BankListBean> bank_info) {
        this.bank_info = bank_info;
    }
}
