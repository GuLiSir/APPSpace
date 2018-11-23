package com.testdemo.utils;

import android.graphics.Point;

import com.testdemo.absPkg.Circle;

/**
 * 圆形工具
 */
public class CircleUtils {
    /**
     * 获取两个点之间的距离
     *
     * @param x1 点1的x轴
     * @param y1 点1的y轴
     * @param x2 点2的x轴
     * @param y2 点2的y轴
     * @return 勾股定理求得的距离
     */
    public static double pointDistance(float x1, float y1, float x2, float y2) {
        //勾股定理求圆心距离
        double sqrt = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return sqrt;
    }

    /**
     * 判断一个点是否在一个圆内(包含边界情况)
     *
     * @param circle 圆
     * @param point  点
     * @return true为在圆内
     */
    public static boolean pointInCircle(Circle circle, Point point) {
        double v = pointToCircleDistance(circle, point);
        return v <= circle.getRadius();
    }

    /**
     * 点到圆心的距离
     *
     * @param circle
     * @param point
     * @return
     */
    public static double pointToCircleDistance(Circle circle, Point point) {
        return pointDistance(circle.getX(), circle.getY(), point.x, point.y);
    }

    /**
     * 求两圆之间的距离
     */
    public static double CircleBetweenCircleDistance(Circle circle1, Circle circle2) {
        return pointDistance(circle1.getX(), circle1.getY(), circle2.getX(), circle2.getY());
    }

    public static boolean CircleInCircle(Circle circle1, Circle circle2) {
        double v = CircleBetweenCircleDistance(circle1, circle2);
        //圆心距离小于总半径之和则碰撞
        return v <= (circle1.getRadius() + circle2.getRadius());
    }
}
