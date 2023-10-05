package com.swmansion.reanimated.nativeProxy;

import com.facebook.jni.HybridData;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/* loaded from: classes.dex */
public class EventHandler implements RCTEventEmitter {
    UIManagerModule.CustomEventNamesResolver mCustomEventNamesResolver;
    private final HybridData mHybridData;

    public native void receiveEvent(String str, WritableMap writableMap);

    @Override // com.facebook.react.uimanager.events.RCTEventEmitter
    public void receiveTouches(String str, WritableArray writableArray, WritableArray writableArray2) {
    }

    private EventHandler(HybridData hybridData) {
        this.mHybridData = hybridData;
    }

    @Override // com.facebook.react.uimanager.events.RCTEventEmitter
    public void receiveEvent(int i, String str, WritableMap writableMap) {
        String resolveCustomEventName = this.mCustomEventNamesResolver.resolveCustomEventName(str);
        receiveEvent(i + resolveCustomEventName, writableMap);
    }
}
