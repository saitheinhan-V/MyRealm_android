package com.example.myrealm;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.luck.picture.lib.engine.ImageEngine;

/**
 * Create By 胡
 * on 2020/4/10 0010
 */
public class GlideEngine implements ImageEngine {
    /**
     * 加载图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, ImageView imageView, String url, int i, int i1) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }

    @Override
    public void loadAlbumCover(Context context, String s, ImageView imageView) {

    }




    /**
     * 加载图片列表图片
     *
     * @param context   上下文
     * @param url       图片路径
     * @param imageView 承载图片ImageView
     */
    @Override
    public void loadGridImage(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView) {
        Glide.with(context)
                .load(url)
//                .apply(new RequestOptions().placeholder(R.drawable.picture_image_placeholder))
                .into(imageView);
    }

    @Override
    public void pauseRequests(Context context) {

    }

    @Override
    public void resumeRequests(Context context) {

    }


    private GlideEngine() {
    }

    private static GlideEngine instance;

    public static GlideEngine createGlideEngine() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }
}
