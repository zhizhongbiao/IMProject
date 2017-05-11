package com.example.alv_chi.improject.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alv_chi.improject.R;

/**
 * Created by Alv_chi on 2017/5/5.
 */

public class GlideUtil {


    public static void loadImage(Context context, String url, ImageView iv) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .error(R.mipmap.error)
                .placeholder(R.mipmap.loading)
                .into(iv);
    }

}
