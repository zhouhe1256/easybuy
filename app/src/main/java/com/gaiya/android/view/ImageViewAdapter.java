package com.gaiya.android.view;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;


import com.gaiya.android.remote.Http;
import com.gaiya.android.remote.IContentDecoder;
import com.gaiya.easybuy.R;

public class ImageViewAdapter {
    public static interface UpdatePredicate {
        boolean accept(ImageViewAdapter adapter, Bitmap bitmap);
    }

    public static class NullUpdatePredicate implements UpdatePredicate {

        @Override
        public boolean accept(ImageViewAdapter adapter, Bitmap bitmap) {
            return true;
        }
    }

    private static volatile long tagSequence;

    private final ImageView imageView;
    private final Http http;
    private final String host;
    private final String url;

    private int defaultImageId;
    private int errorImageId;

    private IContentDecoder<Bitmap> contentDecoder;
    private com.gaiya.android.async.IPromise promise;

    private UpdatePredicate predicate = new NullUpdatePredicate();
    private boolean cacheFlag=true;

    private long tag;

    public boolean isCacheFlag() {
        return cacheFlag;
    }

    public void setCacheFlag(boolean cacheFlag) {
        this.cacheFlag = cacheFlag;
    }

    public ImageViewAdapter(ImageView imageView, String url, int defaultImageId,boolean isCache) {
        this(imageView, null, url, defaultImageId, 0,isCache);
    }

    public ImageViewAdapter(ImageView imageView, String host, String url, int defaultImageId,boolean isCache) {
        this(imageView, host, url, defaultImageId, 0,isCache);
    }

    public ImageViewAdapter(ImageView imageView, String url, int defaultImageId, int errorImageId,boolean isCache) {
        this(imageView, Http.imageInstance(), null, url, defaultImageId, errorImageId, null,isCache);
    }

    public ImageViewAdapter(ImageView imageView, String host, String url, int defaultImageId, int errorImageId,boolean isCache) {
        this(imageView, Http.imageInstance(), host, url, defaultImageId, errorImageId, null,isCache);
    }

    public ImageViewAdapter(ImageView imageView, Http http, String url, int defaultImageId,boolean isCache) {
        this(imageView, http, null, url, defaultImageId, 0, null,isCache);
    }

    public ImageViewAdapter(ImageView imageView, Http http, String host, String url, int defaultImageId,boolean isCache) {
        this(imageView, http, host, url, defaultImageId, 0, null,isCache);
    }

    public ImageViewAdapter(ImageView imageView, String host, String url, IContentDecoder<Bitmap> contentDecoder,boolean isCache) {
        this(imageView, Http.imageInstance(), host, url, 0, 0, contentDecoder,isCache);
    }

    public ImageViewAdapter(ImageView imageView, String url, IContentDecoder<Bitmap> contentDecoder,boolean isCache) {
        this(imageView, Http.imageInstance(), null, url, 0, 0, contentDecoder,isCache);
    }

    public ImageViewAdapter(ImageView imageView, Http http, String host, String url, IContentDecoder<Bitmap> contentDecoder,boolean isCache) {
        this(imageView, http, host, url, 0, 0, contentDecoder,isCache);
    }

    public ImageViewAdapter(ImageView imageView, Http http, String host, String url, int defaultImageId, int errorImageId, IContentDecoder<Bitmap> contentDecoder,boolean isCache) {
        this(imageView, http, host, url, defaultImageId, errorImageId, contentDecoder, new NullUpdatePredicate(),isCache);
    }

    public ImageViewAdapter(ImageView imageView, Http http, String host, String url, int defaultImageId, int errorImageId, IContentDecoder<Bitmap> contentDecoder, UpdatePredicate predicate,boolean isCache) {
        tag = getNextTag();
        imageView.setTag(R.string.image_view_tag, tag);

        this.imageView = imageView;
        this.http = http;
        this.host = host;
        this.cacheFlag=isCache;
        if (this.host != null) {
            this.url = host + url;
        } else {
            this.url = url;
        }
        this.defaultImageId = defaultImageId;
        this.errorImageId = errorImageId;

        this.contentDecoder = contentDecoder;
        if (predicate == null) {
            predicate = new NullUpdatePredicate();
        }
        this.predicate = predicate;

        if (TextUtils.isEmpty(url)) {
            if (defaultImageId == 0) {
                throw new IllegalArgumentException("url can't be empty");
            }
            imageView.setImageResource(defaultImageId);
            return;
        }

        loadImage();
    }

