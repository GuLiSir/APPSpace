package com.testdemo.utils;

import android.graphics.PointF;

public class PointToLineDis {

    // 直线方程一般式 Ax + By + C = 0;

    /**
     * 求直线方程的一般式
     * 直线方程一般式 Ax + By + C = 0;
     * <p>
     * 输入直线l经过的两个点
     *
     * @return 数组, 第0位代表A, 1:B,2:C
     */
    public static double[] lineExp(float x1, float y1, float x2, float y2) {
        double[] result = new double[3];
        double A;
        double B;
        double C;

        /**
         * 由起始点和终止点构成的直线方程一般式的系数A
         */
        A = ((y1 - y2)
                /
                Math.sqrt(Math.pow((y1 - y2), 2) + Math.pow((x1 - x2), 2)));

        /**
         * 由起始点和终止点构成的直线方程一般式的系数
         */
        B = (x2 - x1)
                / Math.sqrt(Math.pow((y1 - y2), 2)
                + Math.pow((x1 - x2), 2));

        /**
         * 由起始点和终止点构成的直线方程一般式的系数
         */
        C = (x1 * y2 - y2 * y1)
                / Math.sqrt(Math.pow((y1 - y2), 2)
                + Math.pow((x1 - x2), 2));
        result[0] = A;
        result[1] = B;
        result[2] = C;
        return result;

    }

    /**
     * 点到直线方程的距离 此公式请参考点到直线距离通用公式
     * 直线方程一般式 Ax + By + C = 0;
     *
     * @param x
     * @param y
     * @return
     */
    public static double alLine(double x, double y, double A, double B, double C) {
        double d = ((A * (x) + B * (y) + C)) / Math.sqrt(Math.pow(A, 2) + Math.pow(B, 2));
        d = Math.abs(d);
        return d;
    }

}
