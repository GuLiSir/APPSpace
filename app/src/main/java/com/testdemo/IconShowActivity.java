package com.testdemo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.testdemo.entity.IconEntity;
import com.testdemo.widget.IconSurfaceView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class IconShowActivity extends Activity {

    Set<IconEntity> iconEntitySet = new HashSet<>();
    IconSurfaceView iconSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_icon_show);
        iconSurfaceView = findViewById(R.id.launcher_view);


        IconEntity entity1 = new IconEntity(0);
        IconEntity entity2 = new IconEntity(1);
        IconEntity entity3 = new IconEntity(2);
        IconEntity entity4 = new IconEntity(3);
        IconEntity entity5 = new IconEntity(4);
        IconEntity entity6 = new IconEntity(5);
        IconEntity entity7 = new IconEntity(6);

        entity1.requestAttach(entity2);
        entity1.requestAttach(entity3);
        entity1.requestAttach(entity7);

        entity2.requestAttach(entity1);
        entity2.requestAttach(entity4);
        entity2.requestAttach(entity5);
        entity2.requestAttach(entity7);

        entity3.requestAttach(entity1);
        entity3.requestAttach(entity5);
        entity3.requestAttach(entity6);

        entity4.requestAttach(entity2);
        entity4.requestAttach(entity6);

        entity5.requestAttach(entity2);
        entity5.requestAttach(entity3);
        entity5.requestAttach(entity6);

        entity6.requestAttach(entity3);
        entity6.requestAttach(entity4);
        entity6.requestAttach(entity5);
        entity6.requestAttach(entity7);

        entity7.requestAttach(entity1);
        entity7.requestAttach(entity2);
        entity7.requestAttach(entity6);

        iconEntitySet.add(entity1);
        iconEntitySet.add(entity2);
        iconEntitySet.add(entity3);
        iconEntitySet.add(entity4);
        iconEntitySet.add(entity5);
        iconEntitySet.add(entity6);
        iconEntitySet.add(entity7);


        //屏幕大小
        WindowManager windowManager = getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;

        rectParent.right = x;
        rectParent.bottom = y;
//        rectParent
        //初始化各个图标的位置
        for (IconEntity entity : iconEntitySet) {
            initPoint(entity);
            Log.i(TAG, "onCreate: 布局位置:" + entity.getRect());
            entity.setAllEntitySet(iconEntitySet);
            entity.setRange(rectParent);
        }

        iconSurfaceView.DumpItem(iconEntitySet);
    }

    private static final String TAG = "IconShowActivity";

    /**
     * 屏幕范围
     */
    private Rect rectParent = new Rect(0, 0, 0, 0);

    private Random random = new Random();

    private void initPoint(IconEntity entity) {
        // TODO: 2018/11/21 已知问题,会超过屏幕边界
        int randomX = random.nextInt(rectParent.width());
        int randomY = random.nextInt(rectParent.height());
        entity.setX(randomX);
        entity.setY(randomY);

        boolean b = collisionDetection(entity, iconEntitySet);
        if (b) {
            initPoint(entity);
        }
    }

    /**
     * 碰撞检测的方法,输入一个item,检查是否与其他item有重叠部分
     */
    public static boolean collisionDetection(IconEntity entity, Set<IconEntity> iconEntitySet) {
        //和每一个item进行碰撞检测,不能重叠在一起
        for (IconEntity iconEntity : iconEntitySet) {
            //过滤掉自己,自己和自己不能进行碰撞检测
            if (iconEntity != entity) {
                int x1 = iconEntity.getX();
                int y1 = iconEntity.getY();
                int x2 = entity.getX();
                int y2 = entity.getY();
                //勾股定理求圆心距离
                double sqrt = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
                if (sqrt <= (iconEntity.radius + iconEntity.radius)) {
                    //圆心距离小于半径之和则碰撞
                    return true;
                }
            }
        }
        return false;
    }




}
