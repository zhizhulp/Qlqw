package com.ascba.rebate.bean;

/**
 * Created by lenovo on 2017/8/24.
 */

public class GetRegisterVerifyEntity {


    /**
     * verify : 1234
     * expiring_in : 1800
     */

    private int verify;
    private int expiring_in;

    public int getVerify() {
        return verify;
    }

    public void setVerify(int verify) {
        this.verify = verify;
    }

    public int getExpiring_in() {
        return expiring_in;
    }

    public void setExpiring_in(int expiring_in) {
        this.expiring_in = expiring_in;
    }
}
