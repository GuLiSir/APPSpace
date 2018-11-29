package com.testdemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import com.testdemo.absPkg.ItemEntity;
import com.testdemo.entity.EllipseItemEntity;
import com.testdemo.utils.PointToLineDis;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IconSurfaceView2 extends SurfaceView {
    private final Set<ItemEntity> iconEntitySet = new HashSet<>();

    public IconSurfaceView2(Context context) {
        super(context);
        init();
    }

    public IconSurfaceView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconSurfaceView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public IconSurfaceView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    SurfaceHolder holder;
    //圆形画笔
    private final Paint paint = new Paint();
    private final Paint paintText = new Paint();
    private final Paint paintPath = new Paint();

    private void init() {
        holder = getHolder();
        holder.addCallback(callback);

        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);

        paintText.setColor(Color.BLUE);
        paintText.setAntiAlias(true);
        paintText.setTextSize(50);
        paintText.setStrokeWidth(5);

        paintPath.setColor(Color.RED);
        paintPath.setAntiAlias(true);
        paintPath.setStrokeWidth(2);
        paintPath.setStyle(Paint.Style.STROKE);

        //绘制椭圆
        RectF rectF = new RectF(200, 200, 1700, 900);
        //第一种方法绘制椭圆
        path.addOval(rectF, Path.Direction.CW);
        float length = pathMeasure.getLength();
        pathMeasure.getPosTan(length / 2.0f, pos, tan);
        Log.i(TAG, "init: ");
        pathMeasure.setPath(path, false);
    }

    Path path = new Path();
    PathMeasure pathMeasure = new PathMeasure(path, false);

    private float[] pos = new float[2];
    private float[] tan = new float[2];


    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            isRunning = true;
            new Thread(runnable).start();
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            time1 = System.currentTimeMillis();
            new Thread(runnableCalc).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            isRunning = false;
        }
    };
    private boolean isRunning;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                draw();
                //通过线程休眠以控制刷新速度
                try {
                    // TODO: 2018/11/28 优化:应该根据绘制的时间来决定休眠时间
                    //25fps
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    //用于清除的画笔
    Paint paintClean = new Paint();

    private void draw() {
        Canvas mCanvas;
        mCanvas = holder.lockCanvas();
        try {
            //清屏
            paintClean.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            mCanvas.drawPaint(paintClean);
            paintClean.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            //绘制运动的轨迹线
            mCanvas.drawPath(path, paintPath);
            //初始化画布并在画布上画一些东西
            for (ItemEntity itemEntity : iconEntitySet) {
                itemEntity.onDraw(mCanvas);
            }

            //绘制交点
//            if (testJiaoDian != null) {
//                mCanvas.drawCircle(testJiaoDian.x, testJiaoDian.y, 20, paintPath);
//            }
        } catch (Exception e) {

        } finally {
            //判断画布是否为空，从而避免黑屏情况
            if (mCanvas != null) {
                holder.unlockCanvasAndPost(mCanvas);
            }
        }
    }


    //计算的处理
    long time1 = System.currentTimeMillis();

    private final Runnable runnableCalc = new Runnable() {
        @Override
        public void run() {
            while (isRunning) {
                //这里的时间应该不能放在for里面取,否则对于每一个item时间可能不一样,导致计算出错
                long l = System.currentTimeMillis();
                long lastDuration = l - time1;
                for (ItemEntity itemEntity : iconEntitySet) {
                    //根据上一次的时间差,来刷新新的数据,时间差为0则不刷新
                    if (lastDuration != 0) {
                        itemEntity.refreshParam(lastDuration);
                    }
                }
                for (ItemEntity itemEntity : iconEntitySet) {
                    EllipseItemEntity ellipseItemEntity = (EllipseItemEntity) itemEntity;
                    ellipseItemEntity.refresh2();
                }

                time1 = System.currentTimeMillis();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    //触摸事件的处理
    @Nullable
    private ItemEntity currentTouch;

    //移动速度测量
    private VelocityTracker mVelocityTracker = null;
    //按下屏幕的位置
    private final PointF touchDownPoint = new PointF();
    //最后一次触摸的位置,包含按下和移动
    private final PointF lastTouchPoint = new PointF();

//    private PointF testJiaoDian;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        Log.i(TAG, "onTouchEvent: " + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //初始化触摸
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);
                touchDownPoint.x = x;
                touchDownPoint.y = y;
                lastTouchPoint.x = x;
                lastTouchPoint.y = y;
                //检查是否按在了指定的圆点
                for (ItemEntity entity : iconEntitySet) {
//                    Log.i(TAG, String.format("onTouchEvent: down:%s %.2f, radius:%d", entity.toString(), v, entity.radius));
//                    double v = CircleUtils.pointDistance( entity.getX(), entity.getY(), (int) x, (int) y );
                    if (entity.touchIn(touchDownPoint)) {
                        //触摸按在此圆内,拦截事件
                        currentTouch = entity;
                        currentTouch.onTouch(true);
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                float offsetX = x - lastTouchPoint.x;
                float offsetY = y - lastTouchPoint.y;
                if (currentTouch != null) {
                    //按住的圆点跟着手指移动
                    currentTouch.touchOffset(offsetX, offsetY);
                }
                Log.i(TAG, String.format("onTouchEvent: speed:x %.2f,y %.2f offSetX:%.2f,offSetY:%.2f",
                        mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity(), offsetX, offsetY));
//                testJiaoDian = null;
//                if (offsetX != 0 && offsetY != 0
//                        && lastTouchPoint.x != x
//                        && lastTouchPoint.y != y) {
//                    //找出与椭圆的交点
//                    double[] doubles = PointToLineDis.lineExp(lastTouchPoint.x, lastTouchPoint.y, x, y);
//                    Log.i(TAG, "onTouchEvent: 斜率:"+( - doubles[0]/doubles[1]));
//                    double minDis = 100000;
//                    for (int i = 0; i < EllipseItemEntity.accuracy; i++) {
//                        PointF pointF = EllipseItemEntity.pointFSparseArray.get(i);
//                        double v = PointToLineDis.alLine(pointF.x, pointF.y, doubles[0], doubles[1], doubles[2]);
//                        Log.i(TAG, "onTouchEvent: 小一号:" + i + "   点到直线的距离:" + v+"  公式:"+ Arrays.toString(doubles)+"   单点:"+pointF);
//                        if (v <= minDis) {
//                            testJiaoDian = pointF;
//                            minDis = v;
//                        }
//                    }
//                }

                lastTouchPoint.x = x;
                lastTouchPoint.y = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_CANCEL:
                if (currentTouch != null) {
                    currentTouch.onTouch(false);
                }
                currentTouch = null;
                //清除速度
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                break;
            default:
                break;
        }

//        return super.onTouchEvent(event);
        return true;
    }

    private static final String TAG = "IconSurfaceView";

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isRunning = false;
    }

    public boolean add(ItemEntity itemEntity) {
        return iconEntitySet.add(itemEntity);
    }

    public boolean remove(Object o) {
        return iconEntitySet.remove(o);
    }

    public boolean addAll(Collection<? extends ItemEntity> c) {
        return iconEntitySet.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return iconEntitySet.removeAll(c);
    }

    public void clear() {
        iconEntitySet.clear();
    }
}
