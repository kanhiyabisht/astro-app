package com.example.astrodashalib.provider.uil;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Created by astrology on 27/03/17.
 */

public class UILProvider {


    public static void initUIL(@NonNull Context context) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                    .defaultDisplayImageOptions(getOptions())
                    .denyCacheImageMultipleSizesInMemory();
            imageLoader.init(builder.build());
        }
    }

    private static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
