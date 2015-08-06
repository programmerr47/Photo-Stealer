package com.github.programmerr47.photostealer.representation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.github.programmerr47.photostealer.R;
import com.github.programmerr47.photostealer.representation.adapters.items.PhotoItem;
import com.github.programmerr47.photostealer.representation.pages.MainActivityCallbacks;
import com.github.programmerr47.photostealer.representation.pages.ScanFragment;
import com.github.programmerr47.photostealer.representation.pages.ScanResultFragment;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        FragmentManager.OnBackStackChangedListener,
        AnimationListener,
        MainActivityCallbacks {

    private static final String DID_SCAN_RESULT_SLIDED_OUT = "DID_SCAN_RESULT_SLIDED_OUT";

    private ScanFragment mScanFragment;
    private ScanResultFragment mScanResultFragment;

    private Toolbar mToolbar;

    private View mDarkCoverView;

    private boolean isAnimating;
    private boolean didScanResultFragmentSlideOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            didScanResultFragmentSlideOut = savedInstanceState.getBoolean(DID_SCAN_RESULT_SLIDED_OUT);
        }

        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            goToScanResultFragment(null, null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(DID_SCAN_RESULT_SLIDED_OUT, didScanResultFragmentSlideOut);
    }

    @Override
    public void onBackPressed() {
        if (!isAnimating) {
            if (didScanResultFragmentSlideOut) {
                didScanResultFragmentSlideOut = false;
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onAnimationEnd() {
        isAnimating = false;
    }

    @Override
    public void onAnimationStart() {
        isAnimating = true;
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        this.mToolbar = toolbar;
        setSupportActionBar(toolbar);
    }

    @Override
    public void goToScanResultFragment(String url, List<PhotoItem> photos) {
        if (isAnimating) {
            return;
        }
        isAnimating = true;
        if (didScanResultFragmentSlideOut) {
            didScanResultFragmentSlideOut = false;
            getFragmentManager().popBackStack();
        } else {
            didScanResultFragmentSlideOut = true;

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.animator.fragment_slide_in,
                    R.animator.fragment_slide_out,
                    R.animator.fragment_slide_in,
                    R.animator.fragment_slide_out);
            mScanResultFragment = ScanResultFragment.createInstance(url, photos);
            transaction.replace(R.id.slide_fragment_container, mScanResultFragment);
            transaction.addToBackStack(null);
            transaction.commit();
            makeBaseFragmentDarker(null);
        }
    }

    @Override
    public void onBackStackChanged() {
        if (!didScanResultFragmentSlideOut) {
            makeBaseFragmentLighter();

            Toolbar toolbar = mScanFragment.getToolbar();
            setToolbar(toolbar);
        }
    }

    private void init() {
        FragmentManager fm = getFragmentManager();

        mScanFragment = (ScanFragment) fm.findFragmentById(R.id.move_to_back_container);
        if (mScanFragment == null) {
            mScanFragment = ScanFragment.createInstance();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.move_to_back_container, mScanFragment);
            transaction.commit();
        }

        mScanResultFragment = (ScanResultFragment) fm.findFragmentById(R.id.slide_fragment_container);

        mDarkCoverView = findViewById(R.id.dark_cover);
        mDarkCoverView.setAlpha(0f);

        fm.addOnBackStackChangedListener(this);
    }

    private void makeBaseFragmentDarker(Animator.AnimatorListener listener){
        ObjectAnimator darkHoverViewAnimator = ObjectAnimator.ofFloat(mDarkCoverView, "alpha", 0.0f, 0.5f);
        darkHoverViewAnimator.setInterpolator(new DecelerateInterpolator());
        darkHoverViewAnimator.setDuration(300);

        AnimatorSet s = new AnimatorSet();
        s.play(darkHoverViewAnimator);

        if (listener != null) {
            s.addListener(listener);
        }
        s.start();
    }

    private void makeBaseFragmentLighter()
    {
        ObjectAnimator darkHoverViewAnimator = ObjectAnimator.ofFloat(mDarkCoverView, "alpha", 0.5f, 0.0f);
        darkHoverViewAnimator.setInterpolator(new AccelerateInterpolator());
        darkHoverViewAnimator.setDuration(300);

        AnimatorSet s = new AnimatorSet();
        s.play(darkHoverViewAnimator);
        s.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
        s.start();
    }
}
