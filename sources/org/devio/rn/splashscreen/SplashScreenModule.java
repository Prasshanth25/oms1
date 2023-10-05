package org.devio.rn.splashscreen;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/* loaded from: classes2.dex */
public class SplashScreenModule extends ReactContextBaseJavaModule {
    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return "SplashScreen";
    }

    public SplashScreenModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
    }

    @ReactMethod
    public void show() {
        SplashScreen.show(getCurrentActivity());
    }

    @ReactMethod
    public void hide() {
        SplashScreen.hide(getCurrentActivity());
    }
}
