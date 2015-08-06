package com.github.programmerr47.photostealer.representation.pages;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class ScanResultFragment extends MainAcitivityFragment {

    private static final int DEFAULT_NUM_OF_COLUMNS = 2;

    private AnimationListener mAnimationListener;

    private Toolbar mToolbar;

    private RecyclerView mPhotosView;
    private PhotoAdapter mPhotosAdapter;

    private List<PhotoItem> mPhotoItems;

    public static ScanResultFragment createInstance(List<PhotoItem> displayPhotos) {
        ScanResultFragment result =  new ScanResultFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstatnceState) {
        return inflater.inflate(R.layout.fragment_scan_result, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mPhotosView = (RecyclerView) view.findViewById(R.id.photos);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
}
