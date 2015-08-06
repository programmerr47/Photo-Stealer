package com.github.programmerr47.photostealer.representation.tasks;

import android.util.Pair;

import com.github.programmerr47.photostealer.api.ApiMethod;

/**
 * @author Michael Spitsin
 * @since 2014-09-13
 */
public class ApiGetMethodTask<Response> extends AsyncTaskWithListener<ApiMethod<Response>, Void, Response> {

    //TODO add accepting several params to execute several ApiMethods
    @Override
    protected Response doInBackground(ApiMethod<Response>... params) {
        if (params.length > 0) {
            ApiMethod<Response> request = params[0];

            if (request != null) {
                try {
                    return request.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
