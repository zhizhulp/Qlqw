package com.ascba.rebate.bean;

public class MctPayAddress extends MctBasePay {
    private String address;
    private int isBuyAgency;//是否购买过代理，1已买过，0未买过 2修改状态
    private int selectID;

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

    public MctPayAddress(String address, int buyAgency, int id) {
        this.address = address;
        isBuyAgency = buyAgency;
        selectID = id;
    }

    public int getIsBuyAgency() {
        return isBuyAgency;
    }

    public void setIsBuyAgency(int isBuyAgency) {
        this.isBuyAgency = isBuyAgency;
    }

    public int getSelectID() {
        return selectID;
    }

    public void setSelectID(int selectID) {
        this.selectID = selectID;
    }
}
