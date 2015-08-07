package com.github.programmerr47.photostealer.representation.adapters.items;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.programmerr47.photostealer.R;
import com.github.programmerr47.photostealer.representation.PhotoStealerApplication;
import com.github.programmerr47.photostealer.representation.adapters.holders.PhotoItemHolder;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class PhotoItem {

    private Uri mPhotoUri;

    public static PhotoItem createInstance(Uri photoUri) {
        PhotoItem result = new PhotoItem();
        result.mPhotoUri = photoUri;
        return result;
    }

    public void bindView(PhotoItemHolder holder, @SuppressWarnings("unused") int position) {
        PhotoStealerApplication.getImageLoader().displayImage(mPhotoUri.toString(), holder.getImageContainer());
    }

    public static PhotoItemHolder produce(ViewGroup parentView, int dimension) {
        LayoutInflater layoutInflater = LayoutInflater.from(parentView.getContext());
        View view = layoutInflater.inflate(R.layout.item_photo, parentView, false);

        if (view == null) {
            throw new IllegalStateException("View not created");
        }

        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = dimension;
        params.height = dimension;

        return new PhotoItemHolder(view, R.id.photo);
    }
}
