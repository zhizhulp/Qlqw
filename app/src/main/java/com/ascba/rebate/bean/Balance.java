package com.ascba.rebate.bean;

/**
 * Created by 李平 on 2017/9/15 10:56
 * Describe: 余额
 */

public class Balance {

    private String money;
    private String balance_question;

    public String getBalance_question() {
        return balance_question;
    }

    public void setBalance_question(String balance_question) {
        this.balance_question = balance_question;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
