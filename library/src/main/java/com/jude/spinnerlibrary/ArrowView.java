package com.jude.spinnerlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class ArrowView extends View
{
    private int _ANIM = 0;
    protected Paint mPaint;
    protected int backColor = Color.BLACK;
    protected boolean shouldClose;
    private Path p;

    public ArrowView(Context context)
    {
        super(context);
        init();
    }

    public ArrowView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public ArrowView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        p = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawColor(backColor);

        int height = canvas.getHeight();
        int width = canvas.getWidth();
        p.reset();
        p.moveTo(width/4, height/2);
        if (_ANIM == 0)
        {
            p.lineTo(width/2, height - height/4);
        }
        else
        {
            p.lineTo(width/2, height/4);
        }
        p.lineTo(width - width/4, height/2);
        if (shouldClose)
        {
            p.close();
        }
        canvas.drawPath(p, mPaint);
    }

    protected void setAnim(int anim)
    {
        _ANIM = anim;
        invalidate();
    }

    public void setBackColor(int color)
    {
        backColor = color;
        invalidate();
    }

    public void setArrowPaint(Paint mPaint)
    {
        this.mPaint = mPaint;
        invalidate();
    }

    public void setArrowView(Paint mPaint, int backColor)
    {
        this.mPaint = mPaint;
        this.backColor = backColor;
        invalidate();
    }

    protected void setPathType(boolean shouldClose)
    {
        this.shouldClose = shouldClose;
        invalidate();
    }
}
