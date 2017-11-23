package com.ascba.rebate.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李平 on 2017/9/6.
 * 公司信息实体类
 */

public class ComMsg implements Parcelable {


    /**
     * id : 566
     * name : 公司
     * oper_name : 张德江
     * regist_capi : 保密
     * company_status : 续存
     * scope : 123456
     * is_oper_name : 0
     * clientele_name : 赵俊峰
     * chartered : /public/uploads/chartered/82/82.png?1504595076
     * warrant : /public/uploads/warrant/82/82.png?1504595076
     * "chartered_demo":"http://apidebug.qlqwp2p.com/agreement/register",
     * "warrant_demo":"http://apidebug.qlqwp2p.com/agreement/register",
     * "submit_tip":"授权书不符合规范，请重新上传",
     *  status : 1
     */

    private int id;
    private String name;
    private String oper_name;
    private String regist_capi;
    private String company_status;
    private String scope;
    private int is_oper_name;
    private String clientele_name;
    private String chartered;
    private String warrant;
    private int status;
    private String chartered_demo;
    private String warrant_demo;
    private String submit_tip;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOper_name() {
        return oper_name;
    }

    public void setOper_name(String oper_name) {
        this.oper_name = oper_name;
    }

    public String getRegist_capi() {
        return regist_capi;
    }

    public void setRegist_capi(String regist_capi) {
        this.regist_capi = regist_capi;
    }

    public String getCompany_status() {
        return company_status;
    }

    public void setCompany_status(String company_status) {
        this.company_status = company_status;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public int getIs_oper_name() {
        return is_oper_name;
    }

    public void setIs_oper_name(int is_oper_name) {
        this.is_oper_name = is_oper_name;
    }

    public String getClientele_name() {
        return clientele_name;
    }

    public void setClientele_name(String clientele_name) {
        this.clientele_name = clientele_name;
    }

    public String getChartered() {
        return chartered;
    }

    public void setChartered(String chartered) {
        this.chartered = chartered;
    }

    public String getWarrant() {
        return warrant;
    }

    public void setWarrant(String warrant) {
        this.warrant = warrant;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getChartered_demo() {
        return chartered_demo;
    }

    public void setChartered_demo(String chartered_demo) {
        this.chartered_demo = chartered_demo;
    }

    public String getWarrant_demo() {
        return warrant_demo;
    }

    public void setWarrant_demo(String warrant_demo) {
        this.warrant_demo = warrant_demo;
    }

    public String getSubmit_tip() {
        return submit_tip;
    }

    public void setSubmit_tip(String submit_tip) {
        this.submit_tip = submit_tip;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.oper_name);
        dest.writeString(this.regist_capi);
        dest.writeString(this.company_status);
        dest.writeString(this.scope);
        dest.writeInt(this.is_oper_name);
        dest.writeString(this.clientele_name);
        dest.writeString(this.chartered);
        dest.writeString(this.warrant);
        dest.writeInt(this.status);
        dest.writeString(this.chartered_demo);
        dest.writeString(this.warrant_demo);
        dest.writeString(this.submit_tip);
    }

    public ComMsg() {
    }

    protected ComMsg(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.oper_name = in.readString();
        this.regist_capi = in.readString();
        this.company_status = in.readString();
        this.scope = in.readString();
        this.is_oper_name = in.readInt();
        this.clientele_name = in.readString();
        this.chartered = in.readString();
        this.warrant = in.readString();
        this.status = in.readInt();
        this.chartered_demo = in.readString();
        this.warrant_demo = in.readString();
        this.submit_tip = in.readString();
    }

    public static final Parcelable.Creator<ComMsg> CREATOR = new Parcelable.Creator<ComMsg>() {
        @Override
        public ComMsg createFromParcel(Parcel source) {
            return new ComMsg(source);
        }

        @Override
        public ComMsg[] newArray(int size) {
            return new ComMsg[size];
        }
    };
}
