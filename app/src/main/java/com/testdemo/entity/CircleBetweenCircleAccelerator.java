package com.testdemo.entity;

import android.util.Log;

import com.testdemo.absPkg.Accelerator;
import com.testdemo.absPkg.AttachInitiator;
import com.testdemo.utils.CircleUtils;

import java.util.Set;

/**
 * 球与球之间的加速度器,两球之间距离越近,斥力越大,距离越远,引力越大,设置的距离值附近,则趋近与0
 */
public class CircleBetweenCircleAccelerator implements Accelerator {
    //当前的球体
    private final IconEntity iconEntity;
    //所有球体
    private final Set<IconEntity> iconEntitySet;

    public CircleBetweenCircleAccelerator(IconEntity iconEntity, Set<IconEntity> iconEntitySet) {
        this.iconEntity = iconEntity;
        this.iconEntitySet = iconEntitySet;
    }

    private int distance = 200;

    //      公式:y=(x/4)^3,其函数图像找网页在线工具绘制 https://zh.numberempire.com/graphingcalculator.php
    @Override
    public float acceleratedX() {
        int xResult = 0;
        for (IconEntity entity : iconEntitySet) {
            //过滤与自身对比计算
            if (entity != iconEntity) {
                double v = CircleUtils.CircleBetweenCircleDistance(iconEntity, entity);
//                xResult += Math.pow((v / 4 - distance), 3);
//                ((x-100)/20)^2
                double pow = Math.pow((v - distance) / 40f, 2);

                if (v <= distance) {
                    pow = -pow;
                }
                xResult += pow;
            }

        }
        if (xResult > 0) {
            xResult = Math.min(xResult, 15);
        } else if (xResult < 0) {
            xResult = Math.max(xResult, -15);
        }
        Log.i(TAG, "acceleratedX: " + xResult);
        return xResult;
    }

    private static final String TAG = "CircleBetweenCircleAcce";

    @Override
    public float acceleratedY() {
        int xResult = 0;
        for (IconEntity entity : iconEntitySet) {
            //过滤与自身对比计算
            if (entity != iconEntity) {
                double v = CircleUtils.CircleBetweenCircleDistance(iconEntity, entity);
//                xResult += Math.pow((v / 4 - distance), 3);
//                ((x-100)/20)^2
                double pow = Math.pow((v - distance) / 40f, 2);

                if (v <= distance) {
                    pow = -pow;
                }
                xResult += pow;
            }

        }
        if (xResult > 0) {
            xResult = Math.min(xResult, 15);
        } else if (xResult < 0) {
            xResult = Math.max(xResult, -15);
        }
        Log.i(TAG, "acceleratedY: " + xResult);
        return xResult;
    }
}
