package com.github.programmerr47.photostealer.api.parsers.htmlparsers;

import android.net.Uri;

import com.github.programmerr47.photostealer.representation.adapters.items.PhotoItem;

import org.jsoup.nodes.Element;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class SinglePhotoParser extends HTMLParser<PhotoItem> {

    @Override
    protected PhotoItem parseObjectFromDoc(Element element) {
        String url = element.attr("abs:src");

        if (url != null) {
            if (!Uri.parse(url).toString().isEmpty()) {
                return PhotoItem.createInstance(Uri.parse(url));
            }
        }

        return null;
    }
}
