package com.testdemo.absPkg;

import android.graphics.Canvas;
import android.graphics.Point;

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
    boolean touchIn(Point point);

    void onTouch(boolean touch);

    void touchOffset(float offsetX, float offsetY);

    /**
     * 是否与该物体碰撞了,这里的碰撞检测,是有可能出现重合的情况,不一定是刚刚好碰撞,所以可以将重合视为碰撞
     *
     * @return true碰撞了, false没有碰撞
     */
    boolean isCollision(ItemEntity body);

    void setX(float x);
    void setY(float y);

    float getX();
    float getY();

}
