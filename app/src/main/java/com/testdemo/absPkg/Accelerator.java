package com.testdemo.absPkg;

/**
 * 加速器,处理加速度的问题,这里的加速度,可理解为力
 */
public interface Accelerator {

    /**
     * @return x轴上的加速度,单位:像素/s
     */
    float acceleratedX();

    /**
     * @return y轴上的加速度,单位:像素/s
     */
    float acceleratedY();

}