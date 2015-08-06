package com.github.programmerr47.photostealer.representation.pages;

import android.annotation.TargetApi;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.programmerr47.photostealer.R;
import com.github.programmerr47.photostealer.api.ApiGetMethod;
import com.github.programmerr47.photostealer.api.ApiMethod;
import com.github.programmerr47.photostealer.api.parsers.htmlparsers.PhotosParser;
import com.github.programmerr47.photostealer.representation.PhotoStealerApplication;
import com.github.programmerr47.photostealer.representation.adapters.items.PhotoItem;
import com.github.programmerr47.photostealer.representation.tasks.ApiGetMethodTask;
import com.github.programmerr47.photostealer.representation.tasks.AsyncTaskWithListener;
import com.github.programmerr47.photostealer.util.AndroidUtils;
import com.github.programmerr47.photostealer.util.AnimationUtil;
import com.github.programmerr47.photostealer.util.Constants;
import com.github.programmerr47.photostealer.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class ScanFragment extends MainAcitivityFragment implements
        View.OnClickListener,
        AsyncTaskWithListener.OnTaskFinishedListener {

    private Toolbar mToolbar;

    private EditText mUrlEditText;
    private View mScanButton;
    private View mProgress;
    private View mButtonLabel;

    private String mLoadingUrl;
    private int mScanButtonFirstWidth;
    private int mProgressWidth;

    private boolean isImagesLoading;

    private int mDisabledSearchButtonColor = PhotoStealerApplication.getAppContext().getResources().getColor(R.color.accent_light);
    private int mEnabledSearchButtonColor = PhotoStealerApplication.getAppContext().getResources().getColor(R.color.accent);

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
        mProgress = view.findViewById(R.id.loading_progress);
        mButtonLabel = view.findViewById(R.id.search_label);
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

        mToolbar.setTitle(PhotoStealerApplication.getAppContext().getString(R.string.app_name));
        getMainActivityCallbacks().setToolbar(mToolbar);

        mScanButton.setOnClickListener(this);
        mScanButton.setEnabled(false);

        mUrlEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    scanUrlAndGoToPhotoPage();
                    handled = true;
                }
                return handled;
            }
        });

        mUrlEditText.addTextChangedListener(new TextWatcher() {
            boolean wasText;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isImagesLoading) {
                    ColorDrawable[] color;
                    boolean hasText = s.length() > 0;
                    if (s.length() > 0) {
                        if (wasText != hasText) {
                            AnimationUtil.changeColorOfView(mScanButton, mDisabledSearchButtonColor, mEnabledSearchButtonColor);
                        }
                    } else {
                        if (wasText != hasText) {
                            AnimationUtil.changeColorOfView(mScanButton, mEnabledSearchButtonColor, mDisabledSearchButtonColor);
                        }
                    }

                    if (hasText) {
                        mScanButton.setEnabled(true);
                    } else {
                        mScanButton.setEnabled(false);
                    }

                    wasText = hasText;
                } else {
                    mScanButton.setBackgroundDrawable(new ColorDrawable(mDisabledSearchButtonColor));
                }
            }
        });

        if (isImagesLoading) {
            mScanButton.setEnabled(false);
            ViewGroup.LayoutParams params = mScanButton.getLayoutParams();
            params.width = mProgressWidth;
            mScanButton.setLayoutParams(params);
            mScanButton.setBackgroundDrawable(new ColorDrawable(mDisabledSearchButtonColor));

            mProgress.setVisibility(View.VISIBLE);
            mButtonLabel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_button) {
            scanUrlAndGoToPhotoPage();
        }
    }

    @Override
    public void onTaskFinished(String taskName, Object extraObject) {
        if (taskName.equals(ApiGetMethodTask.class.getName())) {
            @SuppressWarnings("unchecked")
            List<PhotoItem> items = (List<PhotoItem>) extraObject;

            //TODO temp
            if (items == null) {
                items = new ArrayList<>();
            }

            AnimationUtil.hideProgress(mProgress, mButtonLabel, mScanButton, mScanButtonFirstWidth);
            AnimationUtil.changeColorOfView(mScanButton, mDisabledSearchButtonColor, mEnabledSearchButtonColor);
            mScanButton.setEnabled(true);

            forceCloseKeyboardIfNecessary();
            getMainActivityCallbacks().goToScanResultFragment(mLoadingUrl, items);
            isImagesLoading = false;
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    private void forceCloseKeyboardIfNecessary() {
        if (mUrlEditText.hasFocus() && getActivity() != null) {
            Utils.forceClearFocusOnView(getActivity(), mUrlEditText);
        }
    }

    private void scanUrlAndGoToPhotoPage() {
        isImagesLoading = true;
        mProgressWidth = mProgress.getMeasuredWidth();
        mScanButtonFirstWidth = mScanButton.getMeasuredWidth();
        AnimationUtil.showProgress(mProgress, mButtonLabel, mScanButton);
        AnimationUtil.changeColorOfView(mScanButton, mEnabledSearchButtonColor, mDisabledSearchButtonColor);
        mScanButton.setEnabled(false);

        mLoadingUrl = mUrlEditText.getText().toString();
        ApiMethod<List<PhotoItem>> getPhotosMethod = new ApiGetMethod<List<PhotoItem>>()
                .setUrl(mLoadingUrl)
                .setMethodResultParser(new PhotosParser());

        ApiGetMethodTask<List<PhotoItem>> task = new ApiGetMethodTask<List<PhotoItem>>();
        task.setOnTaskFinishedListener(this);
        task.execute(getPhotosMethod);
    }
}
