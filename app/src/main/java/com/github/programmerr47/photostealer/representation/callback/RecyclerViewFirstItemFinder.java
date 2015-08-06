package com.github.programmerr47.photostealer.representation.callback;

import android.support.v7.widget.RecyclerView;

/**
 * @author Michael Spitsin
 * @since 2015-08-07
 */
public interface RecyclerViewFirstItemFinder {
    int findFirstVisibleItemPosition(RecyclerView recyclerView);
}
