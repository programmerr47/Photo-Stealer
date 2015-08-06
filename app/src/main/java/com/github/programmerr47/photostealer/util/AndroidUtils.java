package com.github.programmerr47.photostealer.util;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.TypedValue;

import com.github.programmerr47.photostealer.PhotoStealerApplication;

/**
 * Some helpful functions related to android os.
 *
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class AndroidUtils {

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static float dpToPx(@NonNull Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float dpToPx(int dp) {
        return dpToPx(PhotoStealerApplication.getAppContext(), dp);
    }
}
