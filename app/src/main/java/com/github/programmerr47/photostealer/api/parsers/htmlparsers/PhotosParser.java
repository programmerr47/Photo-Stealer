package com.github.programmerr47.photostealer.api.parsers.htmlparsers;

import com.github.programmerr47.photostealer.representation.adapters.items.PhotoItem;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2015-08-06
 */
public class PhotosParser extends HTMLParser<List<PhotoItem>> {

    private static final String IMG_TAG = "img";

    private SinglePhotoParser mSinglePhotoParser = new SinglePhotoParser();

    @Override
    protected List<PhotoItem> parseObjectFromDoc(Element element) {
        Elements imgs = element.getElementsByTag(IMG_TAG);

        List<PhotoItem> result = new ArrayList<>();
        for (Element img : imgs) {
            PhotoItem resultItem = mSinglePhotoParser.parseObjectFromDoc(img);

            if (resultItem != null) {
                result.add(resultItem);
            }
        }

        return result;
    }
}
