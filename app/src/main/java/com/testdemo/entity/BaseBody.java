package com.testdemo.entity;

import com.testdemo.absPkg.Body;

public abstract class BaseBody implements Body {

    //x轴速度
    private float speedX = 0;
    //y轴速度
    private float speedY = 0;
    //x轴加速度
    private float acceleratedSpeedX = 0;
    //y轴加速度
    private float acceleratedSpeedY = 0;

    private float x = 0;
    private float y = 0;
    private float z = 0;

    @Override
    public float getSpeedX() {
        return speedX;
    }

    @Override
    public float getSpeedY() {
        return speedY;
    }

    @Override
    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    @Override
    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }



    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public void setZ(float z) {
        this.z = z;
    }


    @Override
    public float getAcceleratedSpeedX() {
        return acceleratedSpeedX;
    }

    @Override
    public float getAcceleratedSpeedY() {
        return acceleratedSpeedY;
    }

    @Override
    public void setAcceleratedSpeedX(float acceleratedSpeedX) {
        this.acceleratedSpeedX = acceleratedSpeedX;
    }

    @Override
    public void setAcceleratedSpeedY(float acceleratedSpeedY) {
        this.acceleratedSpeedY = acceleratedSpeedY;
    }

}
