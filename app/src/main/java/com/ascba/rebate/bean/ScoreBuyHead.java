package com.ascba.rebate.bean;


import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class ScoreBuyHead extends ScoreBuyBase {
    private List<ScoreBuyGrid> grids;

    public List<ScoreBuyGrid> getGrids() {
        return grids;
    }

    public void setGrids(List<ScoreBuyGrid> grids) {
        this.grids = grids;
    }

    public static class ScoreBuyGrid{
        @JSONField(name = "purchase_cate_img")
        private String url;
        @JSONField(name = "purchase_cate_title")
        private String title;
        @JSONField(name = "purchase_cate_id")
        private int id;
        private int purchase_status;

        public ScoreBuyGrid() {
        }

        public ScoreBuyGrid(String url, String title) {
            this.url = url;
            this.title = title;
        }

        public int getPurchase_status() {
            return purchase_status;
        }

        public void setPurchase_status(int purchase_status) {
            this.purchase_status = purchase_status;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
    @Override
    public int getItemType() {
        return 1;
    }

}
