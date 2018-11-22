package com.testdemo.utils;

/**
 * 圆形工具
 */
public class CircleUtils {
    /**
     * 获取两个点之间的距离
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return
     */
    public static double pointDistance(int x1, int y1, int x2, int y2) {
        //勾股定理求圆心距离
        double sqrt = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return sqrt;
    }

}
