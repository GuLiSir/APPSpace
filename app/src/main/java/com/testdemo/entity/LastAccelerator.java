package com.testdemo.entity;

import com.testdemo.absPkg.Accelerator;
import com.testdemo.absPkg.Body;

/**
 * 跟着最后一个跑
 */
public class LastAccelerator implements Accelerator {

    @Override
    public float acceleratedX(Body body) {
        if (body instanceof IconEntity) {

        }
        return 0;
    }

    @Override
    public float acceleratedY(Body body) {
        return 0;
    }




}