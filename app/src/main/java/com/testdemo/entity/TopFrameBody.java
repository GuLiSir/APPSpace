package com.testdemo.entity;

import com.testdemo.absPkg.Body;
import com.testdemo.absPkg.Circle;

/**
 * 左边框碰撞检测
 */
public class TopFrameBody extends BaseFrameBody {

    @Override
    public boolean isCollision(Body body) {
        if (body instanceof Circle) {
            return body.getY() - ((Circle) body).getRadius() <= 0;
        }
        return false;
    }

    @Override
    public void onCollision(Body body1) {

    }
}
