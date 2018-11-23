package com.testdemo.entity;

import com.testdemo.absPkg.Body;
import com.testdemo.absPkg.Circle;

/**
 * 左边框碰撞检测
 */
public class BottomFrameBody extends BaseFrameBody {

    private int measuredHeight;

    public BottomFrameBody(int measuredHeight) {
        this.measuredHeight = measuredHeight;
    }

    @Override
    public boolean isCollision(Body body) {
        if (body instanceof Circle) {
            return body.getY() + ((Circle) body).getRadius() >= measuredHeight;
        }
        return false;
    }

    @Override
    public void onCollision(Body body1) {

    }
}
