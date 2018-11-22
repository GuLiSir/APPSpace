package com.testdemo.entity;

import android.graphics.Rect;
import android.util.Log;

import com.testdemo.IconShowActivity;
import com.testdemo.absPkg.AttachInitiator;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IconEntity implements AttachInitiator {

    private int x = 0;
    private int y = 0;
    private int z = 0;
    //图标半径
    public int radius = 100;
    //所连接的item对象
    private Set<AttachInitiator> attachEntity = new HashSet<>();
    //x轴速度
    private int xSpeed = 0;
    //y轴速度
    private int ySpeed = 0;
    //界面中所有元素的集合
    private final Set<IconEntity> allIconEntitySet = new HashSet<>();
    private Rect rectParent;
    public final int number;

    public IconEntity(int number) {
        this.number = number;
        Random random = new Random();
        //随机速度方向
        while (xSpeed == 0) {
            xSpeed = random.nextInt(200) - 100;
        }
        while (ySpeed == 0) {
            ySpeed = random.nextInt(200) - 100;
        }
    }

    /**
     * 请求绑定指定的item
     *
     * @param recipient
     */
    @Override
    public void requestAttach(AttachInitiator recipient) {
        //1.已绑定的不许再次绑定 2.不能绑定自己
        if (recipient == this) {
            throw new IllegalArgumentException();
        }
        if (!attachEntity.contains(recipient)) {
            attachEntity.add(recipient);
            recipient.onAttachRequest(this, 0);
        }
    }

    /**
     * 请求解绑指定的item
     *
     * @param recipient
     */
    @Override
    public void requestDetach(AttachInitiator recipient) {
        if (attachEntity.contains(recipient)) {
            attachEntity.remove(recipient);
            recipient.onDetachRequest(this);
        }
    }


    /**
     * 发起连接请求
     *
     * @param initiator
     * @param distance
     */
    @Override
    public void onAttachRequest(AttachInitiator initiator, int distance) {
        attachEntity.add(initiator);
    }

    /**
     * 断开连接请求
     *
     * @param initiator
     */
    @Override
    public void onDetachRequest(AttachInitiator initiator) {
        attachEntity.remove(initiator);
    }


    private final Rect rect = new Rect();

    /**
     * 获取该图标的矩形边框
     */
    public Rect getRect() {
        return rect;
    }


    public void setX(int x) {
        this.x = x;
        refreshRect();
    }

    public void setY(int y) {
        this.y = y;
        refreshRect();
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

//    /**
//     * 手指移动事件的设置x坐标
//     */
//    public void moveEventX(int x) {
//        setX(x);
//    }


    /**
     * 刷新所在的矩形区域信息
     */
    private void refreshRect() {
        rect.left = x - radius;
        rect.right = x + radius;
        rect.top = y - radius;
        rect.bottom = y + radius;
    }

    public Set<AttachInitiator> getAttachEntity() {
        return attachEntity;
    }

    /**
     * 设置在界面中所有的元素集合
     */
    public void setAllEntitySet(Set<IconEntity> iconEntitySet) {
        allIconEntitySet.addAll(iconEntitySet);
    }

    private long lastTime = System.currentTimeMillis();

    /**
     * 重新计算位置
     */
    public void fresh() {
        //球与球之间的碰撞检测
        boolean b = IconShowActivity.collisionDetection(this, allIconEntitySet);
        if (b) {
            //检测到碰撞了,直接速度取反
            xSpeed = -xSpeed;
            ySpeed = -ySpeed;
        } else {

        }

        //边缘碰撞检测
        //左边缘
        if (getX() <= rectParent.left + radius) {
            xSpeed = Math.abs(xSpeed);
        }
        //右边缘
        if (getX() >= rectParent.right - radius) {
            xSpeed = -Math.abs(xSpeed);
        }
        //上边缘
        if (getY() <= rectParent.top + radius) {
            ySpeed = Math.abs(ySpeed);
        }
        //下边缘
        if (getY() >= rectParent.bottom - radius) {
            ySpeed = -Math.abs(ySpeed);
        }

        long l = System.currentTimeMillis();
        //与上次的时间差
        long timeSpace = l - lastTime;

        //被暂停了,就不会移动
        if (!stop) {
            setX((int) (getX() + xSpeed / 1000.0f * timeSpace));
            setY((int) (getY() + ySpeed / 1000.0f * timeSpace));
        } else {
            Log.i(TAG, "fresh: stop");
        }

        lastTime = System.currentTimeMillis();
    }

    public void setRange(Rect rectParent) {
        this.rectParent = rectParent;
    }

    private boolean stop = false;

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    private static final String TAG = "IconEntity";
}
