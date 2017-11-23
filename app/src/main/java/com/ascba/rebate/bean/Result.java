package com.ascba.rebate.bean;

/**
 * Created by 李平 on 2017/8/11.
 * app实体类
 */

public class Result<T> {
    private int code; // 状态码
    private T data; // 结果。
    private String msg; // 提示信息。
    /**
     * version_upgrade : {"android_type":1,"ios_type":1}
     */

    private VersionUpgrade version_upgrade;


    public Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Result(int code, T data, String msg, VersionUpgrade version_upgrade) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.version_upgrade = version_upgrade;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public VersionUpgrade getVersion_upgrade() {
        return version_upgrade;
    }

    public void setVersion_upgrade(VersionUpgrade version_upgrade) {
        this.version_upgrade = version_upgrade;
    }

    public static class VersionUpgrade {
        /**
         * android_type : 1
         * ios_type : 1
         */

        private int android_type;
        private int ios_type;
        /**
         * version_ios : 2.3
         * version_android : 3.1.0
         * version_update : 2016-12-02
         * version_remarks : 测试APP接口测试APP接口测试APP接口测试APP接口测试APP接口测试APP接口测试APP接口测试APP接口
         */

        private String version_ios;
        private String version_android;
        private String version_update;
        private String version_remarks;
        private String version_url;

        public String getVersion_url() {
            return version_url;
        }

        public void setVersion_url(String version_url) {
            this.version_url = version_url;
        }

        public int getAndroid_type() {
            return android_type;
        }

        public void setAndroid_type(int android_type) {
            this.android_type = android_type;
        }

        public int getIos_type() {
            return ios_type;
        }

        public void setIos_type(int ios_type) {
            this.ios_type = ios_type;
        }

        public String getVersion_ios() {
            return version_ios;
        }

        public void setVersion_ios(String version_ios) {
            this.version_ios = version_ios;
        }

        public String getVersion_android() {
            return version_android;
        }

        public void setVersion_android(String version_android) {
            this.version_android = version_android;
        }

        public String getVersion_update() {
            return version_update;
        }

        public void setVersion_update(String version_update) {
            this.version_update = version_update;
        }

        public String getVersion_remarks() {
            return version_remarks;
        }

        public void setVersion_remarks(String version_remarks) {
            this.version_remarks = version_remarks;
        }
    }
}
