package com.ascba.rebate.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/6/14.
 * 数字格式化
 */

public class NumberFormatUtils {
    private static DecimalFormat point2 = new DecimalFormat("##0.00");

    public static String getNewDouble(double oriDouble) {
        return point2.format(oriDouble);
    }

    public static String getNewFloat(float oriDouble) {
        return point2.format(oriDouble);
    }
}
