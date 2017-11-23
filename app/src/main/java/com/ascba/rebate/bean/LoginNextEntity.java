package com.ascba.rebate.bean;

/**
 * 登录接口数据模型类
 */

public class LoginNextEntity {

    /**
     * status : 2
     * update_token : {"session_id":"7a8cd9e37d41bbdab8293e32c37fc112","access_token":"ebbc673c2bbd690ea736e189aad68a76"}
     * user_info : {"avatar":"http://img.qlqwshop.com/public/uploads/avatar/85/85.png?1500887955","mobile":"15510115653","nickname":"疯狂牛仔衬衫","sex":1,"age":25,"location":"未设置","card_status":1,"company_status":0,"is_level_pwd":1}
     */

    private UpdateTokenBean update_token;
    private UserInfoBean user_info;
    private int status;

    public UpdateTokenBean getUpdate_token() {
        return update_token;
    }

    public void setUpdate_token(UpdateTokenBean update_token) {
        this.update_token = update_token;
    }

    public UserInfoBean getUser_info() {
        return user_info;
    }

    public void setUser_info(UserInfoBean user_info) {
        this.user_info = user_info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class UpdateTokenBean {
        /**
         * session_id : 7a8cd9e37d41bbdab8293e32c37fc112
         * access_token : ebbc673c2bbd690ea736e189aad68a76
         */

        private String session_id;
        private String access_token;

        public String getSession_id() {
            return session_id;
        }

        public void setSession_id(String session_id) {
            this.session_id = session_id;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }

    public static class UserInfoBean {

        /**
         * avatar : http://img.qlqwshop.com/public/uploads/avatar/85/85.png?1500887955
         * mobile : 15510115653
         * nickname : 疯狂牛仔衬衫
         * sex : 男
         * age : 25
         * location : 未设置
         * card_status : 1
         * company_status : 1
         * "realname": "聂国富"
         * group_name : 中级合伙人
         * is_level_pwd : 1
         * "cardid": "220284199008120614"
         */

        private String avatar;
        private String mobile;
        private String nickname;
        private String sex;
        private int age;
        private String location;
        private int card_status;
        private int company_status;
        private String group_name;
        private int is_level_pwd;
        private String cardid;
        private String realname;

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getCardid() {
            return cardid;
        }

        public void setCardid(String cardid) {
            this.cardid = cardid;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public int getCard_status() {
            return card_status;
        }

        public void setCard_status(int card_status) {
            this.card_status = card_status;
        }

        public int getCompany_status() {
            return company_status;
        }

        public void setCompany_status(int company_status) {
            this.company_status = company_status;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public int getIs_level_pwd() {
            return is_level_pwd;
        }

        public void setIs_level_pwd(int is_level_pwd) {
            this.is_level_pwd = is_level_pwd;
        }
    }
}
