package com.zhong.toollib.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import static com.zhong.toollib.weight.CoProgressBarView.ProgressStyleEnum.HORIZONTAL;

/**
 * 进度条
 * 1、水平渐变进度条
 * 2、圆形渐变进度条
 */
public class CoProgressBarView extends View {

    private Paint txtPaint;
    private Paint shapePaint;
    private float maxProgress;
    private Paint progressPaint;
    private float width;
    private float height;

    private float currentProgress = 0;
    private float textSize = 35F;
    private int shapeColor1 = 0XFF33B6FF;
    private int shapeColor2 = 0XFF5CE0F3;
    private int stopColor = 0XFFFF9800;
    private int completeColor = 0XFF009688;
    private final float rrRadius = 15;
    private float circleRadius = 60;
    private String tipText = "已下载%s";
    private int progressStyleEnum = HORIZONTAL.type;
    private Rect textRect;
    private float txtBaseLine;
    private LinearGradient shader;
    private boolean pause;
    private float progressBarHeight = 20F;

    /**
     * Instantiates a new Switch txt view.
     *
     * @param context the context
     */
    public CoProgressBarView(Context context) {
        super(context);
        initParams();
    }

    /**
     * Instantiates a new Switch txt view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public CoProgressBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initParams();
        initDate(attrs);
    }

    /**
     * Instantiates a new Switch txt view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     */
    public CoProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParams();
        initDate(attrs);
    }

    /**
     * Instantiates a new Switch txt view.
     *
     * @param context      the context
     * @param attrs        the attrs
     * @param defStyleAttr the def style attr
     * @param defStyleRes  the def style res
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CoProgressBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initParams();
        initDate(attrs);
    }

    /**
     * Init params.
     */
    protected void initParams() {
        textRect = new Rect();
        //文本画笔
        txtPaint = new Paint();
        txtPaint.setAntiAlias(true);
        txtPaint.setTextAlign(Paint.Align.CENTER);
        txtPaint.setColor(shapeColor1);
        txtPaint.setTextSize(textSize);

        //形状画笔
        shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePaint.setAntiAlias(true);
        shapePaint.setColor(shapeColor1);
        shapePaint.setStrokeCap(Paint.Cap.BUTT);
        shapePaint.setStrokeWidth(1);
        shapePaint.setDither(true);


        //圆形进度条画笔
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(shapeColor1);
        progressPaint.setStrokeWidth(5);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setDither(true);
    }


    private void initDate(AttributeSet attrs) {
//        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CoProgressBarView);
//        for (int i = 0; i < typedArray.getIndexCount(); i++) {
//            int arrayIndex = typedArray.getIndex(i);
//            switch (arrayIndex) {
//                case R.styleable.CoProgressBarView_bar_height:
//                    progressBarHeight = typedArray.getDimension(arrayIndex, 20F);
//                    break;
//                case R.styleable.CoProgressBarView_font_size:
//                    textSize = typedArray.getDimension(arrayIndex, 35F);
//                    break;
//                case R.styleable.CoProgressBarView_max_progress:
//                    maxProgress = typedArray.getFloat(arrayIndex, 100);
//                    break;
//                case R.styleable.CoProgressBarView_radius:
//                    circleRadius = typedArray.getDimension(arrayIndex, 60);
//                    break;
//                case R.styleable.CoProgressBarView_mode:
//                    progressStyleEnum = typedArray.getInt(arrayIndex, HORIZONTAL.type);
//                    break;
//                default:
//                    throw new IllegalStateException("Unexpected value: " + arrayIndex);
//            }
//        }
//        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        switch (progressStyleEnum) {
            case 1:
                drawHorizontalProgress(canvas);
                drawColorProgressText(canvas);
                break;
            case 2:
                drawCircleProgress(canvas);
                break;
        }
    }

    /**
     * 水平进度条
     */
    private void drawHorizontalProgress(Canvas canvas) {
        //绘制进度条边框
        shapePaint.setStyle(Paint.Style.STROKE);
        RectF rectF1 = new RectF(10, height / 2 - progressBarHeight, width - 10, height / 2 + progressBarHeight);
        canvas.drawRoundRect(rectF1, rrRadius, rrRadius, shapePaint);

        canvas.save();
        Path path = new Path();
        path.addRoundRect(rectF1, rrRadius, rrRadius, Path.Direction.CW);
        canvas.clipPath(path);

        //绘制进度条
        float progress = (currentProgress / maxProgress) * (width - 10);
        shader = new LinearGradient(10, height / 2 - progressBarHeight, progress, height / 2 + progressBarHeight
                , new int[]{shapeColor2, shapeColor1}, null, Shader.TileMode.MIRROR);
        shapePaint.setShader(shader);
        shapePaint.setStyle(Paint.Style.FILL);
        RectF rectF2 = new RectF(10, height / 2 - progressBarHeight, progress, height / 2 + progressBarHeight);
        canvas.drawRect(rectF2, shapePaint);

        //绘制进度文本
        String pro = currentProgress / maxProgress == 1 ? "已完成" : String.format(tipText, String.valueOf((int) ((currentProgress / maxProgress) * 100))) + "%";
        txtPaint.getTextBounds(pro, 0, pro.length(), textRect);
        Paint.FontMetricsInt fontMetrics = txtPaint.getFontMetricsInt();
        txtBaseLine = (rectF2.bottom + rectF2.top) / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
        txtPaint.setTextSize(textSize);
        txtPaint.setColor(shapeColor1);
        canvas.drawText(pro, (width - 20) / 2, txtBaseLine, txtPaint);
        canvas.restore();
    }


    /**
     * 绘制圆形进度条
     */
    private void drawCircleProgress(Canvas canvas) {
        //绘制圆环
        progressPaint.setColor(0XFFECECEC);
        progressPaint.setShader(null);
        canvas.drawCircle(width / 2, height / 2, circleRadius, progressPaint);

        float progress = (currentProgress / maxProgress) * 360;
        if (!pause) {
            //绘制圆弧
            progressPaint.setColor(shapeColor1);
            shader = new LinearGradient(width / 2 - circleRadius, height / 2 - circleRadius, width / 2 + circleRadius, height / 2 + circleRadius
                    , new int[]{shapeColor2, shapeColor1}, null, Shader.TileMode.MIRROR);
            progressPaint.setShader(shader);
            RectF rectF = new RectF(width / 2 - circleRadius, height / 2 - circleRadius, width / 2 + circleRadius, height / 2 + circleRadius);
            canvas.drawArc(rectF, -90, progress, false, progressPaint);

            //绘制进度文本
            txtPaint.setTextSize(textSize);
            txtPaint.setColor(shapeColor1);
            Paint.FontMetricsInt fontMetrics = txtPaint.getFontMetricsInt();
            float baselines = (rectF.bottom + rectF.top) / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
            if (currentProgress / maxProgress == 1) {
                txtPaint.setTextSize(30);
                canvas.drawText("已完成", width / 2, baselines, txtPaint);
                return;
            }
            String progressText = (int) ((currentProgress / maxProgress) * 100) + "%";
            canvas.drawText(progressText, width / 2, baselines, txtPaint);
        } else {
            progressPaint.setColor(stopColor);
            RectF rectF = new RectF(width / 2 - circleRadius, height / 2 - circleRadius, width / 2 + circleRadius, height / 2 + circleRadius);
            canvas.drawArc(rectF, -90, progress, false, progressPaint);

            txtPaint.setColor(stopColor);
            txtPaint.setTextSize(textSize);
            Paint.FontMetricsInt fontMetrics = txtPaint.getFontMetricsInt();
            float baselines = (rectF.bottom + rectF.top) / 2 - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
            canvas.drawText("已暂停", width / 2, baselines, txtPaint);
        }
    }

    /**
     * 当前进度变色处理
     */
    private void drawColorProgressText(Canvas canvas) {
        int tWidth = textRect.width();
        float x = (width - 20) / 2;
        float y = txtBaseLine;
        float v = (width - 20) / 2 - width / 2;
        float progressWidth = (currentProgress / maxProgress) * (width - 20);
        if (progressWidth > v) {
            txtPaint.setColor(Color.WHITE);
            canvas.save();//Canvas.CLIP_SAVE_FLAG
            float right = Math.min(progressWidth, x + tWidth);
            canvas.clipRect(v, height / 2 - progressBarHeight, right, height / 2 + progressBarHeight);
            String pro = currentProgress / maxProgress == 1 ? "已完成" : String.format(tipText, String.valueOf((int) ((currentProgress / maxProgress) * 100))) + "%";
            canvas.drawText(pro, x, y, txtPaint);
            canvas.restore();
        }
    }

    /**
     * 设置进度条样式
     *
     * @param progressStyleEnum the progress style enum
     */
    public CoProgressBarView setMode(ProgressStyleEnum progressStyleEnum) {
        this.progressStyleEnum = progressStyleEnum.type;
        return this;
    }

    /**
     * 设置最大进度
     *
     * @param maxProgress the max progress
     */
    public CoProgressBarView setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
        invalidate();
        return this;
    }

    /**
     * Sets progress bar height.
     *
     * @param progressBarHeight the progress bar height
     */
    public CoProgressBarView setProgressBarHeight(float progressBarHeight) {
        this.progressBarHeight = progressBarHeight;
        return this;
    }

    /**
     * 设置字体大小
     *
     * @param textSize the text size
     */
    public CoProgressBarView setProgressFontSize(float textSize) {
        this.textSize = textSize;
        return this;
    }


    /**
     * 设置圆环进度条半径
     *
     * @param circleRadius the circle radius
     */
    public CoProgressBarView setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
        return this;
    }

    /**
     * 设置当前进度
     *
     * @param currentProgress the current progress
     */
    public void setCurrentProgress(float currentProgress) {
        this.currentProgress = currentProgress;
        invalidate();
    }

    /**
     * 暂停圆形进度条，对水平进度条无效
     *
     * @param b the b
     */
    public void pause(boolean b) {
        this.pause = b;

        invalidate();
    }


    /**
     * 是否暂停状态
     *
     * @return the boolean
     */
    public boolean isPause() {
        return pause;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }


    /**
     * The enum Progress style enum.
     */
    public enum ProgressStyleEnum {

        /**
         * 水平进度条
         */
        HORIZONTAL(1),
        /**
         * 圆形进度条
         */
        CIRCLE(2);

        private int type;

        ProgressStyleEnum(int type) {
            this.type = type;
        }
    }
}
