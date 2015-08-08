package com.github.programmerr47.photostealer.api;

import android.support.annotation.NonNull;

import com.github.programmerr47.photostealer.api.parsers.ResponseParser;
import com.github.programmerr47.photostealer.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class ApiGetMethod<Response> implements ApiMethod<Response> {

    private String mUrl;
    private ResponseParser<Response> mResultParser;

    @Override
    public Response execute() throws Exception {
        if (mUrl != null) {
            try {
                URL url;
                try {
                    url = new URL(mUrl);
                } catch (MalformedURLException e) {
                    url = new URL("http://" + mUrl);
                }
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
                if (connection.getResponseCode() == 200) {
                    InputStream responseStream = connection.getInputStream();

                    if (responseStream != null) {
                        String responseString = Utils.covertInputStreamToString(responseStream);
                        responseStream.close();
                        return mResultParser.parseObjectFrom(responseString);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Sets method url base on type of method. Basically all api method
     * has common part of url (it is a API url), but for more flexibility
     * we has not setMethodName method but setUrl method for maintenance cases
     * when you has no official api and need to parse some page for example.
     *
     * @param url method url
     */
    public ApiGetMethod<Response> setUrl(@NonNull String url) {
        mUrl = url;
        return this;
    }

    /**
     * Sets parser from metadata that comes as a result of urlMethod.
     * This metadata can be JSON, XML or just HTML page layout. So for special
     * method special parser must be specified.
     *
     * @param parser method result parser
     */
    public ApiGetMethod<Response> setMethodResultParser(@NonNull ResponseParser<Response> parser) {
        mResultParser = parser;
        return this;
    }
}
