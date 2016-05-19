package com.bbsz.mlibrary.plugin.webview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/5/18.
 */
/*public*/ class WebViewProgress extends View {

    private float progress = 0.f;

    private int contentWidth, contentHeight;
    private int color = Color.GREEN;
    private Paint paint;

    public WebViewProgress(Context context) {
        super(context);
    }

    public WebViewProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (contentWidth == 0) {
            contentWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        }
        if (contentHeight == 0) {
            contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
        }

        float left = getPaddingLeft();
        float top = getPaddingTop();
        float right = left + progress * contentWidth;
        float bottom = top + contentHeight;

        if (null == paint) {
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color);
        }
        canvas.drawRect(left, top, right, bottom, paint);
    }

    public void updateProgress(float progress) {
        this.progress = progress;
        if (progress >= 1.0f || progress <= 0.f) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);
        }
        color = Color.argb((int) (200 * (1 - progress)) + 55, 0x0, 0xff, 0x0);
        invalidate();
    }

    public float curProgress() {
        return progress;
    }
}
