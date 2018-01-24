package com.ascba.rebate.utils;

import android.util.Log;
import android.util.TimeUtils;

import com.ascba.rebate.appconfig.AppConfig;
import com.megvii.livenesslib.util.Screen;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

/**
 * url加密
 */

public class EncodeUtils {
    private static String TAG = "EncodeUtils";
    private static final String salt = "qlqw46c229d744bc3a013332aff722d32c23";

    public static String makeNonceStr() {
        int length = (int) Math.round((Math.random() + 8) * 4);
        String KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (KeyString.length() - 1))));
        }
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public static String makeSign(String nonceStr, String url) {
        TreeMap map = new TreeMap();
        map.put("nonce_str", nonceStr);//3
        map.put("timestamp", getCurrentTime());//4
        map.put("key", "qlqw46c229d7zjf4bc3a813332aff792d32c23");//2
        map.put("api_url", url);//1
        StringBuilder sb = new StringBuilder();
        for (Object o : map.keySet()) {
            sb.append(map.get(o));
        }
        Log.d(TAG, "makeSign: " + sb.toString());
        String secret = EncryptHelper.md5Encode(sb.toString());
        if (secret == null) {
            return null;
        } else {
            Log.d(TAG, "makeSign: " + secret.toUpperCase());
            return secret.toUpperCase();
        }
    }

    public static String[] makeSignHead(String nonceStr, String url) {
        String[] ss = new String[2];
        TreeMap map = new TreeMap();
        map.put("nonce_str", nonceStr);//3
        long time = getCurrentTime();
        map.put("timestamp", time);//4
        map.put("key", "qlqw46c229d7zjf4bc3a813332aff792d32c23");//2
        map.put("api_url", url);//1
        StringBuilder sb = new StringBuilder();
        for (Object o : map.keySet()) {
            sb.append(map.get(o));
        }
        Log.d(TAG, "makeSign: " + sb.toString());
        String secret = EncryptHelper.md5Encode(sb.toString());
        Log.d(TAG, "makeSign: " + secret.toUpperCase());
        ss[0] = time + "";
        ss[1] = secret.toUpperCase();
        return ss;
    }

    public static String encryptIdentityCode(String code, String secret_key) {
        String originStr = new String(MD5(code + secret_key)).toLowerCase();
        String[] strs = new String[2];
        int length = originStr.length();
        strs[0] = originStr.substring(0, length / 2);
        strs[1] = originStr.substring(length / 2, length);
        String newStr = strs[1] + strs[0];
        return getSha1(newStr);
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getSha1(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 设置密码加密
     */
    public static String encryptPsd(String psd) {
        return encryptMD5(psd);
    }

    /**
     * 1.生成第一次md5加密：明文密码+qlqw46c229d744bc3a013332aff722d32c23
     * <p>
     * 2.md5值乱序：在第一次生成的md5值进行均等分成两个串进行颠倒，生成一个新的串
     * <p>
     * 3.二次md5加密：乱序后的串再次进行md5加密
     *
     * @param data——明文
     */
    private static String encryptMD5(String data) {
        //1、第一次加密
        String firstEncry = getMD5Str(data + salt);
        //2、乱序
        String arg0 = firstEncry.substring(0, firstEncry.length() / 2);
        String arg1 = firstEncry.substring(firstEncry.length() / 2, firstEncry.length());
        firstEncry = arg1 + arg0;
        //3、二次md5加密
        firstEncry = getMD5Str(firstEncry);
        return firstEncry.toLowerCase();
    }

    /**
     * 支付密码验证
     */
    public static String getPayPsd(String psd) {
        String encryptPsd = encryptPsd(psd);
        Log.d(TAG, "origin: " + encryptPsd);
        long time = getCurrentTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String format = simpleDateFormat.format(time);//2015年5月8号 7点16分
        Log.d(TAG, "getPayPsd: " + format);
        try {
            long time1 = simpleDateFormat.parse(format).getTime() / 1000;
            Log.d(TAG, "long_time: " + time1);
            encryptPsd = getMD5Str(encryptPsd + time1);
            Log.d(TAG, "final_psd: " + encryptPsd.toLowerCase());
            return encryptPsd.toLowerCase();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    private static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }

    public static long getCurrentTime() {
        long theTime = System.currentTimeMillis() / 1000 + AppConfig.getInstance().getLong("time_diff", 0);
        Log.d(TAG, "getCurrentTime: " + theTime);
        return theTime;
    }
}
