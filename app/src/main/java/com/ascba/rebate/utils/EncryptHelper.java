package com.ascba.rebate.utils;

import java.security.MessageDigest;

/**
 * 加密
 */
public class EncryptHelper {
    /**
     * 使用MD5对字符串进行加密操作，返回加密后的字符串信息
     * @param orinalString 加密前字符串
     * @return 加密后字符串
     */
    public static String md5Encode(String orinalString) {
        //创建消息摘要实例，MD5表示生成消息摘要的算法名称
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("md5");
            byte[] digest = messageDigest.digest(orinalString.getBytes("UTF-8"));
            //用16进制显示生成的消息摘要
            StringBuilder result = new StringBuilder();
            for (byte b : digest) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
