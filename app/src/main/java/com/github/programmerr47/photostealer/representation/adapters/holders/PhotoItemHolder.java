package com.github.programmerr47.photostealer.representation.adapters.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class PhotoItemHolder extends RecyclerView.ViewHolder {

    private ImageView mImageContainer;

    public PhotoItemHolder(View itemView, int photoId) {
        super(itemView);

        mImageContainer = (ImageView) itemView.findViewById(photoId);
    }

    public ImageView getImageContainer() {
        return mImageContainer;
    }
}