    private synchronized long getNextTag() {
        return tagSequence++;
    }


    private void loadImage() {
        if (defaultImageId != 0) {
            imageView.setImageResource(defaultImageId);
        }

        com.gaiya.android.remote.HttpClient httpClient = http.get(url).isCache(cacheFlag);
        if (contentDecoder != null) {
            httpClient.contentDecoder(contentDecoder);
        }
        promise = httpClient.run().done(new com.gaiya.android.async.ICallback() {
            @Override
            public void call(com.gaiya.android.async.Arguments arguments) {
                if (isImageViewChanged()) {
                    return;
                }

                Bitmap bitmap = arguments.get(0);
                if (predicate.accept(ImageViewAdapter.this, bitmap)) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }).fail(new com.gaiya.android.async.ICallback() {
            @Override
            public void call(com.gaiya.android.async.Arguments arguments) {
                if (isImageViewChanged()) {
                    return;
                }

                if (errorImageId != 0) {
                    if (predicate.accept(ImageViewAdapter.this, null)) {
                        imageView.setImageResource(errorImageId);
                    }
                    return;
                }

                if (defaultImageId != 0) {
                    if (predicate.accept(ImageViewAdapter.this, null)) {
                        imageView.setImageResource(defaultImageId);
                    }
                }
            }
        });
    }

    private boolean isImageViewChanged() {
        Long currentTag = (Long) imageView.getTag(R.string.image_view_tag);
        if (currentTag == null) {
            return true;
        }
        return !currentTag.equals(tag);
    }


    public void setPredicate(UpdatePredicate predicate) {
        this.predicate = predicate;
    }

    public com.gaiya.android.async.IPromise getPromise() {
        return promise;
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String url,boolean isCache) {
        return adapt(imageView, null, url, 0,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String host, String url,boolean isCache) {
        return adapt(imageView, host, url, 0,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String url, int defaultImageId,boolean isCache) {
        return adapt(imageView, null, url, defaultImageId, 0,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String host, String url, int defaultImageId,boolean isCache) {
        return adapt(imageView, host, url, defaultImageId, 0,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String url, int defaultImageId, int errorImageId,boolean isCache) {
        return adapt(imageView, null, url, defaultImageId, errorImageId, null, null,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String host, String url, int defaultImageId, int errorImageId,boolean isCache) {
        return adapt(imageView, host, url, defaultImageId, errorImageId, null, null,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String url, int defaultImageId, int errorImageId, UpdatePredicate predicate,boolean isCache) {
        return adapt(imageView, null, url, defaultImageId, errorImageId, null, predicate,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String host, String url, int defaultImageId, int errorImageId, UpdatePredicate predicate,boolean isCache) {
        return adapt(imageView, host, url, defaultImageId, errorImageId, null, predicate,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String host, String url, int defaultImageId, int errorImageId, IContentDecoder<Bitmap> contentDecoder, UpdatePredicate predicate,boolean isCache) {
        ImageViewAdapter adapter = new ImageViewAdapter(imageView, Http.imageInstance(), host, url, defaultImageId, errorImageId, contentDecoder, predicate,isCache);
        return adapter.getPromise();
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String url, int defaultImageId, int errorImageId, IContentDecoder<Bitmap> contentDecoder, UpdatePredicate predicate,boolean isCache) {
        ImageViewAdapter adapter = new ImageViewAdapter(imageView, Http.imageInstance(), null, url, defaultImageId, errorImageId, contentDecoder, predicate,isCache);
        return adapter.getPromise();
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String url, IContentDecoder<Bitmap> contentDecoder,boolean isCache) {
        return adapt(imageView, null, url, 0, 0, contentDecoder, null,isCache);
    }

    public static com.gaiya.android.async.IPromise adapt(ImageView imageView, String host, String url, IContentDecoder<Bitmap> contentDecoder,boolean isCache) {
        return adapt(imageView, host, url, 0, 0, contentDecoder, null,isCache);
    }
}