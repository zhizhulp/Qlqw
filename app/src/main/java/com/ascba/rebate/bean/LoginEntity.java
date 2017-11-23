package com.ascba.rebate.bean;

/**
 * Created by lenovo on 2017/8/21.
 */

public class LoginEntity {


    /**
     * avatar : http://img.qlqwshop.com/public/uploads/avatar/85/85.png?1500887955
     * verify : 123456
     * update_token : {"accessToken":"4cd606bf7c2e044a2134bab972031266","sessionId":"6c9f00c30609585c1bc0163d621cb19e"}
     * expiring_in : 1800
     */

    private String avatar;
    private String verify;
    private UpdateTokenBean update_token;
    private int expiring_in;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public UpdateTokenBean getUpdate_token() {
        return update_token;
    }

    public void setUpdate_token(UpdateTokenBean update_token) {
        this.update_token = update_token;
    }

    public int getExpiring_in() {
        return expiring_in;
    }

    public void setExpiring_in(int expiring_in) {
        this.expiring_in = expiring_in;
    }

    public static class UpdateTokenBean {
        /**
         * accessToken : 4cd606bf7c2e044a2134bab972031266
         * sessionId : 6c9f00c30609585c1bc0163d621cb19e
         */

        private String accessToken;
        private String sessionId;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}
