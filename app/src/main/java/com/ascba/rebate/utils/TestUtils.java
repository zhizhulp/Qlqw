package com.ascba.rebate.utils;


import java.util.Arrays;

/**
 * Created by 李平 on 2017/9/7.
 * 仅供测试使用
 */

public class TestUtils {
    private static String sortValue(double cf, double e3, double e4, double e5, double e6) {
        double[] ff = new double[]{Math.abs(cf - e3), Math.abs(cf - e4), Math.abs(cf - e5), Math.abs(cf - e6)};
        Arrays.sort(ff);
        for (double aFf : ff) {
            if (aFf == cf - e3) {
                return "1-e3";
            }else if(aFf == cf - e4){
                return "1-e4";
            }else if(aFf == cf - e5){
                return "1-e5";
            }else if(aFf == cf - e6){
                return "1-e6";
            }
        }
        return null;

    }

    public static void main(String[] args) {
        System.out.print(TestUtils.sortValue(82, 73, 56, 88, 78));
    }
}
