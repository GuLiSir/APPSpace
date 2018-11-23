package com.testdemo.entity;

import com.testdemo.absPkg.Body;

/**
 * 边框实体,用来做边界碰撞
 */
public abstract class BaseFrameBody implements Body {
    @Override
    public void setAcceleratedSpeedX(float acceleratedSpeedX) {

    }

    @Override
    public void setAcceleratedSpeedY(float acceleratedSpeedY) {

    }

    @Override
    public void setX(float x) {

    }

    @Override
    public void setY(float y) {

    }

    @Override
    public void setZ(float z) {

    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public float getZ() {
        return 0;
    }

    @Override
    public float getAcceleratedSpeedX() {
        return 0;
    }

    @Override
    public float getAcceleratedSpeedY() {
        return 0;
    }

    @Override
    public float getSpeedX() {
        return 0;
    }

    @Override
    public float getSpeedY() {
        return 0;
    }

    @Override
    public void setSpeedX(float speedX) {

    }

    @Override
    public void setSpeedY(float speedY) {

    }
}
