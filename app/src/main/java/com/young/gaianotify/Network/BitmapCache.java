package com.young.gaianotify.Network;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by young on 2016/3/26 0026.
 */
public class BitmapCache implements ImageLoader.ImageCache {


    public LruCache<String ,Bitmap> cache;
    public  int max=30*1024*1024;

    public BitmapCache() {
        cache = new LruCache<String ,Bitmap>(max)
        {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

    }

    @Override
    public Bitmap getBitmap(String url) {
        return cache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {

        cache.put(url, bitmap);

    }
}
