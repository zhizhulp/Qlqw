package com.ascba.rebate.bean;

import java.util.List;

public class ScoreBuyBanner extends ScoreBuyBase {
    private List<ScoreBuyImg> imgs;

    public List<ScoreBuyImg> getImgs() {
        return imgs;
    }

    public void setImgs(List<ScoreBuyImg> imgs) {
        this.imgs = imgs;
    }

    public static class ScoreBuyImg{
        /**
         * banner_img : http://3demo.oss-cn-beijing.aliyuncs.com/public/static/app/images/2.jpg
         * banner_url : http://www.baidu.com
         * banner_status : 1
         */

        private String banner_img;
        private String banner_url;
        private int banner_status;



        public String getBanner_img() {
            return banner_img;
        }

        public void setBanner_img(String banner_img) {
            this.banner_img = banner_img;
        }

        public String getBanner_url() {
            return banner_url;
        }

        public void setBanner_url(String banner_url) {
            this.banner_url = banner_url;
        }

        public int getBanner_status() {
            return banner_status;
        }

        public void setBanner_status(int banner_status) {
            this.banner_status = banner_status;
        }
    }
    @Override
    public int getItemType() {
        return 0;
    }
}
