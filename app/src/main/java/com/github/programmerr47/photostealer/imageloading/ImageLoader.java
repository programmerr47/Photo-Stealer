package com.github.programmerr47.photostealer.imageloading;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Process;
import android.util.Pair;
import android.widget.ImageView;

import com.github.programmerr47.photostealer.R;
import com.github.programmerr47.photostealer.imageloading.cache.FileCache;
import com.github.programmerr47.photostealer.imageloading.cache.MemoryCache;
import com.github.programmerr47.photostealer.util.BitmapUtils;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Michael Spitsin
 * @since 2014-08-24
 */
public class ImageLoader {

    private static final int FADE_IN_TIME = 200;
    private static final int NUMBER_OF_THREADS = 3;
    private static final int LOADING_IMAGE_RES = R.drawable.photo_placeholder;

    private MemoryCache mMemoryCache;
    private FileCache mFileCache;

    private Map<ImageView, String> mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    private ExecutorService mExecutorService;
    private Handler uiHandler;

    public ImageLoader(Context context) {
        mMemoryCache = new MemoryCache();
        mFileCache = new FileCache(context);

        mExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        uiHandler = new Handler();
    }

    public void displayImage(String url, ImageView imageView) {
        mImageViews.put(imageView, url);

        Bitmap bitmap = mMemoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            mExecutorService.submit(new SingleImageLoader(url, imageView));
            imageView.setImageResource(LOADING_IMAGE_RES);
        }
    }

    public void clearCaches() {
        mMemoryCache.clear();
        mFileCache.clear();
    }

    public void clearMemoryCache() {
        mMemoryCache.clear();
    }

    private class SingleImageLoader implements Runnable {
        Pair<String, ImageView> mUrlViewBundle;

        public SingleImageLoader(String url, ImageView imageView) {
            mUrlViewBundle = new Pair<>(url, imageView);
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            if (!isImageViewReused()) {
                final Bitmap bitmap = getBitmap(mUrlViewBundle.first);
                mMemoryCache.put(mUrlViewBundle.first, bitmap);

                if (!isImageViewReused()) {
                    Drawable imageViewDrawable = mUrlViewBundle.second.getDrawable();
                    if (imageViewDrawable == null) {
                        imageViewDrawable = new ColorDrawable(android.R.color.transparent);
                    }

                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[] {
                                    imageViewDrawable,
                                    new BitmapDrawable(bitmap)
                            });

                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (bitmap != null) {
                                mUrlViewBundle.second.setImageDrawable(td);
                                mUrlViewBundle.second.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                td.startTransition(FADE_IN_TIME);
                            } else {
                                mUrlViewBundle.second.setImageResource(LOADING_IMAGE_RES);
                            }
                        }
                    });
                }
            }
        }

        private boolean isImageViewReused() {
            if (mImageViews.containsKey(mUrlViewBundle.second)) {
                String currentLoaderOrInQueueUrl = mImageViews.get(mUrlViewBundle.second);

                if (currentLoaderOrInQueueUrl.equals(mUrlViewBundle.first)) {
                    return false;
                }
            }

            return true;
        }

        private Bitmap getBitmap(String url)
        {
            File imageFile = mFileCache.getFile(url);
            Bitmap bitmap = BitmapUtils.decodeFile(imageFile);

            if(bitmap != null) {
                return bitmap;
            } else {
                return BitmapUtils.getBitmapFromInternet(url, imageFile, mMemoryCache);
            }
        }
    }
}
