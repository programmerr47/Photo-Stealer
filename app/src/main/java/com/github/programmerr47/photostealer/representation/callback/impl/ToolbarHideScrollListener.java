package com.github.programmerr47.photostealer.representation.callback.impl;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.github.programmerr47.photostealer.representation.callback.HidingScrollListener;
import com.github.programmerr47.photostealer.representation.callback.RecyclerViewFirstItemFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2015-08-07
 */
public class ToolbarHideScrollListener extends HidingScrollListener {
    private List<View> mViews = new ArrayList<>();

    public ToolbarHideScrollListener(@NonNull RecyclerViewFirstItemFinder firstItemFinder, List<View> views) {
        super(firstItemFinder);
        this.mViews = views;
    }

    public ToolbarHideScrollListener(@NonNull RecyclerViewFirstItemFinder firstItemFinder) {
        super(firstItemFinder);
    }

    @Override
    public void onHide() {
        for (View view : mViews) {
            view.animate().translationY(-view.getLayoutParams().height).setInterpolator(new AccelerateInterpolator(2)).start();
        }
    }

    @Override
    public void onShow() {
        for (View view : mViews) {
            view.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }

    public void addView(View view) {
        mViews.add(view);
    }

    public void removeView(View view) {
        mViews.remove(view);
    }
}
