package com.ascba.rebate.bean;

/**
 * Created by Jero on 2017/9/18 0018.
 */

public class MsgEntity {

    /**
     * class_id : 26
     * title : 系统消息
     * pic : http://www.qlqw.com/public/uploads/product/2017-06-20/5948ceab32b3a.png
     * messageType : undefined
     * is_read : 1
     * messageTitle : 钱来钱往App系统升级官方公告
     * create_time : 03/31
     */

    private int class_id;
    private String title;
    private String pic;
    private String messageType;
    private int is_read;
    private String messageTitle;
    private String create_time;

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
