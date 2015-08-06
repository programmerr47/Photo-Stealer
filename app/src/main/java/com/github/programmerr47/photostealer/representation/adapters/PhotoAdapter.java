package com.github.programmerr47.photostealer.representation.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.github.programmerr47.photostealer.representation.adapters.holders.PhotoItemHolder;
import com.github.programmerr47.photostealer.representation.adapters.items.PhotoItem;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoItemHolder> {

    private List<PhotoItem> mPhotoItems;
    private int mItemDimension;

    public PhotoAdapter(List<PhotoItem> photoItems, int itemDimension) {
        mPhotoItems = photoItems;
        mItemDimension = itemDimension;
    }

    @Override
    public PhotoItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return PhotoItem.produce(parent, mItemDimension);
    }

    @Override
    public void onBindViewHolder(PhotoItemHolder holder, int position) {
        PhotoItem item = mPhotoItems.get(position);
        item.bindView(holder, position);
    }

    @Override
    public int getItemCount() {
        return mPhotoItems.size();
    }
}
