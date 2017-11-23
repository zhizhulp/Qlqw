package com.ascba.rebate.bean;

/**
 * Created by 李平 on 2017/9/7.
 * 实名认证信息实体类
 */

public class PIUserInfo {

    /**
     * cardid : 220284199008120614
     * realname : 聂国富
     * sex : 男
     * age : 27
     * location : 吉林省磐石市烟筒山镇
     */

    private String cardid;
    private String realname;
    private String sex;
    private int age;
    private String location;

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
