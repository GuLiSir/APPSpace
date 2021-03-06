package com.testdemo.entity;

import com.testdemo.absPkg.Accelerator;
import com.testdemo.absPkg.Body;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseBody implements Body {

    //x轴速度
    private float speedX = 0;
    //y轴速度
    private float speedY = 0;
    //x轴加速度
//    private float acceleratedSpeedX = 0;
    //y轴加速度
//    private float acceleratedSpeedY = 0;

    private float x = 0;
    private float y = 0;
    private float z = 0;

    /**
     * 所受加速度的集合
     */
    private final List<Accelerator> acceleratorList = new ArrayList<>();


    @Override
    public void addAccelerator(Accelerator accelerator) {
        acceleratorList.add(accelerator);
    }

    @Override
    public float getSpeedX() {
//        if (speedX > 0) {
//            speedX--;
//        } else if (speedX < 0) {
//            speedX++;
//        }
        return speedX;
    }

    @Override
    public float getSpeedY() {
//        if (speedY > 0) {
//            speedY--;
//        } else if (speedY    < 0) {
//            speedY++;
//        }
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
        float result = 0f;
        for (Accelerator accelerator : acceleratorList) {
            result += accelerator.acceleratedX(this);
        }
        return result;
    }

    @Override
    public float getAcceleratedSpeedY() {
        float result = 0f;
        for (Accelerator accelerator : acceleratorList) {
            result += accelerator.acceleratedY(this);
        }
        return result;
    }


}
