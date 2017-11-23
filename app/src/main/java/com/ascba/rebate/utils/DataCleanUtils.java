package com.ascba.rebate.utils;

/*  * 文 件 名:  DataCleanManager.java  
 * * 描    述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录  
 * */

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.math.BigDecimal;

/**
 * 本应用数据清除管理器
 */
public class DataCleanUtils {

    private static File[] filePathList = {};

    /**
     * * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * *
     *
     * @param context
     */
    public static void cleanInternalCache(Context context) {
        deleteFolderFile(context.getCacheDir(), true);
    }

    /**
     * * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * *
     *
     * @param context
     */
    public static void cleanDatabases(Context context) {
        deleteFolderFile(new File("/data/data/" + context.getPackageName() + "/databases"), true);
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) *
     *
     * @param context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFolderFile(new File("/data/data/" + context.getPackageName() + "/shared_prefs"), true);
    }

    /**
     * * 按名字清除本应用数据库 * *
     *
     * @param context
     * @param dbName
     */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }

    /**
     * * 清除/data/data/com.xxx.xxx/files下的内容 * *
     *
     * @param context
     */
    public static void cleanFiles(Context context) {
        deleteFolderFile(context.getFilesDir(), true);
    }

    // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
    // 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() -->
    // SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache)
     *
     * @param context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFolderFile(context.getExternalCacheDir(), true);
        }
    }

    /**
     * * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * *
     *
     * @param filePath
     */
    public static void cleanCustomCache(String filePath) {
        deleteFolderFile(new File(filePath), true);
    }

    public static void cleanCustomCache(File filePath) {
        deleteFolderFile(filePath, true);
    }

    /**
     * * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * *
     *
     * @param directory
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @return
     */
    public static void deleteFolderFile(File filePath, boolean deleteThisPath) {
        if (filePath != null) {
            try {
                if (filePath.isDirectory()) {// 如果下面还有文件
                    File files[] = filePath.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i], true);
                    }
                }
                if (deleteThisPath) {
                    if (!filePath.isDirectory()) {// 如果是文件，删除
                        filePath.delete();
                    } else {// 目录
                        if (filePath.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            filePath.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            deleteFolderFile(file, true);
        }
    }

    // 获取文件大小
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        if (!file.exists()) {
            return size;
        }
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }


    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        if (size == 0) {
            return "0K";
        }
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "T";
    }

    public static String getCacheSize(File file) throws Exception {
        return getFormatSize(getFolderSize(file));
    }

    /**
     * 获取缓存大小
     *
     * @param context
     */
    public static String getApplicationDataSize(Context context) {
        long size = 0;
        try {
            size += getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                size += getFolderSize(context.getExternalCacheDir());
            }
            size += getFolderSize(new File("/data/data/"
                    + context.getPackageName() + "/databases"));
            size += getFolderSize(new File("/data/data/"
                    + context.getPackageName() + "/app_webview"));
//             size += getFolderSize(new File("/data/data/"
//                     + context.getPackageName() + "/shared_prefs"));
            size += getFolderSize(context.getFilesDir());
            if (filePathList == null) {
                return getFormatSize(size);
            }
            for (File filePath : filePathList) {
                size += getFolderSize(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getFormatSize(size);
    }

    /**
     * * 清除本应用所有的数据 * *
     *
     * @param context
     */
    public static void cleanApplicationData(Context context) {
        try {
            cleanInternalCache(context);
            cleanExternalCache(context);
            cleanDatabases(context);
            cleanCustomCache(new File("/data/data/"
                    + context.getPackageName() + "/app_webview"));
            // cleanSharedPreference(context);
            cleanFiles(context);
            if (filePathList == null) {
                return;
            }
            for (File filePath : filePathList) {
                cleanCustomCache(filePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}