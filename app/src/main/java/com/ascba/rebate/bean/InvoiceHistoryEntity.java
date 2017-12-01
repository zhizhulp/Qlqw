package com.ascba.rebate.bean;

/**
 * Created by Jero on 2017/11/30 0030.
 */

public class InvoiceHistoryEntity {
    /**
     * invoice_id : 16
     * total_fee : 201.6
     * create_time : 2017/11/29
     * member_info : 汪炳旭(个人)
     * status : 待开票
     * shipping : 待邮寄
     * wu_url : 0
     * shipping_url :
     */

    private int invoice_id;
    private double total_fee;
    private String create_time;
    private String member_info;
    private String status;
    private String shipping;
    private int wu_url;
    private String shipping_url;

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public double getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(double total_fee) {
        this.total_fee = total_fee;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getMember_info() {
        return member_info;
    }

    public void setMember_info(String member_info) {
        this.member_info = member_info;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(String shipping) {
        this.shipping = shipping;
    }

    public int getWu_url() {
        return wu_url;
    }

    public void setWu_url(int wu_url) {
        this.wu_url = wu_url;
    }

    public String getShipping_url() {
        return shipping_url;
    }

    public void setShipping_url(String shipping_url) {
        this.shipping_url = shipping_url;
    }
}