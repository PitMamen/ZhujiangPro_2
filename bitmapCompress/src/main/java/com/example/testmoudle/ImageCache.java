package com.example.testmoudle;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by zhujiang on 16-8-29.
 */
public class ImageCache extends LruCache<String, Bitmap> {

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     */
    public ImageCache(int maxSize) {
        super(maxSize);
    }


    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        // 缓存大小以kb为单位
        return bitmap.getByteCount() / 1024;
    }

    /**
     * 根据键值获取bitmap
     *
     * @param key
     * @return
     */
    public Bitmap getBitmapFromKey(String key) {
        return get(key);
    }

    /**
     * 如果缓存中没有该图片，则将bitmap添加到缓存中
     *
     * @param key
     * @param bitmap
     */
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromKey(key) == null) {
            put(key, bitmap);
        }
    }


}
