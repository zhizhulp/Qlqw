package com.ascba.rebate.bean;

/**
 * Created by Jero on 2017/9/26 0026.
 */

public class ExchangeEntity {

    /**
     * order_id : 14
     * create_time : 2017-09-26
     * goods_num : 1
     * score_price : 40积分
     * goods_title : 韩国 chic复古百褶喇...
     * goods_img : http://www.qlqw.com/public/uploads/product/2017-09-20/59c2309652801.jpg
     * is_virtual : 0
     * status : 1
     * status_tip : 发货状态：已发货
     * status_text : 订单状态：已发货
     * status_url : http://apidebug.qlqwp2p.com/getExpInfo?order_num=1000897097434
     */

    private int order_id;
    private String create_time;
    private int goods_num;
    private String score_price;
    private String goods_title;
    private String goods_img;
    private int is_virtual;
    private int status;
    private String status_tip;
    private String status_text;
    private String status_url;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public String getScore_price() {
        return score_price;
    }

    public void setScore_price(String score_price) {
        this.score_price = score_price;
    }

    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public int getIs_virtual() {
        return is_virtual;
    }

    public void setIs_virtual(int is_virtual) {
        this.is_virtual = is_virtual;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatus_tip() {
        return status_tip;
    }

    public void setStatus_tip(String status_tip) {
        this.status_tip = status_tip;
    }

    public String getStatus_text() {
        return status_text;
    }

    public void setStatus_text(String status_text) {
        this.status_text = status_text;
    }

    public String getStatus_url() {
        return status_url;
    }

    public void setStatus_url(String status_url) {
        this.status_url = status_url;
    }
}
