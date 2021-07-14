package com.example.comics;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class JavaUtils {

    public static void setupPicassoCaching (Context ctx) {
        Picasso.Builder builder = new Picasso.Builder(ctx);
        builder.downloader(new OkHttp3Downloader(ctx,Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);
    }
}
