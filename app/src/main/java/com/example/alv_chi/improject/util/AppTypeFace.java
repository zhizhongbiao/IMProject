package com.example.alv_chi.improject.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;


/**
 * Created by Alv_chi on 2017/1/14.
 */

public class AppTypeFace {

    private static final String TAG="AppTypeFace";

    // make a cache for the TypeFace
    private static Hashtable<String, Typeface> cache = new Hashtable<>();

    /**
     *
     * @param context
     * @param ttfFileAssetsPath a ttf file path (in the Assets)
     * @return the TypeFace that is loaded from given path
     */
    public static Typeface getTypeFace(Context context, String ttfFileAssetsPath) {
        if (context == null || ttfFileAssetsPath == null) {
            return null;
        }
        synchronized (AppTypeFace.class) {
            if (!cache.containsKey(ttfFileAssetsPath)) {
                try {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(), ttfFileAssetsPath);
                    cache.put(ttfFileAssetsPath, typeface);
                } catch (Exception e) {
                    Log.e(TAG, "getTypeFace: load Assets resource error" );
                    return null;
                }
            }
            return cache.get(ttfFileAssetsPath);
        }
    }


}
