package com.dylanvann.fastimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.Request;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/* loaded from: classes.dex */
public class FastImageViewWithUrl extends AppCompatImageView {
    public GlideUrl glideUrl;
    private Drawable mDefaultSource;
    private boolean mNeedsReload;
    private ReadableMap mSource;

    public FastImageViewWithUrl(Context context) {
        super(context);
        this.mNeedsReload = false;
        this.mSource = null;
        this.mDefaultSource = null;
    }

    public void setSource(ReadableMap readableMap) {
        this.mNeedsReload = true;
        this.mSource = readableMap;
    }

    public void setDefaultSource(Drawable drawable) {
        this.mNeedsReload = true;
        this.mDefaultSource = drawable;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public void onAfterUpdate(@Nonnull FastImageViewManager fastImageViewManager, RequestManager requestManager, @Nonnull Map<String, List<FastImageViewWithUrl>> map) {
        if (this.mNeedsReload) {
            ReadableMap readableMap = this.mSource;
            if ((readableMap == null || !readableMap.hasKey("uri") || isNullOrEmpty(this.mSource.getString("uri"))) && this.mDefaultSource == null) {
                clearView(requestManager);
                GlideUrl glideUrl = this.glideUrl;
                if (glideUrl != null) {
                    FastImageOkHttpProgressGlideModule.forget(glideUrl.toStringUrl());
                }
                setImageDrawable(null);
                return;
            }
            FastImageSource imageSource = FastImageViewConverter.getImageSource(getContext(), this.mSource);
            if (imageSource != null && imageSource.getUri().toString().length() == 0) {
                int id = getId();
                WritableNativeMap writableNativeMap = new WritableNativeMap();
                writableNativeMap.putString("message", "Invalid source prop:" + this.mSource);
                ((RCTEventEmitter) ((ThemedReactContext) getContext()).getJSModule(RCTEventEmitter.class)).receiveEvent(id, "onFastImageError", writableNativeMap);
                clearView(requestManager);
                GlideUrl glideUrl2 = this.glideUrl;
                if (glideUrl2 != null) {
                    FastImageOkHttpProgressGlideModule.forget(glideUrl2.toStringUrl());
                }
                setImageDrawable(null);
                return;
            }
            GlideUrl glideUrl3 = imageSource == null ? null : imageSource.getGlideUrl();
            this.glideUrl = glideUrl3;
            clearView(requestManager);
            String stringUrl = glideUrl3 == null ? null : glideUrl3.toStringUrl();
            if (glideUrl3 != null) {
                FastImageOkHttpProgressGlideModule.expect(stringUrl, fastImageViewManager);
                List<FastImageViewWithUrl> list = map.get(stringUrl);
                if (list != null && !list.contains(this)) {
                    list.add(this);
                } else if (list == null) {
                    map.put(stringUrl, new ArrayList(Collections.singletonList(this)));
                }
            }
            ThemedReactContext themedReactContext = (ThemedReactContext) getContext();
            if (imageSource != null) {
                ((RCTEventEmitter) themedReactContext.getJSModule(RCTEventEmitter.class)).receiveEvent(getId(), "onFastImageLoadStart", new WritableNativeMap());
            }
            if (requestManager != null) {
                RequestBuilder<Drawable> apply = requestManager.load(imageSource != null ? imageSource.getSourceForLoad() : null).apply((BaseRequestOptions<?>) FastImageViewConverter.getOptions(themedReactContext, imageSource, this.mSource).placeholder(this.mDefaultSource).fallback(this.mDefaultSource));
                if (stringUrl != null) {
                    apply.listener(new FastImageRequestListener(stringUrl));
                }
                apply.into(this);
            }
        }
    }

    public void clearView(RequestManager requestManager) {
        if (requestManager == null || getTag() == null || !(getTag() instanceof Request)) {
            return;
        }
        requestManager.clear(this);
    }
}
