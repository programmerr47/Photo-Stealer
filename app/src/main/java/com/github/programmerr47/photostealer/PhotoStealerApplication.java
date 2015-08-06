package com.github.programmerr47.photostealer;

import android.app.Application;
import android.content.Context;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class PhotoStealerApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return mContext;
    }
}
