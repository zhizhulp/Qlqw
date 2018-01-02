package com.ascba.rebate.bean;

import java.util.List;

public class ScoreBuyType extends ScoreBuyBase {
    private List<ScoreBuyTypeC> types;

    public List<ScoreBuyTypeC> getTypes() {
        return types;
    }

    public void setTypes(List<ScoreBuyTypeC> types) {
        this.types = types;
    }

    @Override
    public int getItemType() {
        return 2;
    }

    public static class ScoreBuyTypeC{

        /**
         * cate_id : 1
         * cate_title : 名酒
         * cate_img : 1
         */

        private int cate_id;
        private String cate_title;
        private String cate_img;

        public int getCate_id() {
            return cate_id;
        }

        public void setCate_id(int cate_id) {
            this.cate_id = cate_id;
        }

        public String getCate_title() {
            return cate_title;
        }

        public void setCate_title(String cate_title) {
            this.cate_title = cate_title;
        }

        public String getCate_img() {
            return cate_img;
        }

        public void setCate_img(String cate_img) {
            this.cate_img = cate_img;
        }
    }
}
