package com.ascba.rebate.bean;

/**
 * Created by Jero on 2017/9/18 0018.
 */

public class MsgListEntity {

    /**
     * message_id : 102
     * title : 钱来钱往测试7
     * intro : 钱来钱往测试7
     * create_time : 09-15 11:46
     * is_read : 1
     * address : http://www.qlqw.com/message/details?messageType=undefined&message_id=102
     */

    private int message_id;
    private String title;
    private String intro;
    private String create_time;
    private int is_read;
    private String address;

    public int getMessage_id() {
        return message_id;
    }

    public void setMessage_id(int message_id) {
        this.message_id = message_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
