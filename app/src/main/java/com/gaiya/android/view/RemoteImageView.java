package com.gaiya.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class RemoteImageView extends ImageView {
    private String url;
    private com.gaiya.android.async.IPromise promise;

    private int defaultImageId;
    private int errorImageId;

    public RemoteImageView(Context context) {
        super(context);
    }

    public RemoteImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RemoteImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public com.gaiya.android.async.IPromise getPromise() {
        return promise;
    }

    public void setUrl(String url) {
        this.url = url;

        promise = ImageViewAdapter.adapt(this, this.url, defaultImageId, errorImageId,true);
    }

}
