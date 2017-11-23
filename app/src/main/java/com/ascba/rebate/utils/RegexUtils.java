package com.ascba.rebate.utils;

import java.util.regex.Pattern;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : 正则相关工具类
 * </pre>
 */
public class RegexUtils {
    //正则：手机号（简单）
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    //正则：身份证号码15位
    private static final String REGEX_IDCARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    //正则：身份证号码18位
    private static final String REGEX_IDCARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    //正则：汉字
    private static final String REGEX_CHZ = "^[\\u4e00-\\u9fa5]+$";
    //正则：用户名，取值范围为中文a-z,A-Z,0-9,"_",汉字
    private static final String REGEX_USERNAME = "^[a-zA-Z0-9\\u4E00-\\u9FA5_]+$";

    /**
     * 验证身份证号码15位
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(String string) {
        return isMatch(REGEX_IDCARD15, string);
    }

    /**
     * 验证身份证号码18位
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(String string) {
        return isMatch(REGEX_IDCARD18, string);
    }

    /**
     * 验证汉字
     *
     * @param string 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isChz(String string) {
        return isMatch(REGEX_CHZ, string);
    }


    /**
     * string是否匹配regex
     *
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, String string) {
        return Pattern.matches(regex, string);
    }

    /**
     * 验证手机号是否合法
     */
    public static boolean isMobilePhoneNum(String telNum) {
        return isMatch(REGEX_MOBILE_SIMPLE, telNum);
    }

    /**
     *  验证用户名称
     */
    public static boolean isUserName(String string) {
        return isMatch(REGEX_USERNAME, string);
    }


    public static void main(String[] args) {
        System.out.print(RegexUtils.isUserName("上岛咖啡卡,了的asdas1231_454")+"\n");
    }

}