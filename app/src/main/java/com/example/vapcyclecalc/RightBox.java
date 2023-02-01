package com.example.vapcyclecalc;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class RightBox extends View {
    private Paint paint;
    private ValueAnimator animator;
    private boolean isAnimating = true;
    private final int defaultWidth = 30;

    public RightBox(Context context) {
        super(context);
        init();
    }

    public RightBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RightBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);

        animator = ValueAnimator.ofFloat(5f, 10f, 5f);
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(animation -> {
            paint.setStrokeWidth((float) animation.getAnimatedValue());
            invalidate();
        });
        animator.start();

        setOnClickListener(v -> {

        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Corner 1
        canvas.drawLine(0, 0, defaultWidth , 0, paint);
        canvas.drawLine(defaultWidth, 0, defaultWidth , defaultWidth, paint);
        // Corner 2
        canvas.drawLine(defaultWidth, getMeasuredHeight(), defaultWidth, getMeasuredHeight() - defaultWidth, paint);
        canvas.drawLine(defaultWidth, getMeasuredHeight(), 0, getMeasuredHeight(), paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(defaultWidth, getMeasuredHeight());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.cancel();
    }
}
