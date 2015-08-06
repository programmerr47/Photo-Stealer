package com.github.programmerr47.photostealer.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class XFractionalFrameLayout extends FrameLayout {

    private int mScreenWidth;

    private float mXFraction;

    public XFractionalFrameLayout(Context context) {
        super(context);
    }

    public XFractionalFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XFractionalFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public XFractionalFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mScreenWidth = w;
        setX((mScreenWidth > 0) ? (mScreenWidth - mXFraction * mScreenWidth) : 0);
    }

    public float getXFraction() {
        return mXFraction;
    }

    public void setXFraction(float xFraction) {
        mXFraction = xFraction;
        setX((mScreenWidth > 0) ? (mScreenWidth - mXFraction * mScreenWidth) : 0);
    }
}
