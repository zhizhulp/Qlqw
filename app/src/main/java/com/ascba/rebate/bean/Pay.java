package com.ascba.rebate.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 李平 on 2017/9/15 12:01
 * Describe: 阿里支付信息
 */

public class Pay {

    /**
     * payInfo : alipay_sdk=alipay-sdk-php-20161101&app_id=2016091801917680&biz_content=%7B++++%22timeout_express%22%3A%2290m%22%2C++++%22seller_id%22%3A%222088421801134208%22%2C++++%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C++++%22total_amount%22%3A1.00%2C++++%22subject%22%3A%22%E5%85%85%E5%80%BC%22%2C++++%22body%22%3A%225027%22%2C++++%22out_trade_no%22%3A%2220170914165239555356%22%2C++++%22passback_params%22%3A%220%22%2C++%7D&charset=utf-8&format=json&method=alipay.trade.app.pay&notify_url=http%253A%252F%252Fapidebug.qlqwp2p.com%252FalipayNotify.html&sign_type=RSA2&timestamp=2017-09-14+16%3A52%3A39&version=1.0&sign=fE8EXpS1w5OTovx1GAfaqUjdGL3hEinslsAuZRELrlsbm5TH%2BucxLiI6dYGJEk1rhYZg4NGgJgplHn0S27eNuEnNSSPP8evZe1sjX25x3N%2BqNZuJQEUdph7w5Y3KptO8BSebJO6Z6TLalH%2Bii4Hs6FISKLMvYLeXAzE9bhQwPrM%3D
     * out_trade_no : 20170914165239555356
     */

    private String aliPayInfo;
    private String out_trade_no;
    private WXPay wxPayInfo;
    private String success_info;
    private int member_status;

    public String getSuccess_info() {
        return success_info;
    }

    public void setSuccess_info(String success_info) {
        this.success_info = success_info;
    }

    public WXPay getWxPayInfo() {
        return wxPayInfo;
    }

    public void setWxPayInfo(WXPay wxPayInfo) {
        this.wxPayInfo = wxPayInfo;
    }

    public String getAliPayInfo() {
        return aliPayInfo;
    }

    public void setAliPayInfo(String aliPayInfo) {
        this.aliPayInfo = aliPayInfo;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getMember_status() {
        return member_status;
    }

    public void setMember_status(int member_status) {
        this.member_status = member_status;
    }

    public static class WXPay{
        private String appid;
        private String noncestr;
        @SerializedName("package")
        private String _package;
        private String partnerid;
        private String prepayid;
        private long timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String get_package() {
            return _package;
        }

        public void set_package(String _package) {
            this._package = _package;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
