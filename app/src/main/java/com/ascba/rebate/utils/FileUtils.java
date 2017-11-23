package com.ascba.rebate.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by 李平 on 2017/8/21.
 * 文件操作帮助类
 */

public class FileUtils {
    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath() {
//        File path = null;
//        if (sdCardIsAvailable()) {
        //        } else {
//            path = Environment.getDataDirectory();
//        }
        return Environment.getExternalStorageDirectory();
    }

    /**
     * SD卡是否可用.
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else{
            return false;
        }
    }
    /**
     * 获取 Android/data/com.nettec.qlqw/file目录
     */
    public static File getAppFile(Context context,String type) {
        return context.getExternalFilesDir(type);
    }
}
