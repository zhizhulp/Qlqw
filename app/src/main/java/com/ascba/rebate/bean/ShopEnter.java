package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by Jero on 2018/1/25 0025.
 */

public class ShopEnter {
    /**
     * tip_status : 0
     * tip_text : 未个人认证，是否前往认证?
     * store_settled_h5 : http://www.baidu.com
     * store_settled_h5_title : 商城入驻协议
     * pattern : [{"pattern_id":1,"pattern_title":"普通售卖模式","pattern_img":"https://www.baidu.com/img/bd_logo1.png"},{"pattern_id":2,"pattern_title":"赠送售卖模式","pattern_img":"https://www.baidu.com/img/bd_logo1.png"},{"pattern_id":3,"pattern_title":"拼单售卖模式","pattern_img":"https://www.baidu.com/img/bd_logo1.png"},{"pattern_id":4,"pattern_title":"佣金售卖模式","pattern_img":"https://www.baidu.com/img/bd_logo1.png"},{"pattern_id":5,"pattern_title":"快反售卖模式","pattern_img":"https://www.baidu.com/img/bd_logo1.png"}]
     * interests : [{"interests_title":"现金收益","interests_tip":"立即到账","interests_index":[{"interests_value":"推荐商家入驻奖励10%"},{"interests_value":"推荐代理加盟奖励10%"}]},{"interests_title":"积分收益","interests_tip":"立即到账","interests_index":[{"interests_value":"每笔销售额50%的礼品奖励"},{"interests_value":"每笔销售额50%的礼品奖励"}]}]
     */

    private int tip_status;
    private String tip_text;
    private String store_settled_h5;
    private String store_settled_h5_title;
    private List<PatternBean> pattern;
    private List<InterestsBean> interests;

    public int getTip_status() {
        return tip_status;
    }

    public void setTip_status(int tip_status) {
        this.tip_status = tip_status;
    }

    public String getTip_text() {
        return tip_text;
    }

    public void setTip_text(String tip_text) {
        this.tip_text = tip_text;
    }

    public String getStore_settled_h5() {
        return store_settled_h5;
    }

    public void setStore_settled_h5(String store_settled_h5) {
        this.store_settled_h5 = store_settled_h5;
    }

    public String getStore_settled_h5_title() {
        return store_settled_h5_title;
    }

    public void setStore_settled_h5_title(String store_settled_h5_title) {
        this.store_settled_h5_title = store_settled_h5_title;
    }

    public List<PatternBean> getPattern() {
        return pattern;
    }

    public void setPattern(List<PatternBean> pattern) {
        this.pattern = pattern;
    }

    public List<InterestsBean> getInterests() {
        return interests;
    }

    public void setInterests(List<InterestsBean> interests) {
        this.interests = interests;
    }

    public static class PatternBean {
        /**
         * pattern_id : 1
         * pattern_title : 普通售卖模式
         * pattern_img : https://www.baidu.com/img/bd_logo1.png
         */

        private int pattern_id;
        private String pattern_title;
        private String pattern_img;

        public int getPattern_id() {
            return pattern_id;
        }

        public void setPattern_id(int pattern_id) {
            this.pattern_id = pattern_id;
        }

        public String getPattern_title() {
            return pattern_title;
        }

        public void setPattern_title(String pattern_title) {
            this.pattern_title = pattern_title;
        }

        public String getPattern_img() {
            return pattern_img;
        }

        public void setPattern_img(String pattern_img) {
            this.pattern_img = pattern_img;
        }
    }

    public static class InterestsBean {
        /**
         * interests_title : 现金收益
         * interests_tip : 立即到账
         * interests_index : [{"interests_value":"推荐商家入驻奖励10%"},{"interests_value":"推荐代理加盟奖励10%"}]
         */

        private String interests_title;
        private String interests_tip;
        private List<InterestsIndexBean> interests_index;

        public String getInterests_title() {
            return interests_title;
        }

        public void setInterests_title(String interests_title) {
            this.interests_title = interests_title;
        }

        public String getInterests_tip() {
            return interests_tip;
        }

        public void setInterests_tip(String interests_tip) {
            this.interests_tip = interests_tip;
        }

        public List<InterestsIndexBean> getInterests_index() {
            return interests_index;
        }

        public void setInterests_index(List<InterestsIndexBean> interests_index) {
            this.interests_index = interests_index;
        }

        public static class InterestsIndexBean {
            /**
             * interests_value : 推荐商家入驻奖励10%
             */

            private String interests_value;

            public String getInterests_value() {
                return interests_value;
            }

            public void setInterests_value(String interests_value) {
                this.interests_value = interests_value;
            }
        }
    }
}
