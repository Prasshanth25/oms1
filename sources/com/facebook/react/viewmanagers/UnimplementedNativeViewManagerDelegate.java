package com.facebook.react.viewmanagers;

import android.view.View;
import androidx.autofill.HintConstants;
import com.facebook.react.uimanager.BaseViewManagerDelegate;
import com.facebook.react.uimanager.BaseViewManagerInterface;
import com.facebook.react.viewmanagers.UnimplementedNativeViewManagerInterface;

/* loaded from: classes.dex */
public class UnimplementedNativeViewManagerDelegate<T extends View, U extends BaseViewManagerInterface<T> & UnimplementedNativeViewManagerInterface<T>> extends BaseViewManagerDelegate<T, U> {
    public UnimplementedNativeViewManagerDelegate(BaseViewManagerInterface baseViewManagerInterface) {
        super(baseViewManagerInterface);
    }

    @Override // com.facebook.react.uimanager.BaseViewManagerDelegate, com.facebook.react.uimanager.ViewManagerDelegate
    public void setProperty(T t, String str, Object obj) {
        str.hashCode();
        if (str.equals(HintConstants.AUTOFILL_HINT_NAME)) {
            ((UnimplementedNativeViewManagerInterface) this.mViewManager).setName(t, obj == null ? "" : (String) obj);
        } else {
            super.setProperty(t, str, obj);
        }
    }
}
