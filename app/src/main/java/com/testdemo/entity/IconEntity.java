package com.testdemo.entity;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
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


    public IconEntity(int number) {
        this.number = number;
        //随机速度方向
        while (getSpeedX() == 0) {
            setSpeedX(random.nextInt(50) - 25);
        }
        while (getSpeedY() == 0) {
            setSpeedY(random.nextInt(50) - 25);
        }

        setSpeedX(0);
        setSpeedY(0);

        //绘制椭圆
        RectF rectF = new RectF(200, 200, 1700, 900);
        //第一种方法绘制椭圆
        path.addOval(rectF, Path.Direction.CW);
        pathMeasure.setPath(path, false);

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

    Path path = new Path();
    PathMeasure pathMeasure = new PathMeasure(path, false);
    private float[] pos = new float[2];
    private float[] tan = new float[2];

    long time1 = System.currentTimeMillis();


    @Override
    public float getX() {

        long dis = System.currentTimeMillis() - time1;
        final long time = 36000;
        long l = dis % time;
        float v = 1 - l / (float) time;
        int startP = number*number / 7;
        float v1 = (startP + v);

        float length = pathMeasure.getLength();
        pathMeasure.getPosTan(length *v1, pos, tan);
        return pos[0];
//        return super.getX();
    }

    @Override
    public float getY() {
        long dis = System.currentTimeMillis() - time1;
        final long time = 36000;
        long l = dis % time;
        float v = 1 - l / (float) time;
        int startP = number*number / 7;
        float v1 = (startP + v);

        float length = pathMeasure.getLength();
        pathMeasure.getPosTan(length *v1, pos, tan);
        return pos[1];
//        return super.getY();
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


    //暂停移动
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
