package com.testdemo;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.testdemo.absPkg.ItemEntity;
import com.testdemo.entity.EllipseItemEntity;
import com.testdemo.widget.IconSurfaceView2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class EllipseActivity extends Activity {

    IconSurfaceView2 iconSurfaceView;

    Set<EllipseItemEntity> ItemEntitySet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_ellipse);

        iconSurfaceView = findViewById(R.id.launcher_view2);


        //屏幕大小
        WindowManager windowManager = getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;
        rectParent.right = x;
        rectParent.bottom = y;


        EllipseItemEntity entity1 = new EllipseItemEntity(0, rectParent);
        EllipseItemEntity entity2 = new EllipseItemEntity(1, rectParent);
        EllipseItemEntity entity3 = new EllipseItemEntity(2, rectParent);
        EllipseItemEntity entity4 = new EllipseItemEntity(3, rectParent);
        EllipseItemEntity entity5 = new EllipseItemEntity(4, rectParent);
        EllipseItemEntity entity6 = new EllipseItemEntity(5, rectParent);
        EllipseItemEntity entity7 = new EllipseItemEntity(6, rectParent);

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

        ItemEntitySet.add(entity1);
        ItemEntitySet.add(entity2);
        ItemEntitySet.add(entity3);
        ItemEntitySet.add(entity4);
        ItemEntitySet.add(entity5);
        ItemEntitySet.add(entity6);
        ItemEntitySet.add(entity7);

        List<EllipseItemEntity> ellipseItemEntities = new ArrayList<>(ItemEntitySet.size());
//        ellipseItemEntities.addAll(ItemEntitySet);
        ellipseItemEntities.add(entity1);
        ellipseItemEntities.add(entity2);
        ellipseItemEntities.add(entity3);
        ellipseItemEntities.add(entity4);
        ellipseItemEntities.add(entity5);
        ellipseItemEntities.add(entity6);
        ellipseItemEntities.add(entity7);
        //链表的连接,构成环链表
        for (int i = 0; i < ellipseItemEntities.size(); i++) {
            EllipseItemEntity ellipseItemEntity = ellipseItemEntities.get(i);
            if (i != ellipseItemEntities.size() - 1) {
                ellipseItemEntity.LinkToNextNode(ellipseItemEntities.get(i + 1));
            } else {
                ellipseItemEntity.LinkToNextNode(ellipseItemEntities.get(0));
            }
        }


//        rectParent
        //初始化各个图标的位置
        for (EllipseItemEntity entity : ItemEntitySet) {
            initPoint(entity);
//            entity.setRange(rectParent);
            entity.setAllEntitySet(ItemEntitySet);
        }

        iconSurfaceView.addAll(ItemEntitySet);

    }


    /**
     * 屏幕范围
     */
    private Rect rectParent = new Rect(0, 0, 0, 0);

    private Random random = new Random();

    private void initPoint(EllipseItemEntity entity) {
        //随机位置取值范围,屏幕边界减去两边的半径
        int randomX = (int) (random.nextInt((int) (rectParent.width() - 2 * entity.getRadius())) + entity.getRadius());
        int randomY = (int) (random.nextInt((int) (rectParent.height() - 2 * entity.getRadius())) + entity.getRadius());
        entity.setX(randomX);
        entity.setY(randomY);


        for (ItemEntity ItemEntity : ItemEntitySet) {
            if (ItemEntity != entity) {
                if (entity.isCollision(ItemEntity)) {
//                    initPoint(entity);
                }
            }

        }
    }
}
