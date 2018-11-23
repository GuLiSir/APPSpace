package com.testdemo.entity;

import android.graphics.Rect;
import android.util.Log;

import com.testdemo.IconShowActivity;
import com.testdemo.absPkg.Accelerator;
import com.testdemo.absPkg.AttachInitiator;
import com.testdemo.absPkg.Body;
import com.testdemo.absPkg.Circle;
import com.testdemo.utils.CircleUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class IconEntity extends BaseBody implements AttachInitiator, Circle {

    //图标半径
    public float radius = 100;
    //所连接的item对象
    private Set<AttachInitiator> attachEntity = new HashSet<>();

    //界面中所有元素的集合
    private final Set<IconEntity> allIconEntitySet = new HashSet<>();
    public final int number;
    private Random random = new Random();
    /**
     * 所受加速度的集合
     */
    public final List<Accelerator> acceleratorList = new ArrayList<>();

    public IconEntity(int number) {
        this.number = number;
        //随机速度方向
        while (getSpeedX() == 0) {
            setSpeedX(random.nextInt(200) - 100);
        }
        while (getSpeedY() == 0) {
            setSpeedY(random.nextInt(200) - 100);
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


    @Override
    public void setX(float x) {
        if (!stop) {
            super.setX(x);
        }
    }

    @Override
    public void setY(float y) {
        if (!stop) {
            super.setY(y);
        }
    }

    /**
     * 设置绝对位置
     */
    public void setXReal(float x) {
        super.setX(x);
    }


    /**
     * 设置绝对位置
     */
    public void setYReal(float y) {
        super.setY(y);
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


    private boolean stop = false;

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    private static final String TAG = "IconEntity";

    /**
     * @return 该圆的半径
     */
    @Override
    public float getRadius() {
        return radius;
    }


    @Override
    public boolean isCollision(Body body) {
        if (body instanceof Circle) {
            boolean b = CircleUtils.CircleInCircle(this, (Circle) body);
            return b;
        }
        return false;
    }

    @Override
    public void onCollision(Body body1) {
        if (body1 instanceof IconEntity) {
            //圆与圆相撞
            float x = body1.getX();
            if (x >= getX()) {
                //对方在自己的右边,自己往左边移动
                setSpeedX(-Math.abs(getSpeedX()));
            } else {
                setSpeedX(Math.abs(getSpeedX()));
            }
            float y = body1.getY();
            if (y >= getY()) {
                //对方在自己的下边,自己往上边移动
                setSpeedY(-Math.abs(getSpeedY()));
            } else {
                setSpeedY(Math.abs(getSpeedY()));
            }
        } else if (body1 instanceof LeftFrameBody) {
            //左边框相撞
            setSpeedX(Math.abs(getSpeedX()));
        } else if (body1 instanceof TopFrameBody) {
            //顶部相撞
            setSpeedY(Math.abs(getSpeedY()));
        } else if (body1 instanceof RightFrameBody) {
            setSpeedX(-Math.abs(getSpeedX()));
        } else if (body1 instanceof BottomFrameBody) {
            setSpeedY(-Math.abs(getSpeedY()));
        }
    }
}
