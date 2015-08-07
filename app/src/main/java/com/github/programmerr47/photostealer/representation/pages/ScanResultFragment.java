package com.github.programmerr47.photostealer.representation.pages;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.github.programmerr47.photostealer.representation.AnimationListener;
import com.github.programmerr47.photostealer.R;
import com.github.programmerr47.photostealer.representation.adapters.PhotoAdapter;
import com.github.programmerr47.photostealer.representation.adapters.items.PhotoItem;
import com.github.programmerr47.photostealer.representation.callback.RecyclerViewFirstItemFinder;
import com.github.programmerr47.photostealer.representation.callback.impl.ToolbarHideScrollListener;
import com.github.programmerr47.photostealer.util.AndroidUtils;
import com.github.programmerr47.photostealer.util.Constants;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class ScanResultFragment extends MainActivityFragment {

    private static final int DEFAULT_NUM_OF_COLUMNS = 2;

    private AnimationListener mAnimationListener;

    private Toolbar mToolbar;

    private RecyclerView mPhotosView;
    private PhotoAdapter mPhotosAdapter;

    private String mUrl;
    private List<PhotoItem> mPhotoItems;

    private ToolbarHideScrollListener mScrollListener = new ToolbarHideScrollListener(new RecyclerViewFirstItemFinder() {
        @Override
        public int findFirstVisibleItemPosition(RecyclerView recyclerView) {
            return ((LinearLayoutManager) mPhotosView.getLayoutManager()).findFirstVisibleItemPosition();
        }
    });

    public static ScanResultFragment createInstance(String url, List<PhotoItem> displayPhotos) {
        ScanResultFragment result =  new ScanResultFragment();
        result.mUrl = url;
        result.mPhotoItems = displayPhotos;
        return result;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mAnimationListener = (AnimationListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AnimationListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mPhotosView = (RecyclerView) view.findViewById(R.id.photos);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (AndroidUtils.hasLollipop()) {
            mToolbar.setElevation(AndroidUtils.dpToPx(Constants.TOOLBAR_ELEVATION_DEFAULT));
        } else {
            ViewCompat.setElevation(mToolbar, Constants.TOOLBAR_ELEVATION_DEFAULT);
        }

        mToolbar.setTitle(mUrl);
        getMainActivityCallbacks().setToolbar(mToolbar);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mScrollListener.addView(mToolbar);

        mPhotosView.addOnScrollListener(mScrollListener);
        mPhotosView.setLayoutManager(new GridLayoutManager(getActivity(), DEFAULT_NUM_OF_COLUMNS));
        mPhotosView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mPhotosView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                        mPhotosAdapter = new PhotoAdapter(mPhotoItems, mPhotosView.getMeasuredWidth() / DEFAULT_NUM_OF_COLUMNS);
                        mPhotosView.setAdapter(mPhotosAdapter);
                    }
                });
    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        int id = enter ? R.animator.fragment_slide_in : R.animator.fragment_slide_out;
        final Animator anim = AnimatorInflater.loadAnimator(getActivity(), id);
        if (enter) {
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimationListener.onAnimationEnd();
                }
            });
        }
        return anim;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
    private ActionBar getActionBar() {
        AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
        return parentActivity.getSupportActionBar();
    }
}
