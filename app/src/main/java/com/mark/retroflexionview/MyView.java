package com.mark.retroflexionview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 自定义翻转View
 * Created by mark.leihao on 2016/8/5.
 */

public class MyView extends View {

    private Context context;
    /**
     * 圆心坐标x
     */
    float o_x;

    /**
     * 圆心坐标y
     */
    float o_y;

    /**
     * 图片的宽度
     */
    int width;

    /**
     * 图片的高度
     */
    int height;

    /**
     * view的真实宽度
     */
    float maxwidth;

    /**
     * view的真实高度
     */
    float maxheight;

    /**
     * 圆环
     */
    Bitmap ring;

    private Matrix matrix;
    private Camera camera;
    private float deltaDegree;//翻转角度

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initData();
    }

    private void initData() {
        ring = BitmapFactory.decodeResource(context.getResources(), R.mipmap.dashboard_01);
        width = ring.getWidth();
        height = ring.getHeight();
        maxwidth = (float) Math.sqrt(width * width + height * height);
        maxheight = maxwidth;
        o_x = maxwidth / 2;
        o_y = maxheight / 2;
        matrix = new Matrix();
        camera = new Camera();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        camera.save();
        camera.translate(-width / 2, -height / 2, 100);
        camera.rotateX(-deltaDegree / 100);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(0, -height / 2);
        matrix.postTranslate(maxwidth / 2, (maxheight - height) / 2);
        canvas.drawBitmap(ring, matrix, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int) maxwidth, (int) maxheight);
        Log.i("MyView", "maxwidth = " + maxwidth + ", maxheight = " + maxheight + ", o_x = " + o_x + ", o_y = " + o_y);
    }

    float lastMouseY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastMouseY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - lastMouseY;
                deltaDegree += dy;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }
}
