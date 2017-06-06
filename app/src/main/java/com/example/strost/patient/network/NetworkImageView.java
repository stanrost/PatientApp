package com.example.strost.patient.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.Serializable;
import java.lang.ref.WeakReference;

public class NetworkImageView extends AppCompatImageView implements Serializable {

    private WeakReference<Context> context;

    public NetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = new WeakReference<Context>(context);
    }

    public void setUrl(String url) {
        Picasso.with(context.get()).load(url).into(target);
    }

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setImageBitmap(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            setImageBitmap(null);
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {

        }
    };

}
