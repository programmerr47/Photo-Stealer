package com.github.programmerr47.photostealer.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class Utils {

    /**
     * Converts given input stream (<strong>without</strong> closing it) to {@link String}.
     *
     * @param is given input stream
     * @return converted stream to string
     */
    public static String covertInputStreamToString(InputStream is) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String line;
        String result = "";

        try {
            while((line = bufferedReader.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            //ignored
        }

        return result;
    }

    public static void forceClearFocusOnView(Context applicationContext, View view) {
        view.clearFocus();
        InputMethodManager imm = (InputMethodManager) applicationContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
