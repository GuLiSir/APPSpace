package com.testdemo.entity;

import android.util.Log;

import com.testdemo.absPkg.Body;
import com.testdemo.absPkg.Circle;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 屏幕箱世界,处理在屏幕中的各种参数
 */
public class ScreenBox {
    private final Set<Body> bodySet = new HashSet<>();

    public boolean add(Body body) {
        return bodySet.add(body);
    }

    public boolean remove(Object o) {
        return bodySet.remove(o);
    }

    private Thread thread;
    private boolean isRunning = true;

    /**
     * 开启物理运动
     */
    public void bigBang() {
        isRunning = true;
        thread = new Thread(runnable);
        thread.start();
    }

    public void destory() {
        thread.interrupt();
        isRunning = false;
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            lastNanoTime = System.currentTimeMillis();
            while (isRunning) {
                freshParam();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private long lastNanoTime = 0;

    /**
     * 刷新各个参数,处理:物体的加速度,速度,位置,碰撞
     */
    private void freshParam() {
        long time = System.currentTimeMillis();
        //与上一次计算的时间差
        long timeSpaceNano = time - lastNanoTime;

        //新的加速度的计算,以及位置的计算
        for (Body body : bodySet) {
            float acceleratedSpeedX = body.getAcceleratedSpeedX();
            float acceleratedSpeedY = body.getAcceleratedSpeedY();
            float speedX = body.getSpeedX();
            float speedY = body.getSpeedY();
            body.setSpeedX(speedX + (acceleratedSpeedX * timeSpaceNano));
            body.setSpeedY(speedY + (acceleratedSpeedY * timeSpaceNano));

            //这里位移计算,直接跟速度时间相关,忽略加速度因素
            float v = body.getX() + body.getSpeedX() * timeSpaceNano/1000.0f;//除以1000是将"像素/s"转为"像素/ms"
            if (body instanceof IconEntity) {
                Log.i(TAG, "freshParam: "+((IconEntity) body).number);
            }
            Log.i(TAG, String.format("freshParam: x:%.2f speedX:%.2f timeSpace:%d now:%.2f",body.getX() ,  body.getSpeedX(),timeSpaceNano,v));
            body.setX(v);
            body.setY(body.getY() + body.getSpeedY() * timeSpaceNano/1000.0f);
        }


        //碰撞检测
        for (Body body : bodySet) {
            for (Body body1 : bodySet) {
                if (body != body1) {
                    //碰撞检测,如果有碰撞的话,让物体各自处理碰撞逻辑
                    if (body.isCollision(body1) || body1.isCollision(body)) {
                        body.onCollision(body1);
                        body1.onCollision(body);
                    }
                }
            }
        }


        lastNanoTime = System.currentTimeMillis();
    }

    private static final String TAG = "ScreenBox";

}