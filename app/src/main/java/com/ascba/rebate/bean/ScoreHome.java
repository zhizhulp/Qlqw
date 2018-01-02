package com.ascba.rebate.bean;

import java.util.List;

/**
 * Created by 李平 on 2017/9/21 9:56
 * Describe: 积分商城首页实体类
 */

public class ScoreHome {

    private List<GiftGoods> goods_list;
    private Member member;
    private String score;
    private int cattype;
    private String member_level;
    private int on_sale;

    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;
    }
    public List<GiftGoods> getGoods_list() {
        return goods_list;
    }
    public void setGoods_list(List<GiftGoods> goods_list) {
        this.goods_list = goods_list;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getCattype() {
        return cattype;
    }

    public void setCattype(int cattype) {
        this.cattype = cattype;
    }

    public String getMember_level() {
        return member_level;
    }

    public void setMember_level(String member_level) {
        this.member_level = member_level;
    }

    public int getOn_sale() {
        return on_sale;
    }

    public void setOn_sale(int on_sale) {
        this.on_sale = on_sale;
    }

    public static class Member {
        private String ranking;
        private int score;
        private String today_money;
        private String last_bill_time;
        private float least_settlement;
        private String need_money;
        private String member_level;
        private int on_sale;
        private int is_show;
        private String agreement_url;

        public String getAgreement_url() {
            return agreement_url;
        }

        public void setAgreement_url(String agreement_url) {
            this.agreement_url = agreement_url;
        }

        public float getLeast_settlement() {
            return least_settlement;
        }

        public void setLeast_settlement(float least_settlement) {
            this.least_settlement = least_settlement;
        }

        public String getRanking() {
            return ranking;
        }

        public void setRanking(String ranking) {
            this.ranking = ranking;
        }

        public String getMember_level() {
            return member_level;
        }

        public void setMember_level(String member_level) {
            this.member_level = member_level;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getToday_money() {
            return today_money;
        }

        public void setToday_money(String today_money) {
            this.today_money = today_money;
        }

        public String getLast_bill_time() {
            return last_bill_time;
        }

        public void setLast_bill_time(String last_bill_time) {
            this.last_bill_time = last_bill_time;
        }

        public int getOn_sale() {
            return on_sale;
        }

        public void setOn_sale(int on_sale) {
            this.on_sale = on_sale;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public String getNeed_money() {
            return need_money;
        }

        public void setNeed_money(String need_money) {
            this.need_money = need_money;
        }
    }


    public static class GiftGoods extends ScoreBuyBase {
        /**
         * id : 8
         * title : ILISYA 柔色纤长浓密睫毛膏可温水卸妆防水不易晕染卷翘大容量15g
         * type : 1
         * to_money : 29.00
         * score_price : 10
         * img : /public/uploads/product/2017-09-20/59c22ba9d31f9.jpg
         * is_virtual : 0
         * inventory : 2000
         */

        private int goods_id;
        private String title;
        private int type;
        private String to_money;//原价
        private String money;//加价
        private int score_price;
        private String price;
        private String img;
        private List<String> photos;
        private int is_virtual;
        private int inventory;
        private String description;
        private String html;
        private String url;
        private String freight_price;
        private int condition;
        private String score;//用户当前积分
        private int button_tip;
        private String button_text;
        private float voucher;
        private int is_money;//福利券商品是否允许余额补差价

        public int getIs_money() {
            return is_money;
        }

        public void setIs_money(int is_money) {
            this.is_money = is_money;
        }

        public float getVoucher() {
            return voucher;
        }

        public void setVoucher(float voucher) {
            this.voucher = voucher;
        }

        public String getMoney() {
            return money;
        }
        public void setMoney(String money) {
            this.money = money;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getButton_tip() {
            return button_tip;
        }

        public void setButton_tip(int button_tip) {
            this.button_tip = button_tip;
        }

        public String getButton_text() {
            return button_text;
        }

        public void setButton_text(String button_text) {
            this.button_text = button_text;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public List<String> getPhotos() {
            return photos;
        }

        public void setPhotos(List<String> photos) {
            this.photos = photos;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getHtml() {
            return html;
        }

        public void setHtml(String html) {
            this.html = html;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getFreight_price() {
            return freight_price;
        }

        public void setFreight_price(String freight_price) {
            this.freight_price = freight_price;
        }

        public int getCondition() {
            return condition;
        }

        public void setCondition(int condition) {
            this.condition = condition;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTo_money() {
            return to_money;
        }

        public void setTo_money(String to_money) {
            this.to_money = to_money;
        }

        public int getScore_price() {
            return score_price;
        }

        public void setScore_price(int score_price) {
            this.score_price = score_price;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getIs_virtual() {
            return is_virtual;
        }

        public void setIs_virtual(int is_virtual) {
            this.is_virtual = is_virtual;
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        @Override
        public int getItemType() {
            return 3;
        }
    }
}
