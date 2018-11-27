package com.testdemo.entity;

import android.util.Log;

import com.testdemo.absPkg.Accelerator;
import com.testdemo.absPkg.Body;
import com.testdemo.absPkg.Circle;
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
    public float acceleratedX(Body body1) {
        float xResult = 0;
        for (IconEntity entity : iconEntitySet) {
            IconEntity body = entity;
            if (body instanceof Circle && entity != iconEntity) {
                Circle circle = (Circle) body;
                double v = CircleUtils.CircleBetweenCircleDistance(iconEntity, circle);
                //拉力大小
                double a1 = (v - distance) * 2;
                float x1 = iconEntity.getX() - body.getX();
                double x2 = x1 * a1;
                xResult = (float) x2;
                if (v > distance) {

                    //距离过远,产生互相靠拢的拉力
                    if (iconEntity.getX() < body.getX()) {
                        assert x2 > 0;
                    }
                }
            }
        }
        return 000;
    }

    private static final String TAG = "CircleBetweenCircleAcce";

    @Override
    public float acceleratedY(Body body1) {
        float yResult = 0;
        for (IconEntity entity : iconEntitySet) {
            IconEntity body = entity;
            if (body instanceof Circle && entity != iconEntity) {
                Circle circle = (Circle) body;
                double v = CircleUtils.CircleBetweenCircleDistance(iconEntity, circle);
                //拉力大小
                double a1 = (v - distance) * 2;
                float x1 = iconEntity.getY() - body.getY();
                double x2 = x1 * a1;
                yResult = (float) x2;
            }
        }

        return 000;
    }
}
