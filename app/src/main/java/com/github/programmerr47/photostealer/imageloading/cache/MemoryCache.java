package com.github.programmerr47.photostealer.imageloading.cache;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Michael Spitsin
 * @since 2014-08-24
 */
public class MemoryCache {
    private static final int MEMORY_SIZE_LIMIT = 8 * 1000 * 1000; //8MB

    private Map<String, Bitmap> mCache = Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>());

    private int mAllocatedMemorySize = 0;
    private int mMemorySizeLimit = MEMORY_SIZE_LIMIT;

    public void setLimit(int memorySizeLimit) {
        mMemorySizeLimit = memorySizeLimit;
    }

    public synchronized Bitmap get(String key) {
        if (mCache.containsKey(key)) {
            return mCache.get(key);
        }

        return null;
    }

    public synchronized void put(String key, Bitmap bitmap) {
        if (!mCache.containsKey(key)) {
            mCache.put(key, bitmap);
            mAllocatedMemorySize += getBitmapSize(bitmap);
            checkSize();
        }
    }

    public void clear() {
        mCache.clear();
        mAllocatedMemorySize = 0;
    }

    /**
     * Retrieves size of given bitmap in bytes.
     *
     * @param bitmap given bitmap
     * @return size in bytes or zero if butmap is null
     */
    private int getBitmapSize(Bitmap bitmap) {
        if (bitmap != null) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        }

        return 0;
    }

    /**
     * Checks allocated memory size. If the size of used memory over the limit,
     * then the items are removing from the first until the memory usage does
     * not fall below the limit.
     */
    private void checkSize() {
        if (mAllocatedMemorySize > mMemorySizeLimit) {
            for (Iterator<Map.Entry<String, Bitmap>> iterator = mCache.entrySet().iterator(); iterator.hasNext();) {
                Map.Entry<String, Bitmap> entry = iterator.next();
                mAllocatedMemorySize -= getBitmapSize(entry.getValue());
                iterator.remove();

                if (mAllocatedMemorySize < mMemorySizeLimit) {
                    break;
                }
            }
        }
    }
}
