package com.testdemo.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;

import com.testdemo.absPkg.AttachInitiator;
import com.testdemo.absPkg.ItemEntity;
import com.testdemo.utils.CircleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class EllipseItemEntity implements ItemEntity, AttachInitiator {
    /**
     * 上一个节点
     */
    private EllipseItemEntity lastNode;
    /**
     * 下一个节点
     */
    private EllipseItemEntity nextNode;

    private float x;
    private float y;

    private float tagX;
    private float tagY;
    //目标百分比
    private float tagPercent;
    private float currentPercent;
    //该值仅用于调试使用
    private int currentPercentIndex;

    private float radius = 80;
    //是否触摸住了自己
    private boolean touchIng;

    private final int number;
    /**
     * 允许该物体活动的范围
     */
    private final Rect rectParent;


    public EllipseItemEntity(int number, Rect rectParent) {
        this.number = number;
        // TODO: 2018/11/28  7改
        tagPercent = (1.0f / 7) * number;
        currentPercent = tagPercent;
//        currentPercentIndex = (int) tagPercent;
        this.rectParent = rectParent;


    }

    private static Path path = new Path();
    //预先分割的粒度
    public static final int accuracy = 100;
    private static PathMeasure pathMeasure = new PathMeasure(path, false);
    //    private static float[][] percentAll = new float[accuracy][2];
    public static final SparseArray<PointF> pointFSparseArray = new SparseArray<>();

    static {

        //绘制椭圆
        RectF rectF = new RectF(200, 200, 1700, 900);
        //第一种方法绘制椭圆
        path.addOval(rectF, Path.Direction.CW);
        pathMeasure.setPath(path, false);
        float length = pathMeasure.getLength();
        float[] floats = new float[2];
        for (int i = 0; i < accuracy; i++) {
            float percent = i / (float) accuracy;
            pathMeasure.getPosTan(length * percent, floats, null);
//            percentAll[i] = floats;
            PointF pointF = new PointF(floats[0], floats[1]);
            pointFSparseArray.put(i, pointF);
        }
    }

    private float[] pos = new float[2];
    private float[] tan = new float[2];


    private final Paint paint = new Paint();
    private final Paint paint2 = new Paint();

    {
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(2);
        paint.setAntiAlias(true);
        paint2.setColor(Color.BLUE);
        paint2.setColor(Color.YELLOW);//重合调试
        paint2.setTextSize(40);
        paint2.setAntiAlias(true);
    }


    @Override
    public void onDraw(Canvas canvas) {
        //绘制连接线
        for (AttachInitiator attachInitiator : attachEntity) {
            // TODO: 2018/11/28 强转类型可优化
            EllipseItemEntity ellipseItemEntity = (EllipseItemEntity) attachInitiator;
            canvas.drawLine(this.x, this.y, ellipseItemEntity.getX(), ellipseItemEntity.getY(), paint2);
        }

        //绘制调试用的指示点
        PointF pointF = pointFSparseArray.get(currentPercentIndex);
        canvas.drawCircle(pointF.x, pointF.y, 10, paint2);

        //绘制圆
        canvas.drawCircle(this.x, this.y, radius, paint);

        //绘制调试用的文本
        String format = String.format(Locale.getDefault(), ",%.2f", currentPercent);
        float v = paint2.measureText(format);
        canvas.drawText(String.valueOf(number) + format, this.x - v / 2.0f, this.y, paint2);

    }

    /**
     * 取得真实的百分比,某些情况,百分比为120%,实际上就是20%,或者-20%,实际上就是80%;此方法就是完成换算,得到真实的百分比
     *
     * @return 0.0f-1.0f
     */
    private float realPercent(float percent) {
        if (percent >= 0) {
            return percent % 1;
        } else {
            return 1f - Math.abs(percent % 1);
        }
    }

    @Override
    public void refreshParam(long lastDuration) {
        float v = lastDuration / (100 * 1000f);
        float tagPercent1 = tagPercent + -v + moveOffsetPercent;
        float newPercent = realPercent(tagPercent1);
        if (tagPercent1 <= 0) {
            Log.i(TAG, "refreshParam: tagPercent1:" + tagPercent1 + "   realPercent:" + newPercent);
        }

        moveOffsetPercent = 0;
//        逆时针移动
        tagPercent = newPercent;


        //-------------百分比移动的速率矫正,------
        /*说明根据上一个结点和下一个结点来判断百分比自增的速率是否要加大,一般情况下是均分的;
        如果跟上一个结点百分比差值过小(低于均值),则移动速度稍微变慢,使其不再接近,
        跟上一个结点距离过远(高于均值),则移动速率稍微加大,加快靠近*/
        final float NextPercentDistant;
        if (nextNode.currentPercent > this.currentPercent) {
            NextPercentDistant = nextNode.currentPercent - this.currentPercent;
        } else if (nextNode.currentPercent < this.currentPercent) {
            NextPercentDistant = nextNode.currentPercent + 1 - this.currentPercent;
        } else {
            NextPercentDistant = 0;
        }

        final float lastPercentDistant;
        if (lastNode.currentPercent < this.currentPercent) {
            lastPercentDistant = this.currentPercent - lastNode.currentPercent;
        } else if (lastNode.currentPercent > this.currentPercent) {
            lastPercentDistant = this.currentPercent - lastNode.currentPercent + 1;
        } else {
            lastPercentDistant = 0;
        }


        Log.i(TAG, "refreshParam: 距离,next:" + (NextPercentDistant - 1 / 7f) + "  last:" + lastPercentDistant);
        tagPercent = tagPercent + (NextPercentDistant - 1 / 7f) * 0.005f - (lastPercentDistant - 1 / 7f) * 0.005f;
        //-------------百分比移动的速率矫正,结束------


        float length = pathMeasure.getLength();
        pathMeasure.getPosTan(length * tagPercent, pos, tan);
        this.tagX = pos[0];
        this.tagY = pos[1];
        //???? 需要封装多一层
        currentPercentIndex = findNearestPercent();

        this.currentPercent = currentPercentIndex / (float) accuracy;

        if (touchIng) {
            //正在触摸,需要刷新位置,刷新的位置为最接近的百分比之间
            tagPercent = this.currentPercent;
        } else {
            float speedX = 0;
            float speedY = 0;
            //下面数字常量代表向目标移动的速度
            speedX = (this.tagX - this.x) * 0.0055f;
            speedY = (this.tagY - this.y) * 0.0055f;

            setY(this.y + speedY * lastDuration);
            setX(this.x + speedX * lastDuration);
        }


        /*
         * 结点判断,判断是否有越过结点的行为,如果有越过结点,则进行结点移除插入工作:
         * 在原来的位置移除结点,然后插入到当前百分比最近的位置相邻的两个结点
         */
        Collections.sort(itemEntities, comparator);

        //链表的连接,构成环链表
        for (EllipseItemEntity itemEntity : itemEntities) {
            Log.i(TAG, "refreshParam: 排序后结果:" + itemEntity.currentPercent);
            itemEntity.nextNode = null;
            itemEntity.lastNode = null;
        }

        // TODO: 2018/11/28 优化点,跟activity的连接结点代码重复了
        for (int i = 0; i < itemEntities.size(); i++) {
            EllipseItemEntity ellipseItemEntity = itemEntities.get(i);
            if (i != itemEntities.size() - 1) {
                ellipseItemEntity.LinkToNextNode(itemEntities.get(i + 1));
            } else {
                ellipseItemEntity.LinkToNextNode(itemEntities.get(0));
            }
        }

    }

    //根据当前所在的百分比排序
    private final Comparator<EllipseItemEntity> comparator = new Comparator<EllipseItemEntity>() {
        @Override
        public int compare(EllipseItemEntity o1, EllipseItemEntity o2) {
            if (o1.currentPercent == o2.currentPercent) {
                return 0;
            }
            return o1.currentPercent > o2.currentPercent ? 1 : -1;
        }
    };

    // TODO: 2018/11/28 刷新结点行为可每次只刷新一次,移出外面去
    private final List<EllipseItemEntity> itemEntities = new ArrayList<>();

    /**
     * 查找距离本圆体最近的百分比
     * <p>
     * //todo 可优化的点:根据距离增减的变化率,来优化查找,省略后面的计算
     */
    private int findNearestPercent() {
        if (pointFSparseArray.size() == 0) {
            return -1;
        }
        int result = -1;
        double minDistance = -1;
        for (int i = 0; i < pointFSparseArray.size(); i++) {
            PointF floats = pointFSparseArray.get(i);
            double v = CircleUtils.pointDistance(this.x, this.y, floats.x, floats.y);
            if (minDistance == -1) {
                result = i;
                minDistance = v;
            } else {
                if (v <= minDistance) {
                    result = i;
                    minDistance = v;
                }
            }
        }
        return result;
    }

    private static final String TAG = "EllipseItemEntity";

    @Override
    public boolean touchIn(PointF point) {
        double v = CircleUtils.pointDistance(x, y, point.x, point.y);
        return v <= radius;
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void onTouch(boolean touch) {
        this.touchIng = touch;
    }

    float moveOffsetPercent = 0.0f;

    @Override
    public void touchOffset(float offsetX, float offsetY) {
        if (touchIng) {
            //移动位置前的百分比
            float currentPercent = this.currentPercent;
            //移动位置前的位置
            boolean setX = setX(this.x + offsetX);
            boolean setY = setY(this.y + offsetY);
            //移动位置后的百分比
            int nearestPercent = findNearestPercent();
            //手指移动的百分比
            float moveOffsetPercent = nearestPercent / (float) accuracy - currentPercent;
            if (moveOffsetPercent != 0 && validMovePercent(moveOffsetPercent)) {
                //手指有百分比的移动,也就是有滑动控件,通知其他圆做相同的运动,也就是全部跟着滑动的效果
                for (EllipseItemEntity itemEntity : itemEntities) {
//                    itemEntity.moveOffsetPercent = moveOffsetPercent;
                    notifyOtherMove(moveOffsetPercent, itemEntity);
                }

            }
        } else {
            throw new IllegalStateException();
        }
    }

    private void notifyOtherMove(float percent, EllipseItemEntity... itemEntitySet) {
        for (EllipseItemEntity itemEntity : itemEntitySet) {
            if (itemEntity != this) {
                itemEntity.moveOffsetPercent = percent;
            }
        }
    }

    /**
     * 检查手指移动的百分比是否有效,需要用此方法进行判断的原因解释:
     * 当移动圆的时候,可能会出现移动后跨结点的情况,例如顺序的number是123456,组成一个环状,
     * 现在手指移动2,检查移动的百分比数值,是否大于平均数值,如果大于,则判定为跨过了结点,该次移动的百分比数值无效
     *
     * @param moveOffsetPercent
     * @return
     */
    private boolean validMovePercent(float moveOffsetPercent) {
        return moveOffsetPercent < 1f / itemEntities.size() && moveOffsetPercent > -1f / itemEntities.size();
    }

    @Override
    public boolean isCollision(ItemEntity body) {
        if (body instanceof EllipseItemEntity) {
            return CircleUtils.CircleInCircle(this, (EllipseItemEntity) body);
        }
        return false;
    }

    /**
     * 设置下一次的x位置,注:调用此方法不一定能成功,取决于检测规则是否通过
     * 所有对x赋值的操作都应该调用此方法,不能直接进行赋值,此方法中做了屏幕边界检测和碰撞检测
     */
    @Override
    public boolean setX(float x) {
        //左右屏幕越界判断
        if (rectParent.left + radius <= x && x <= rectParent.right - radius) {
            //与该界面上的其他物体进行碰撞检测,如果碰撞了,则不进行设置新的位置
            for (EllipseItemEntity itemEntity : itemEntities) {
                if (itemEntity != this) {
                    double v = CircleUtils.pointDistance(x, this.y, itemEntity.x, itemEntity.y);
                    if (v <= (this.radius + itemEntity.radius)) {
                        return false;
                    }
                }
            }
            this.x = x;
            return true;
        }
        return false;
    }

    /**
     * 设置下一次的y位置,注:调用此方法不一定能设置成功,取决于检测规则是否通过
     * 所有对y赋值的操作都应该调用此方法,不能直接进行赋值,此方法中做了屏幕边界检测和碰撞检测
     */
    @Override
    public boolean setY(float y) {
        //上下屏幕越界判断
        if (rectParent.top + radius <= y && y <= rectParent.bottom - radius) {
            //与该界面上的其他物体进行碰撞检测,如果碰撞了,则不进行设置新的位置
            for (EllipseItemEntity itemEntity : itemEntities) {
                if (itemEntity != this) {
                    //请求设置新的位置之后,两圆心之间的距离,注意下面的参数y用的是形参
                    double v = CircleUtils.pointDistance(this.x, y, itemEntity.x, itemEntity.y);
                    if (isCollision(itemEntity)) {
                        //如果当前与某一圆体重叠了,则检测新的位置两圆心之间距离是否更大了,如果更大则允许设置新的位置,这样能使重叠面积变小
                        double currentDistance = CircleUtils.pointDistance(this.x, this.y, itemEntity.x, itemEntity.y);
                        if (v > currentDistance) {
                            break;
                        }
                    }
                    if (v <= (this.radius + itemEntity.radius)) {
                        return false;
                    }
                }
            }
            this.y = y;
            return true;
        }
        return false;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }


    //所连接的item对象,用于绘制绑定的线条
    private Set<AttachInitiator> attachEntity = new HashSet<>();

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


    public void LinkToNextNode(EllipseItemEntity itemEntity) {
        if (this.nextNode != null || itemEntity.lastNode != null) {
            throw new IllegalStateException();
        }
        this.nextNode = itemEntity;
        itemEntity.lastNode = this;
    }

    public void LinkToLastNode(EllipseItemEntity itemEntity) {
        if (itemEntity.lastNode != null || itemEntity.nextNode != null) {
            throw new IllegalStateException();
        }
        this.lastNode = itemEntity;
        itemEntity.nextNode = this;
    }

    /**
     * 将一元素添加到本对象的下一节点中,(也就是在链表中添加一个元素)
     *
     * @param itemEntity
     */
    public void joinInNextNodeToLinkedList(EllipseItemEntity itemEntity) {
        EllipseItemEntity selfNextNode = this.nextNode;
        itemEntity.nextNode = this;
        itemEntity.lastNode = selfNextNode;
        selfNextNode.lastNode = itemEntity;
        this.nextNode = itemEntity;
    }

    /**
     * 在链表中移除自身
     */
    public void RemoveSelfFromLinkedList() {
        this.lastNode.nextNode = this.nextNode;
        this.nextNode.lastNode = this.lastNode;
        this.lastNode = null;
        this.nextNode = null;
    }

    @Deprecated
    public void refresh2() {
//        float nextPercent = nextNode.currentPercent;
//        float lastPercent = lastNode.currentPercent;
//        if (lastPercent <= currentPercent && currentPercent <= nextPercent) {
////            tagPercent =tagPercent +  ;
//        } else {
////            tagPercent = ((nextPercent + lastPercent) % 1) / 2f;
//        }
//        Log.i(TAG, String.format("refresh2: 左:%.2f,中:%.2f,右:%.2f", lastPercent,tagPercent,nextPercent));

    }

    public void setAllEntitySet(Set<EllipseItemEntity> itemEntitySet) {
        itemEntities.addAll(itemEntitySet);
    }


}
