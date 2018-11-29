package com.testdemo.absPkg;

import android.graphics.Canvas;
import android.graphics.PointF;

/**
 * 每一项
 */
public interface ItemEntity {
    /**
     * 绘制
     *
     * @param canvas
     */
    void onDraw(Canvas canvas);

    /**
     * 与上一次的时间差
     *
     * @param lastDuration
     */
    void refreshParam(long lastDuration);

    /**
     * 是否按到了自身
     *
     * @param point
     * @return
     */
    boolean touchIn(PointF point);

    void onTouch(boolean touch);

    void touchOffset(float offsetX, float offsetY);

    /**
     * 是否与该物体碰撞(重叠,有交点)了
     *
     * @return true碰撞了(有交点), false没有碰撞(无交点)
     */
    boolean isCollision(ItemEntity body);

    boolean setX(float x);
    boolean setY(float y);

    float getX();
    float getY();

}
