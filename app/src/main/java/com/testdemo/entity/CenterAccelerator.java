package com.testdemo.entity;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.Log;

import com.testdemo.absPkg.Accelerator;
import com.testdemo.absPkg.Body;
import com.testdemo.utils.CircleUtils;

import java.util.Arrays;

/**
 * 中心加速度,使其围绕着中心运动,不让其远离中心
 */
public class CenterAccelerator implements Accelerator {

    private final float centerX;
    private final float centerY;


    Path path = new Path();
    PathMeasure pathMeasure = new PathMeasure(path, false);
    private float[] pos = new float[2];
    private float[] tan = new float[2];


    public CenterAccelerator(float centerX, float centerY) {
        this.centerX = centerX;
        this.centerY = centerY;

        //绘制椭圆
        RectF rectF = new RectF(200, 200, 1700, 900);
        //第一种方法绘制椭圆
        path.addOval(rectF, Path.Direction.CW);
        pathMeasure.setPath(path, false);

    }

    @Override
    public float acceleratedX(Body body) {
        float length = pathMeasure.getLength();
        //获取最小的百分比位置
        int percent = 0;
        double minDistance = -1;//最小距离,初始值
        for (int i = 0; i < 100; i++) {
            pathMeasure.getPosTan(length * (i / 100.0f), pos, tan);
            double v = CircleUtils.pointDistance(pos[0], pos[1], body.getX(), body.getY());
            if (minDistance == -1) {
                minDistance = v;
            } else {
                if (v < minDistance) {
                    percent = i;
                    minDistance = v;
                }
            }
        }
        //用最近的点来产生拉力
        pathMeasure.getPosTan(length * (percent / 100.0f), pos, tan);
        Log.i(TAG, "最近的点是: " + percent + "   pos:" + Arrays.toString(pos));
        float v = pos[0] - body.getX();


        return v * 0.05f;
    }

    private static final String TAG = "CenterAccelerator";

    @Override
    public float acceleratedY(Body body) {
        float length = pathMeasure.getLength();
        //获取最小的百分比位置
        int percent = 0;
        double minDistance = -1;//最小距离,初始值
        for (int i = 0; i < 100; i++) {
            pathMeasure.getPosTan(length * (i / 100.0f), pos, tan);
            double v = CircleUtils.pointDistance(pos[0], pos[1], body.getX(), body.getY());
            if (minDistance == -1) {
                minDistance = v;
            } else {
                if (v < minDistance) {
                    Log.i(TAG, "acceleratedY: p:" + percent + " dis:" + v);
                    percent = i;
                    minDistance = v;
                }
            }
        }
        //用最近的点来产生拉力
        pathMeasure.getPosTan(length * (percent / 100.0f), pos, tan);
        Log.i(TAG, "最近的点是: " + percent + "   pos:" + Arrays.toString(pos));
        float v = pos[1] - body.getY();


        return v * 0.05f;
    }
}
