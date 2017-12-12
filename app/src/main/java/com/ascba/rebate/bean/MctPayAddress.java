package com.ascba.rebate.bean;

public class MctPayAddress extends MctBasePay {
    private String address;
    private boolean isBuyAgency;

    @Override
    public int getItemType() {
        return ITEM_TYPE_ADDRESS;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MctPayAddress(String address) {
        this.address = address;
    }

    public MctPayAddress(String address, boolean buyAgency) {
        this.address = address;
        isBuyAgency = buyAgency;
    }

    public boolean isBuyAgency() {
        return isBuyAgency;
    }

    public void setBuyAgency(boolean buyAgency) {
        isBuyAgency = buyAgency;
    }
}
