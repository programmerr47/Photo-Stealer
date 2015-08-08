package com.github.programmerr47.photostealer.representation.pages;

import android.app.Activity;
import android.app.Fragment;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class MainActivityFragment extends Fragment {

    private MainActivityCallbacks mMainActivityCallbacks;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mMainActivityCallbacks = (MainActivityCallbacks) activity;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Attached activity not implements MainActivityCallbacks");
        }
    }

    protected MainActivityCallbacks getMainActivityCallbacks() {
        return mMainActivityCallbacks;
    }
}
