package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by Jero on 2017/10/12 0012.
 */

public class SellerEntity {

    /**
     * money : 125.00
     * server_tip : 敬请期待...
     * server : [{"server_title":"礼品赠送","server_desc":"礼品多多，相送就送","server_img":"http://apidebug.qlqwp2p.com/public/static/app/images/1.jpg","server_url":"http://apidebug.qlqwp2p.com/mallStoreGift/giveindex","server_status":1},{"server_title":"收钱码贴纸","server_desc":"防水二维码贴纸","server_img":"http://apidebug.qlqwp2p.com/public/static/app/images/2.jpg","server_url":"http://www.baidu.com","server_status":2},{"server_title":"语音提示首款到账","server_desc":"顾客多生意忙，不看手机也能快速知道钱是否到账","server_img":"http://www.baidu.com","server_status":1},{"server_title":"分店管理","server_desc":"一账户多店，轻松运营","server_img":"http://apidebug.qlqwp2p.com/public/static/app/images/2.jpg","server_url":null,"server_status":1},{"server_title":"电源通","server_desc":"店员也能确认首款是否到账啦！","server_img":"http://apidebug.qlqwp2p.com/public/static/app/images/2.jpg","server_url":null,"server_status":1}]
     */

    private String money;
    private String server_tip;
    private List<ServerBean> server;
    private int company_status;
    private String seller_perfect_url;
    private int member_status;
    private String compayn_status_text;

    public String getCompayn_status_text() {
        return compayn_status_text;
    }

    public void setCompayn_status_text(String compayn_status_text) {
        this.compayn_status_text = compayn_status_text;
    }

    public int getMember_status() {
        return member_status;
    }

    public void setMember_status(int member_status) {
        this.member_status = member_status;
    }

    public int getCompany_status() {
        return company_status;
    }

    public void setCompany_status(int company_status) {
        this.company_status = company_status;
    }

    public String getSeller_perfect_url() {
        return seller_perfect_url;
    }

    public void setSeller_perfect_url(String seller_perfect_url) {
        this.seller_perfect_url = seller_perfect_url;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getServer_tip() {
        return server_tip;
    }

    public void setServer_tip(String server_tip) {
        this.server_tip = server_tip;
    }

    public List<ServerBean> getServer() {
        return server;
    }

    public void setServer(List<ServerBean> server) {
        this.server = server;
    }

    public static class ServerBean {
        /**
         * server_title : 礼品赠送
         * server_desc : 礼品多多，相送就送
         * server_img : http://apidebug.qlqwp2p.com/public/static/app/images/1.jpg
         * server_url : http://apidebug.qlqwp2p.com/mallStoreGift/giveindex
         * server_status : 1
         */

        private String server_title;
        private String server_desc;
        private String server_img;
        private String server_url;
        private int server_status;

        public ServerBean() {
        }

        public ServerBean(String server_title, String server_desc, String server_img, String server_url, int server_status) {
            this.server_title = server_title;
            this.server_desc = server_desc;
            this.server_img = server_img;
            this.server_url = server_url;
            this.server_status = server_status;
        }

        public String getServer_title() {
            return server_title;
        }

        public void setServer_title(String server_title) {
            this.server_title = server_title;
        }

        public String getServer_desc() {
            return server_desc;
        }

        public void setServer_desc(String server_desc) {
            this.server_desc = server_desc;
        }

        public String getServer_img() {
            return server_img;
        }

        public void setServer_img(String server_img) {
            this.server_img = server_img;
        }

        public String getServer_url() {
            return server_url;
        }

        public void setServer_url(String server_url) {
            this.server_url = server_url;
        }

        public int getServer_status() {
            return server_status;
        }

        public void setServer_status(int server_status) {
            this.server_status = server_status;
        }
    }
}
