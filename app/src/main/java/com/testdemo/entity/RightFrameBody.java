package com.testdemo.entity;

import com.testdemo.absPkg.Body;
import com.testdemo.absPkg.Circle;

/**
 * 左边框碰撞检测
 */
public class RightFrameBody extends BaseFrameBody {

    private int measuredWidth;

    public RightFrameBody(int measuredWidth) {
        this.measuredWidth = measuredWidth;
    }

    @Override
    public boolean isCollision(Body body) {
        if (body instanceof Circle) {
            // TODO: 2018/11/23  
            return body.getX() + ((Circle) body).getRadius() >= measuredWidth;
        }
        return false;
    }

    @Override
    public void onCollision(Body body1) {

    }
}
