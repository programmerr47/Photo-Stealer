package com.github.programmerr47.photostealer.representation.pages;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.programmerr47.photostealer.R;
import com.github.programmerr47.photostealer.api.ApiGetMethod;
import com.github.programmerr47.photostealer.api.ApiMethod;
import com.github.programmerr47.photostealer.api.parsers.htmlparsers.PhotosParser;
import com.github.programmerr47.photostealer.representation.PhotoStealerApplication;
import com.github.programmerr47.photostealer.representation.adapters.items.PhotoItem;
import com.github.programmerr47.photostealer.representation.tasks.ApiGetMethodTask;
import com.github.programmerr47.photostealer.representation.tasks.AsyncTaskWithListener;
import com.github.programmerr47.photostealer.util.AndroidUtils;
import com.github.programmerr47.photostealer.util.Constants;

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

    private String mLoadingUrl;

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

        mToolbar.setTitle(PhotoStealerApplication.getAppContext().getString(R.string.app_name));
        getMainActivityCallbacks().setToolbar(mToolbar);

        mScanButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_button) {
            mLoadingUrl = mUrlEditText.getText().toString();
            ApiMethod<List<PhotoItem>> getPhotosMethod = new ApiGetMethod<List<PhotoItem>>()
                    .setUrl(mLoadingUrl)
                    .setMethodResultParser(new PhotosParser());

            ApiGetMethodTask<List<PhotoItem>> task = new ApiGetMethodTask<List<PhotoItem>>();
            task.setOnTaskFinishedListener(this);
            task.execute(getPhotosMethod);
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

            getMainActivityCallbacks().goToScanResultFragment(mLoadingUrl, items);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }
}
