package com.github.programmerr47.photostealer.representation.callback;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

/**
 * @author Michael Spitsin
 * @since 2015-08-07
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 20;

    private int mScrolledDistance = 0;
    private boolean mControlsVisible = true;
    private RecyclerViewFirstItemFinder mFirstItemFinder;

    public HidingScrollListener(@NonNull RecyclerViewFirstItemFinder firstItemFinder) {
        this.mFirstItemFinder = firstItemFinder;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItem = mFirstItemFinder.findFirstVisibleItemPosition(recyclerView);

        if (firstVisibleItem == 0) {
            if (!mControlsVisible) {
                onShow();
                mControlsVisible = true;
            }
        } else {
            if (mScrolledDistance > HIDE_THRESHOLD && mControlsVisible) {
                onHide();
                mControlsVisible = false;
                mScrolledDistance = 0;
            } else if (mScrolledDistance < -HIDE_THRESHOLD && !mControlsVisible) {
                onShow();
                mControlsVisible = true;
                mScrolledDistance = 0;
            }
        }

        if ((mControlsVisible && dy > 0) || (!mControlsVisible && dy < 0)) {
            mScrolledDistance += dy;
        }
    }

    public abstract void onHide();

    public abstract void onShow();
}
