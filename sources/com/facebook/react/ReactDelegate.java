package com.facebook.react;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.devsupport.DoubleTapReloadRecognizer;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

/* loaded from: classes.dex */
public class ReactDelegate {
    private final Activity mActivity;
    private DoubleTapReloadRecognizer mDoubleTapReloadRecognizer;
    private boolean mFabricEnabled;
    private Bundle mLaunchOptions;
    private final String mMainComponentName;
    private ReactNativeHost mReactNativeHost;
    private ReactRootView mReactRootView;

    public ReactDelegate(Activity activity, ReactNativeHost reactNativeHost, String str, Bundle bundle) {
        this.mFabricEnabled = false;
        this.mActivity = activity;
        this.mMainComponentName = str;
        this.mLaunchOptions = bundle;
        this.mDoubleTapReloadRecognizer = new DoubleTapReloadRecognizer();
        this.mReactNativeHost = reactNativeHost;
    }

    public ReactDelegate(Activity activity, ReactNativeHost reactNativeHost, String str, Bundle bundle, boolean z) {
        this.mFabricEnabled = false;
        this.mActivity = activity;
        this.mMainComponentName = str;
        this.mLaunchOptions = composeLaunchOptions(bundle);
        this.mDoubleTapReloadRecognizer = new DoubleTapReloadRecognizer();
        this.mReactNativeHost = reactNativeHost;
        this.mFabricEnabled = z;
    }

    public void onHostResume() {
        if (getReactNativeHost().hasInstance()) {
            if (this.mActivity instanceof DefaultHardwareBackBtnHandler) {
                ReactInstanceManager reactInstanceManager = getReactNativeHost().getReactInstanceManager();
                Activity activity = this.mActivity;
                reactInstanceManager.onHostResume(activity, (DefaultHardwareBackBtnHandler) activity);
                return;
            }
            throw new ClassCastException("Host Activity does not implement DefaultHardwareBackBtnHandler");
        }
    }

    public void onHostPause() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostPause(this.mActivity);
        }
    }

    public void onHostDestroy() {
        ReactRootView reactRootView = this.mReactRootView;
        if (reactRootView != null) {
            reactRootView.unmountReactApplication();
            this.mReactRootView = null;
        }
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onHostDestroy(this.mActivity);
        }
    }

    public boolean onBackPressed() {
        if (getReactNativeHost().hasInstance()) {
            getReactNativeHost().getReactInstanceManager().onBackPressed();
            return true;
        }
        return false;
    }

    public void onActivityResult(int i, int i2, Intent intent, boolean z) {
        if (getReactNativeHost().hasInstance() && z) {
            getReactNativeHost().getReactInstanceManager().onActivityResult(this.mActivity, i, i2, intent);
        }
    }

    public void loadApp() {
        loadApp(this.mMainComponentName);
    }

    public void loadApp(String str) {
        if (this.mReactRootView != null) {
            throw new IllegalStateException("Cannot loadApp while app is already running.");
        }
        ReactRootView createRootView = createRootView();
        this.mReactRootView = createRootView;
        createRootView.startReactApplication(getReactNativeHost().getReactInstanceManager(), str, this.mLaunchOptions);
    }

    public ReactRootView getReactRootView() {
        return this.mReactRootView;
    }

    protected ReactRootView createRootView() {
        ReactRootView reactRootView = new ReactRootView(this.mActivity);
        reactRootView.setIsFabric(isFabricEnabled());
        return reactRootView;
    }

    public boolean shouldShowDevMenuOrReload(int i, KeyEvent keyEvent) {
        if (getReactNativeHost().hasInstance() && getReactNativeHost().getUseDeveloperSupport()) {
            if (i == 82) {
                getReactNativeHost().getReactInstanceManager().showDevOptionsDialog();
                return true;
            } else if (((DoubleTapReloadRecognizer) Assertions.assertNotNull(this.mDoubleTapReloadRecognizer)).didDoubleTapR(i, this.mActivity.getCurrentFocus())) {
                getReactNativeHost().getReactInstanceManager().getDevSupportManager().handleReloadJS();
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private ReactNativeHost getReactNativeHost() {
        return this.mReactNativeHost;
    }

    public ReactInstanceManager getReactInstanceManager() {
        return getReactNativeHost().getReactInstanceManager();
    }

    protected boolean isFabricEnabled() {
        return this.mFabricEnabled;
    }

    private Bundle composeLaunchOptions(Bundle bundle) {
        if (isFabricEnabled()) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean("concurrentRoot", true);
        }
        return bundle;
    }
}
