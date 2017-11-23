package com.ascba.rebate.bean;

/**
 * Created by lenovo on 2017/8/22.
 */

public class MineEntity {

    /**
     * userData : {"white_score":0,"coupon":"0.00","money":"0.00","joinPic":"http://apidebug.qlqwp2p.com/public/static/base/images/join_pic.png"}
     * userInfo : {"company_status":0,"age":18,"group_name":"普通会员","nickname":"17801034237","mobile":"17801034237","avatar":"http://apidebug.qlqwp2p.com/public/static/base/images/default.jpg","is_level_pwd":0,"sex":"保密","card_status":0}
     */

    private UserDataBean userData;
    private UserInfoBean userInfo;

    public UserDataBean getUserData() {
        return userData;
    }

    public void setUserData(UserDataBean userData) {
        this.userData = userData;
    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserDataBean {
        /**
         * white_score : 0
         * money : 0.00
         * joinPic : http://apidebug.qlqwp2p.com/public/static/base/images/join_pic.png
         */

        private String white_score;
        private String money;
        private String joinPic;
        private String voucher;
        private String bill;
        private String recharge;

        public String getVoucher() {
            return voucher;
        }

        public void setVoucher(String voucher) {
            this.voucher = voucher;
        }

        public String getWhite_score() {
            return white_score;
        }

        public void setWhite_score(String white_score) {
            this.white_score = white_score;
        }


        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getJoinPic() {
            return joinPic;
        }

        public void setJoinPic(String joinPic) {
            this.joinPic = joinPic;
        }

        public String getBill() {
            return bill;
        }

        public void setBill(String bill) {
            this.bill = bill;
        }

        public String getRecharge() {
            return recharge;
        }

        public void setRecharge(String recharge) {
            this.recharge = recharge;
        }
    }


    public static class UserInfoBean {
        /**
         * company_status : 0
         * age : 18
         * group_name : 普通会员
         * nickname : 17801034237
         * mobile : 17801034237
         * avatar : http://apidebug.qlqwp2p.com/public/static/base/images/default.jpg
         * is_level_pwd : 0
         * sex : 保密
         * card_status : 0
         */
        private String location;
        private int company_status;
        private int age;
        private String group_name;
        private String nickname;
        private String mobile;
        private String avatar;
        private int is_level_pwd;
        private String sex;
        private int card_status;
        private int is_weixin;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getCompany_status() {
            return company_status;
        }

        public void setCompany_status(int company_status) {
            this.company_status = company_status;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getIs_level_pwd() {
            return is_level_pwd;
        }

        public void setIs_level_pwd(int is_level_pwd) {
            this.is_level_pwd = is_level_pwd;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getCard_status() {
            return card_status;
        }

        public void setCard_status(int card_status) {
            this.card_status = card_status;
        }

        public int getIs_weixin() {
            return is_weixin;
        }

        public void setIs_weixin(int is_weixin) {
            this.is_weixin = is_weixin;
        }
    }
}
