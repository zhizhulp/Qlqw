package com.ascba.rebate.bean;

/**
 * Created by 李平 on 2017/9/7.
 * 身份证信息
 */

public class IDCardMsg {

    /**
     * address : 北京市海淀区XXXX
     * birthday : {"day":"2","month":"6","year":"1991"}
     * gender : 男
     * id_card_number : xxxxxx19910602xxxx
     * name : 陈XX
     */

    private String address;
    private BirthdayBean birthday;
    private String gender;
    private String id_card_number;
    private String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BirthdayBean getBirthday() {
        return birthday;
    }

    public void setBirthday(BirthdayBean birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class BirthdayBean {
        /**
         * day : 2
         * month : 6
         * year : 1991
         */

        private String day;
        private String month;
        private String year;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }
    }
}
