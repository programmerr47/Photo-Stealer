package com.github.programmerr47.photostealer.representation;

import android.app.Application;
import android.content.Context;

import com.github.programmerr47.photostealer.imageloading.ImageLoader;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class PhotoStealerApplication extends Application {

    private static Context context;
    private static ImageLoader imageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        imageLoader = new ImageLoader(context);
    }

    public static Context getAppContext() {
        return context;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }
}
