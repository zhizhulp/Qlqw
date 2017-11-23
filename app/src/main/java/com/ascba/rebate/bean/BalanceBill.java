package com.ascba.rebate.bean;

/**
 * Created by 李平 on 2017/9/11 17:14
 * Describe:
 */
public class BalanceBill {
    private int type;
    private String desc;
    private String time;
    private String balance;
    private String flow;

    public BalanceBill(int type, String desc, String time, String balance, String flow) {
        this.type = type;
        this.desc = desc;
        this.time = time;
        this.balance = balance;
        this.flow = flow;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }
}
