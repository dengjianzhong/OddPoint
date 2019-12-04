package com.zhong.toollib.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * 集成虚线和邮票的样式
 */
public class DashLineView extends View {
    private int width, height;
    private Paint paint;
    private Path path;
    private Paint stampPain;
    private float radius = 20F;
    private boolean enableStamp = true;
    private DashModeEnum mode= DashModeEnum.HORIZONTAL;

    /**
     * Instantiates a new Dash line view.
     *
     * @param context the context
     */
    public DashLineView(Context context) {
        super(context);
        init();
    }

    /**
     * Instantiates a new Dash line view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public DashLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Instantiates a new Dash line view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public DashLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Instantiates a new Dash line view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DashLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(1F);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0XFF4D4D4D);
        paint.setPathEffect(new DashPathEffect(new float[]{10F, 7F}, 0));

        stampPain = new Paint(Paint.ANTI_ALIAS_FLAG);
        stampPain.setStrokeWidth(1F);
        stampPain.setStyle(Paint.Style.FILL);
        stampPain.setColor(0XFFFFFFFF);

        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        int startY = height / 2;
        int statX = width / 2;

        path.reset();
        if (mode== DashModeEnum.HORIZONTAL) {
            path.moveTo(0, startY);
            path.lineTo(width, startY);
        }else {
            path.moveTo(statX, 0);
            path.lineTo(statX, height);
        }
        canvas.drawPath(path, paint);

//        path.addCircle(3, 3, 3, Path.Direction.CW);
//        paint.setPathEffect(new PathDashPathEffect(path, 15, 0, PathDashPathEffect.Style.ROTATE));
//        paint.setShader(new LinearGradient(0, startY, width, startY, new int[]{Color.TRANSPARENT, Color.BLACK, Color.BLACK, Color.TRANSPARENT}, null, Shader.TileMode.CLAMP));

        // TODO: 2019/12/4 0004 绘制邮票样式
        if (enableStamp) {
            RectF leftRectF = new RectF(-radius, startY - radius, radius, startY + radius);
            canvas.drawArc(leftRectF, -90F, 180, false, stampPain);

            RectF rightRectF = new RectF(width - radius, startY - radius, width + radius, startY + radius);
            canvas.drawArc(rightRectF, 90F, 180, false, stampPain);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }


    /**
     * 是否启用邮票模式
     *
     * @param enableStamp the enable stamp
     */
    public void setEnableStamp(boolean enableStamp) {
        this.enableStamp = enableStamp;
    }

    /**
     * 设置邮票半圆半径
     *
     * @param radius the radius
     */
    public void setStampRadius(float radius) {
        this.radius = radius;
    }

    public void setMode(DashModeEnum mode) {
        this.mode = mode;
    }

    public enum DashModeEnum {
        /**
         * 水平虚线
         */
        HORIZONTAL(1),
        /**
         * 垂直虚线
         */
        VERTICAL(2);

        private int type;

        DashModeEnum(int type) {
            this.type = type;
        }
    }
}
