package com.testdemo.absPkg;

/**
 * 加速器,处理加速度的问题,这里的加速度,可理解为力
 */
public interface Accelerator {

    /**
     * @return 某一物体对自身在x轴上的加速度,单位:像素/s
     * @param body 某一物体
     */
    float acceleratedX(Body body);

    /**
     * @return 某一物体对自身在y轴上的加速度,单位:像素/s
     * @param body 某一物体
     */
    float acceleratedY(Body body);

}