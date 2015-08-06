package com.github.programmerr47.photostealer.representation.pages;

import android.support.v7.widget.Toolbar;

import com.github.programmerr47.photostealer.representation.adapters.items.PhotoItem;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public interface MainActivityCallbacks {
    void setToolbar(Toolbar toolbar);
    void goToScanResultFragment(List<PhotoItem> photos);
}
