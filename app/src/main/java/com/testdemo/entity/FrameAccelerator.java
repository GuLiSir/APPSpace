package com.testdemo.entity;

import android.graphics.Rect;
import android.util.Log;

import com.testdemo.absPkg.Accelerator;
import com.testdemo.absPkg.Body;

/**
 * 边框加速度,离边框越近,所产生的反加速度越大
 */
public class FrameAccelerator implements Accelerator {
    private final Rect rectFrame;

    //左边和上边的百分比处
    private static final float deivcesPercent1 = 1f / 4.0f;
    //右边和下边的百分比处
    private static final float deivcesPercent2 = 3f / 4.0f;

    public FrameAccelerator(Rect rectFrame) {
        this.rectFrame = rectFrame;
    }

    @Override
    public float acceleratedX(Body body) {
        int width = rectFrame.width();
        float leftF = width * deivcesPercent1;
        float rightF = width * deivcesPercent2;
        float result = 0.0f;
        if (body instanceof IconEntity) {
            IconEntity tag = (IconEntity) body;
            float x = tag.getX();
            if (x <= leftF) {
                //小球的位置稍微靠左了,产生向右的加速度,使其远离边框
                result = (-0.5f * (x - leftF) + 40);
            } else if (x >= rightF) {
                //小球的位置稍微靠右了,产生向左的加速度,使其远离边框
                result = -(0.5f * (x - rightF));
            }
            Log.i(TAG, String.format("acceleratedX: l:%.2f r:%.2f x:%.2f result:%.2f", leftF, rightF, x, result));
        }

        return result / 1000.0f;
    }

    private static final String TAG = "FrameAccelerator";

    @Override
    public float acceleratedY(Body body) {
        int height = rectFrame.height();
        float topF = height * deivcesPercent1;
        float bottomF = height * deivcesPercent2;
        float result = 0.0f;
        if (body instanceof IconEntity) {
            IconEntity tag = (IconEntity) body;
            float y = tag.getY();
            if (y <= topF) {
                //小球的位置稍微靠左了,产生向右的加速度,使其远离边框
                result = (-0.7f * (y - topF) + 40);
            } else if (y >= bottomF) {
                //小球的位置稍微靠右了,产生向左的加速度,使其远离边框
                result = -(0.7f * (y - bottomF));
            }
            Log.i(TAG, String.format("acceleratedX: l:%.2f r:%.2f y:%.2f result:%.2f", topF, bottomF, y, result));
        }

        return result / 1000.0f;
    }

}
