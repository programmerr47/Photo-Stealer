package com.github.programmerr47.photostealer.imageloading.cache;

import android.content.Context;
import android.os.Environment;

import com.github.programmerr47.photostealer.util.BitmapUtils;

import java.io.File;

/**
 *
 * @author Michael Spitsin
 * @since 2014-08-24
 */
public class FileCache {
    private static final int DISK_SIZE_LIMIT = 40 * 1000 * 1000; //40MB

    private File mCacheDir;

    private int mAllocatedDiskSize = 0;
    private int mDiskSizeLimit = DISK_SIZE_LIMIT;

    public FileCache(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            mCacheDir = context.getExternalCacheDir();
        } else {
            mCacheDir = context.getCacheDir();
        }
    }

    /**
     * Gets file that corresponds to given url. Name of file is hashcode of url.
     * <br>
     * <strong>Note: </strong> there may be conflicts. But the probability of this is min.
     * So for demonstration it is simple and clear solution.
     *
     * @param url given url of file
     * @return file (it does not necessarily exist)
     */
    public File getFile(String url) {
        mAllocatedDiskSize = BitmapUtils.dirSize(mCacheDir);
        while (mAllocatedDiskSize > mDiskSizeLimit) {
            File[] files = mCacheDir.listFiles();
            File oldestFile = null;
            for (File file : files) {
                if (!file.isDirectory()) {
                    if (oldestFile == null) {
                        oldestFile = file;
                    } else {
                        if (oldestFile.lastModified() > file.lastModified()) {
                            oldestFile = file;
                        }
                    }
                }
            }

            if (oldestFile != null) {
                mAllocatedDiskSize -= oldestFile.length();
                oldestFile.delete();

                if (mAllocatedDiskSize <= 0) {
                    mAllocatedDiskSize = 0;
                    break;
                }
            } else {
                break;
            }
        }

        String fileName = url.hashCode() + "";
        return new File(mCacheDir, fileName);
    }

    /**
     * Deletes all files in disk cache.
     */
    public void clear() {
        File[] files = mCacheDir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (!file.isDirectory()) {
                    file.delete();
                }
            }
        }
    }
}
