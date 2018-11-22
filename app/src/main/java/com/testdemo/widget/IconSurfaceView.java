package com.testdemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

import com.testdemo.absPkg.AttachInitiator;
import com.testdemo.entity.IconEntity;
import com.testdemo.utils.CircleUtils;

import java.util.HashSet;
import java.util.Set;

public class IconSurfaceView extends SurfaceView {
    private final Set<IconEntity> iconEntitySet = new HashSet<>();

    public IconSurfaceView(Context context) {
        super(context);
        init();
    }

    public IconSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IconSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public IconSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    SurfaceHolder holder;
    //圆形画笔
    private final Paint paint = new Paint();
    private final Paint paintText = new Paint();

    private void init() {
        holder = getHolder();
        holder.addCallback(callback);

        paint.setColor(Color.YELLOW);
        paint.setAntiAlias(true);

        paintText.setColor(Color.BLUE);
        paintText.setAntiAlias(true);
    }

//    Set<IconEntity>


    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mIsDrawing = true;
            new Thread(runnable).start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mIsDrawing = false;
        }
    };
    private boolean mIsDrawing;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (mIsDrawing) {


                calcLocation();
                draw();
                //通过线程休眠以控制刷新速度
                try {
                    //25fps
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * 计算每个item新的位置,以及速度的方向
     */
    private void calcLocation() {
        for (IconEntity entity : iconEntitySet) {
            entity.fresh();

        }
    }

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

            //初始化画布并在画布上画一些东西
            for (IconEntity entity : iconEntitySet) {
                Set<AttachInitiator> linkEntity = entity.getAttachEntity();
                //todo 划线的地方可优化,就是每个点之间只画一条线就够了,现在的这种实现是互相划一条线,也就是每个相连点话两条重合的线
                for (AttachInitiator attachInitiator : linkEntity) {
                    IconEntity entity1 = (IconEntity) attachInitiator;
                    mCanvas.drawLine(entity1.getX(), entity1.getY(), entity.getX(), entity.getY(), paint);
                }
//                    mCanvas.drawRect(entity.getRect(), paint);
                mCanvas.drawText(String.valueOf(entity.number), entity.getX(), entity.getY(), paintText);
                //绘制圆
                mCanvas.drawCircle(entity.getX(), entity.getY(), entity.radius, paint);
            }
        } catch (Exception e) {

        } finally {
            //判断画布是否为空，从而避免黑屏情况
            if (mCanvas != null) {
                holder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    public void DumpItem(Set<IconEntity> iconEntitySet) {
        this.iconEntitySet.addAll(iconEntitySet);
    }

    @Nullable
    private IconEntity currentTouch;

    //移动速度测量
    private VelocityTracker mVelocityTracker = null;

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
                //检查是否按在了指定的圆点
                for (IconEntity entity : iconEntitySet) {
                    double v = CircleUtils.pointDistance(entity.getX(), entity.getY(), (int) x, (int) y);
                    if (v <= entity.radius) {
                        //触摸按在此圆内,拦截事件
                        currentTouch = entity;
                        currentTouch.setStop(true);
                        return true;
                    }
                    Log.i(TAG, String.format("onTouchEvent: down:%s %.2f, radius:%d", entity.toString(), v, entity.radius));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                if (currentTouch != null) {
                    //按住的圆点跟着手指移动
                    currentTouch.setX((int) x);
                    currentTouch.setY((int) y);
                }
                Log.i(TAG, String.format("onTouchEvent: speed:x %.2f,y %.2f", mVelocityTracker.getXVelocity(), mVelocityTracker.getYVelocity()));
                break;
            default:
                if (currentTouch == null) {
                    throw new IllegalStateException();
                }
                currentTouch.setStop(false);
                currentTouch = null;
                //清除速度
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                break;
        }

        return super.onTouchEvent(event);
//        return true;
    }

    private static final String TAG = "IconSurfaceView";
}