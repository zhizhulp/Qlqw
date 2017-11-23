package com.ascba.rebate.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.w3c.dom.ProcessingInstruction;

public class Trade implements Parcelable {

    /**
     * order_id : 1618
     * order_number : XF20171018170734544810
     * avatar : http://www.testqlqw.org/public/uploads/seller/logo/23/23.png
     * name : 老家肉饼
     * money : -10.00
     * pay_type : 1
     * pay_type_text : 现金交易
     * order_status : 0
     * order_status_text : 交易中
     * member_username : 132****7083
     * member_money : 51.00
     * seller_contact : 13253587083
     * score : 1000
     * pay_commission : ¥1.60
     * create_time : 2017-10-18 17:07:34
     * order_identity : customer
     */

    private String order_id;
    private String order_number;
    private String avatar;
    private String name;
    private String money;
    private int pay_type;
    private String pay_type_text;
    private int order_status;
    private String order_status_text;
    private String member_username;
    private String member_money;
    private String seller_contact;
    private String score;
    private String pay_commission;
    private String create_time;
    private String order_identity;
    private boolean purchase_status;
    private String sys_score_text;

    public String getSys_score_text() {
        return sys_score_text;
    }

    public void setSys_score_text(String sys_score_text) {
        this.sys_score_text = sys_score_text;
    }

    public boolean isPurchase_status() {
        return purchase_status;
    }

    public void setPurchase_status(boolean purchase_status) {
        this.purchase_status = purchase_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_type_text() {
        return pay_type_text;
    }

    public void setPay_type_text(String pay_type_text) {
        this.pay_type_text = pay_type_text;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getOrder_status_text() {
        return order_status_text;
    }

    public void setOrder_status_text(String order_status_text) {
        this.order_status_text = order_status_text;
    }

    public String getMember_username() {
        return member_username;
    }

    public void setMember_username(String member_username) {
        this.member_username = member_username;
    }

    public String getMember_money() {
        return member_money;
    }

    public void setMember_money(String member_money) {
        this.member_money = member_money;
    }

    public String getSeller_contact() {
        return seller_contact;
    }

    public void setSeller_contact(String seller_contact) {
        this.seller_contact = seller_contact;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPay_commission() {
        return pay_commission;
    }

    public void setPay_commission(String pay_commission) {
        this.pay_commission = pay_commission;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOrder_identity() {
        return order_identity;
    }

    public void setOrder_identity(String order_identity) {
        this.order_identity = order_identity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.order_id);
        dest.writeString(this.order_number);
        dest.writeString(this.avatar);
        dest.writeString(this.name);
        dest.writeString(this.money);
        dest.writeInt(this.pay_type);
        dest.writeString(this.pay_type_text);
        dest.writeInt(this.order_status);
        dest.writeString(this.order_status_text);
        dest.writeString(this.member_username);
        dest.writeString(this.member_money);
        dest.writeString(this.seller_contact);
        dest.writeString(this.score);
        dest.writeString(this.pay_commission);
        dest.writeString(this.create_time);
        dest.writeString(this.order_identity);
        dest.writeByte(this.purchase_status ? (byte) 1 : (byte) 0);
        dest.writeString(this.sys_score_text);
    }

    public Trade() {
    }

    protected Trade(Parcel in) {
        this.order_id = in.readString();
        this.order_number = in.readString();
        this.avatar = in.readString();
        this.name = in.readString();
        this.money = in.readString();
        this.pay_type = in.readInt();
        this.pay_type_text = in.readString();
        this.order_status = in.readInt();
        this.order_status_text = in.readString();
        this.member_username = in.readString();
        this.member_money = in.readString();
        this.seller_contact = in.readString();
        this.score = in.readString();
        this.pay_commission = in.readString();
        this.create_time = in.readString();
        this.order_identity = in.readString();
        this.purchase_status = in.readByte() != 0;
        this.sys_score_text = in.readString();
    }

    public static final Parcelable.Creator<Trade> CREATOR = new Parcelable.Creator<Trade>() {
        @Override
        public Trade createFromParcel(Parcel source) {
            return new Trade(source);
        }

        @Override
        public Trade[] newArray(int size) {
            return new Trade[size];
        }
    };
}
