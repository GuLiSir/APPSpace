package com.testdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.v4.graphics.PathParser;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.testdemo.entity.IconEntity;
import com.testdemo.utils.CircleUtils;
import com.testdemo.widget.IconSurfaceView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

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
            entity.setAllEntitySet(iconEntitySet);
//            CircleBetweenCircleAccelerator circleAccelerator = new CircleBetweenCircleAccelerator(entity, iconEntitySet);
//            CenterAccelerator centerAccelerator = new CenterAccelerator(0,0);
//            FrameAccelerator frameAccelerator = new FrameAccelerator(rectParent);
//            entity.addAccelerator(frameAccelerator);
//            entity.addAccelerator(circleAccelerator);
//            entity.addAccelerator(centerAccelerator);
        }

        iconSurfaceView.DumpItem(iconEntitySet);


//            PathParser pathParser =   PathParser.createPathFromPathData( "M17,19.9l1.7,1.1c-4,5.8-12.1,7.8-12.4,7.9L5.8,27C5.9,27,13.5,25.1,17,19.9z" );
//
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


        for (IconEntity iconEntity : iconEntitySet) {
            boolean b = CircleUtils.CircleInCircle(entity, iconEntity);
            if (b) {
//                initPoint(entity);
            }
        }
    }

}
