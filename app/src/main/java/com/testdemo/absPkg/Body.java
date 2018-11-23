package com.testdemo.absPkg;

/**
 * 物体
 */
public interface Body {

    void setAcceleratedSpeedX(float acceleratedSpeedX);

    void setAcceleratedSpeedY(float acceleratedSpeedY);

    void setX(float x);

    void setY(float y);

    void setZ(float z);

    /**
     * @return x轴位置, 相对于屏幕的坐标系
     */
    float getX();

    /**
     * @return y轴位置, 相对于屏幕的坐标系
     */
    float getY();

    float getZ();

    float getAcceleratedSpeedX();

    float getAcceleratedSpeedY();


    /**
     * @return x轴速度,像素/s
     */
    float getSpeedX();

    float getSpeedY();


    void setSpeedX(float speedX);

    void setSpeedY(float speedY);

    /**
     * 是否与该物体碰撞了,这里的碰撞检测,是有可能出现重合的情况,不一定是刚刚好碰撞,所以可以将重合视为碰撞
     * @return true碰撞了,false没有碰撞
     */
    boolean isCollision(Body body);

    /**
     * 与该物体碰撞时的行为,这里就只处理自己的碰撞后续动作,这个方法会被调用,取决于A,B两个实例isCollision方法是否返回true
     * @param body1 与其碰撞的物体
     */
    void onCollision(Body body1);

}