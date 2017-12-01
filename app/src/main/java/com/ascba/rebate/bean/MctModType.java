package com.ascba.rebate.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李平 on 2017/11/30 17:39
 * Describe: 商户修改界面实体类
 */

public class MctModType implements Parcelable {

    private int type;
    private String title;
    private String rules;
    private String hint;
    private String content;
    private int requestCode;

    public MctModType() {
    }

    public MctModType(int type, String title, String rules, String hint) {
        this.type = type;
        this.title = title;
        this.rules = rules;
        this.hint = hint;
    }

    public MctModType(int type, String title, String rules, String hint, String content, int requestCode) {
        this(type, title, rules, hint);
        this.requestCode = requestCode;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeInt(this.requestCode);
        dest.writeString(this.title);
        dest.writeString(this.rules);
        dest.writeString(this.hint);
        dest.writeString(this.content);
    }

    private MctModType(Parcel in) {
        this.type = in.readInt();
        this.requestCode = in.readInt();
        this.title = in.readString();
        this.rules = in.readString();
        this.hint = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<MctModType> CREATOR = new Parcelable.Creator<MctModType>() {
        @Override
        public MctModType createFromParcel(Parcel source) {
            return new MctModType(source);
        }

        @Override
        public MctModType[] newArray(int size) {
            return new MctModType[size];
        }
    };
}
