package com.github.programmerr47.photostealer.pages;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.programmerr47.photostealer.R;
import com.github.programmerr47.photostealer.adapters.items.PhotoItem;
import com.github.programmerr47.photostealer.util.AndroidUtils;
import com.github.programmerr47.photostealer.util.Constants;

import java.util.ArrayList;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class ScanFragment extends MainAcitivityFragment implements View.OnClickListener {

    private Toolbar mToolbar;

    private EditText mUrlEditText;
    private View mScanButton;

    public static ScanFragment createInstance() {
        return new ScanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstatnceState) {
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mUrlEditText = (EditText) view.findViewById(R.id.search_request);
        mScanButton = view.findViewById(R.id.search_button);
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

        mScanButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_button) {
            //TODO
            getMainActivityCallbacks().goToScanResultFragment(new ArrayList<PhotoItem>());
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
