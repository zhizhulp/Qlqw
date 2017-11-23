package com.ascba.rebate.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.boredream.bdvideoplayer.bean.IVideoInfo;

import java.util.List;

/**
 * Created by 李平 on 2017/10/12 18:15
 * Describe:主页实体类
 */

public class HomeBean {

    private List<ModuleEntity> navList;
    private List<VideoBean> videoList;
    private List<BillBean> bill;
    /**
     * nav_activity : 1
     */

    private int nav_activity;

    public List<ModuleEntity> getNavList() {
        return navList;
    }

    public void setNavList(List<ModuleEntity> navList) {
        this.navList = navList;
    }

    public List<VideoBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoBean> videoList) {
        this.videoList = videoList;
    }

    public List<BillBean> getBill() {
        return bill;
    }

    public void setBill(List<BillBean> bill) {
        this.bill = bill;
    }

    public int getNav_activity() {
        return nav_activity;
    }

    public void setNav_activity(int nav_activity) {
        this.nav_activity = nav_activity;
    }

    public static class VideoBean implements IVideoInfo, Parcelable {
        /**
         * thumb : http://www.qlqw.com/public/uploads/product/2017-06-01/592fc2004bdb9.png
         * video_url : http://video.qlqwshop.com/nianhui.mp4
         */

        private String thumb;
        private String video_url;


        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        @Override
        public String getVideoTitle() {
            return "正在播放...";
        }

        @Override
        public String getVideoPath() {
            return video_url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.thumb);
            dest.writeString(this.video_url);
        }

        public VideoBean() {
        }

        protected VideoBean(Parcel in) {
            this.thumb = in.readString();
            this.video_url = in.readString();
        }

        public static final Parcelable.Creator<VideoBean> CREATOR = new Parcelable.Creator<VideoBean>() {
            @Override
            public VideoBean createFromParcel(Parcel source) {
                return new VideoBean(source);
            }

            @Override
            public VideoBean[] newArray(int size) {
                return new VideoBean[size];
            }
        };
    }

    public static class BillBean {
        /**
         * money : 0.01
         * remark : 充值-支付宝
         * type : 充值
         * time : 17:29
         * title : 支付助手
         * icon : http://www.qlqw.com/public/uploads/indexPic/payment.png
         */
        @JSONField(name = "money",alternateNames = "score")
        private String money;
        private String remark;
        private String type;
        private String time;
        private String title;
        private String icon;
        private String billType;
        private String billMore;


        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getBillType() {
            return billType;
        }

        public void setBillType(String billType) {
            this.billType = billType;
        }

        public String getBillMore() {
            return billMore;
        }

        public void setBillMore(String billMore) {
            this.billMore = billMore;
        }


        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
