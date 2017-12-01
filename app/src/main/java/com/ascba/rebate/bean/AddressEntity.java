package com.ascba.rebate.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by Jero on 2017/9/20 0020.
 */

public class AddressEntity implements Parcelable {
    /**
     * address_id : 111
     * consignee : 聂国强
     * address : 烟筒山镇南庆村二社
     * mobile : 18232669445
     * type : 0
     * province : 129861
     * city : 132041
     * district : 133524
     * address_region : 吉林省 吉林市 磐石市
     * address_detail : 吉林省 吉林市 磐石市 烟筒山镇南庆村二社
     */

    private int address_id;
    @JSONField(name = "consignee", alternateNames = {"address_realname"})
    private String consignee;
    private String address;
    @JSONField(name = "mobile", alternateNames = {"address_mobile"})
    private String mobile;
    private int type;
    private int province;
    private int city;
    private int district;
    private String address_region;
    private String address_detail;

    public int getAddress_id() {
        return address_id;
    }

    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getDistrict() {
        return district;
    }

    public void setDistrict(int district) {
        this.district = district;
    }

    public String getAddress_region() {
        return address_region;
    }

    public void setAddress_region(String address_region) {
        this.address_region = address_region;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.address_id);
        dest.writeString(this.consignee);
        dest.writeString(this.address);
        dest.writeString(this.mobile);
        dest.writeInt(this.type);
        dest.writeInt(this.province);
        dest.writeInt(this.city);
        dest.writeInt(this.district);
        dest.writeString(this.address_region);
        dest.writeString(this.address_detail);
    }

    public AddressEntity() {
    }

    protected AddressEntity(Parcel in) {
        this.address_id = in.readInt();
        this.consignee = in.readString();
        this.address = in.readString();
        this.mobile = in.readString();
        this.type = in.readInt();
        this.province = in.readInt();
        this.city = in.readInt();
        this.district = in.readInt();
        this.address_region = in.readString();
        this.address_detail = in.readString();
    }

    public static final Creator<AddressEntity> CREATOR = new Creator<AddressEntity>() {
        @Override
        public AddressEntity createFromParcel(Parcel source) {
            return new AddressEntity(source);
        }

        @Override
        public AddressEntity[] newArray(int size) {
            return new AddressEntity[size];
        }
    };
}
