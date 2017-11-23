package com.ascba.rebate.bean;

/**
 * Created by Jero on 2017/10/16 0016.
 */

public class SellerGiveEntity {

    /**
     * give_min_config : 1000
     * give_max_config : 1000000000
     * store_gift_score : 0
     * store_gift_status : 0
     * store_gift_status_text : 礼品不足，是否立即储值礼品?
     */

    private int give_min_config;
    private int give_max_config;
    private int store_gift_score;
    private int store_gift_status;
    private String store_gift_status_text;

    public int getGive_min_config() {
        return give_min_config;
    }

    public void setGive_min_config(int give_min_config) {
        this.give_min_config = give_min_config;
    }

    public int getGive_max_config() {
        return give_max_config;
    }

    public void setGive_max_config(int give_max_config) {
        this.give_max_config = give_max_config;
    }

    public int getStore_gift_score() {
        return store_gift_score;
    }

    public void setStore_gift_score(int store_gift_score) {
        this.store_gift_score = store_gift_score;
    }

    public int getStore_gift_status() {
        return store_gift_status;
    }

    public void setStore_gift_status(int store_gift_status) {
        this.store_gift_status = store_gift_status;
    }

    public String getStore_gift_status_text() {
        return store_gift_status_text;
    }

    public void setStore_gift_status_text(String store_gift_status_text) {
        this.store_gift_status_text = store_gift_status_text;
    }
}
