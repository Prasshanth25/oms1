package com.swmansion.rnscreens;

import android.util.Log;
import android.view.View;
import com.facebook.react.bridge.JSApplicationCausedNativeException;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerDelegate;
import com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface;
import com.swmansion.rnscreens.events.HeaderAttachedEvent;
import com.swmansion.rnscreens.events.HeaderDetachedEvent;
import java.util.Map;
import javax.annotation.Nonnull;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ScreenStackHeaderConfigViewManager.kt */
@ReactModule(name = ScreenStackHeaderConfigViewManager.REACT_CLASS)
@Metadata(d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b2\b\u0007\u0018\u0000 M2\b\u0012\u0004\u0012\u00020\u00020\u00012\b\u0012\u0004\u0012\u00020\u00020\u0003:\u0001MB\u0005¢\u0006\u0002\u0010\u0004J \u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00022\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\u0010H\u0014J\u0018\u0010\u0011\u001a\u00020\u000b2\u0006\u0010\t\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0016J\u0010\u0010\u0012\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0002H\u0016J\u000e\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006H\u0014J\u0016\u0010\u0014\u001a\u0010\u0012\u0004\u0012\u00020\u0016\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u0015H\u0016J\b\u0010\u0018\u001a\u00020\u0016H\u0016J\u0010\u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u0016H\u0002J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0002H\u0014J\u0012\u0010\u001e\u001a\u00020\b2\b\b\u0001\u0010\u001f\u001a\u00020\u0002H\u0016J\u0010\u0010 \u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0002H\u0016J\u0018\u0010!\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00022\u0006\u0010\f\u001a\u00020\rH\u0016J\u0018\u0010\"\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\u0006\u0010$\u001a\u00020\u001cH\u0017J\u001c\u0010%\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\b\u0010&\u001a\u0004\u0018\u00010\u0016H\u0016J\u001c\u0010'\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\b\u0010&\u001a\u0004\u0018\u00010\u0016H\u0016J\u001a\u0010(\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\u0006\u0010&\u001a\u00020\rH\u0016J\u001a\u0010)\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\u0006\u0010&\u001a\u00020\u001cH\u0016J\u001f\u0010*\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\b\u0010+\u001a\u0004\u0018\u00010\rH\u0017¢\u0006\u0002\u0010,J\u001f\u0010-\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\b\u0010.\u001a\u0004\u0018\u00010\rH\u0017¢\u0006\u0002\u0010,J\u001a\u0010/\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\b\u00100\u001a\u0004\u0018\u00010\u0016H\u0017J\u001a\u00101\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\u0006\u0010&\u001a\u00020\u001cH\u0016J\u0018\u00102\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\u0006\u00103\u001a\u00020\u001cH\u0017J\u0018\u00104\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\u0006\u00105\u001a\u00020\u001cH\u0017J\u0018\u00106\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\u0006\u00107\u001a\u00020\u001cH\u0017J\u001a\u00108\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\u0006\u0010&\u001a\u00020\u001cH\u0016J!\u00109\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\b\u0010&\u001a\u0004\u0018\u00010\rH\u0016¢\u0006\u0002\u0010,J!\u0010:\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\b\u0010&\u001a\u0004\u0018\u00010\rH\u0016¢\u0006\u0002\u0010,J\u001c\u0010;\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\b\u0010&\u001a\u0004\u0018\u00010\u0016H\u0016J\u001a\u0010<\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\u0006\u0010&\u001a\u00020\rH\u0016J\u001c\u0010=\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\b\u0010&\u001a\u0004\u0018\u00010\u0016H\u0016J\u001a\u0010>\u001a\u00020\b2\b\u0010\u001f\u001a\u0004\u0018\u00010\u00022\u0006\u0010&\u001a\u00020\u001cH\u0016J\u001a\u0010?\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\b\u0010@\u001a\u0004\u0018\u00010\u0016H\u0017J\u001f\u0010A\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\b\u0010B\u001a\u0004\u0018\u00010\rH\u0017¢\u0006\u0002\u0010,J\u001a\u0010C\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\b\u0010D\u001a\u0004\u0018\u00010\u0016H\u0017J\u0018\u0010E\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\u0006\u0010F\u001a\u00020\rH\u0017J\u001a\u0010G\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\b\u0010H\u001a\u0004\u0018\u00010\u0016H\u0017J\u0018\u0010I\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\u0006\u0010J\u001a\u00020\u001cH\u0017J\u0018\u0010K\u001a\u00020\b2\u0006\u0010#\u001a\u00020\u00022\u0006\u0010L\u001a\u00020\u001cH\u0017R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006N"}, d2 = {"Lcom/swmansion/rnscreens/ScreenStackHeaderConfigViewManager;", "Lcom/facebook/react/uimanager/ViewGroupManager;", "Lcom/swmansion/rnscreens/ScreenStackHeaderConfig;", "Lcom/facebook/react/viewmanagers/RNSScreenStackHeaderConfigManagerInterface;", "()V", "mDelegate", "Lcom/facebook/react/uimanager/ViewManagerDelegate;", "addView", "", "parent", "child", "Landroid/view/View;", "index", "", "createViewInstance", "reactContext", "Lcom/facebook/react/uimanager/ThemedReactContext;", "getChildAt", "getChildCount", "getDelegate", "getExportedCustomDirectEventTypeConstants", "", "", "", "getName", "logNotAvailable", "propName", "needsCustomLayoutForChildren", "", "onAfterUpdateTransaction", "onDropViewInstance", "view", "removeAllViews", "removeViewAt", "setBackButtonInCustomView", "config", "backButtonInCustomView", "setBackTitle", "value", "setBackTitleFontFamily", "setBackTitleFontSize", "setBackTitleVisible", "setBackgroundColor", ViewProps.BACKGROUND_COLOR, "(Lcom/swmansion/rnscreens/ScreenStackHeaderConfig;Ljava/lang/Integer;)V", "setColor", ViewProps.COLOR, "setDirection", "direction", "setDisableBackButtonMenu", "setHidden", ViewProps.HIDDEN, "setHideBackButton", "hideBackButton", "setHideShadow", "hideShadow", "setLargeTitle", "setLargeTitleBackgroundColor", "setLargeTitleColor", "setLargeTitleFontFamily", "setLargeTitleFontSize", "setLargeTitleFontWeight", "setLargeTitleHideShadow", "setTitle", "title", "setTitleColor", "titleColor", "setTitleFontFamily", "titleFontFamily", "setTitleFontSize", "titleFontSize", "setTitleFontWeight", "titleFontWeight", "setTopInsetEnabled", "topInsetEnabled", "setTranslucent", "translucent", "Companion", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class ScreenStackHeaderConfigViewManager extends ViewGroupManager<ScreenStackHeaderConfig> implements RNSScreenStackHeaderConfigManagerInterface<ScreenStackHeaderConfig> {
    public static final Companion Companion = new Companion(null);
    public static final String REACT_CLASS = "RNSScreenStackHeaderConfig";
    private final ViewManagerDelegate<ScreenStackHeaderConfig> mDelegate = new RNSScreenStackHeaderConfigManagerDelegate(this);

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.IViewManagerWithChildren
    public boolean needsCustomLayoutForChildren() {
        return true;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public ScreenStackHeaderConfig createViewInstance(ThemedReactContext reactContext) {
        Intrinsics.checkNotNullParameter(reactContext, "reactContext");
        return new ScreenStackHeaderConfig(reactContext);
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public void addView(ScreenStackHeaderConfig parent, View child, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(child, "child");
        if (!(child instanceof ScreenStackHeaderSubview)) {
            throw new JSApplicationCausedNativeException("Config children should be of type RNSScreenStackHeaderSubview");
        }
        parent.addConfigSubview((ScreenStackHeaderSubview) child, i);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void onDropViewInstance(@Nonnull ScreenStackHeaderConfig view) {
        Intrinsics.checkNotNullParameter(view, "view");
        view.destroy();
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public void removeAllViews(ScreenStackHeaderConfig parent) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        parent.removeAllConfigSubviews();
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public void removeViewAt(ScreenStackHeaderConfig parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        parent.removeConfigSubview(i);
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public int getChildCount(ScreenStackHeaderConfig parent) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        return parent.getConfigSubviewsCount();
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager
    public View getChildAt(ScreenStackHeaderConfig parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        return parent.getConfigSubview(i);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(ScreenStackHeaderConfig parent) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        super.onAfterUpdateTransaction((ScreenStackHeaderConfigViewManager) parent);
        parent.onUpdate();
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "title")
    public void setTitle(ScreenStackHeaderConfig config, String str) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setTitle(str);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "titleFontFamily")
    public void setTitleFontFamily(ScreenStackHeaderConfig config, String str) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setTitleFontFamily(str);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "titleFontSize")
    public void setTitleFontSize(ScreenStackHeaderConfig config, int i) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setTitleFontSize(i);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "titleFontWeight")
    public void setTitleFontWeight(ScreenStackHeaderConfig config, String str) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setTitleFontWeight(str);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(customType = "Color", name = "titleColor")
    public void setTitleColor(ScreenStackHeaderConfig config, Integer num) {
        Intrinsics.checkNotNullParameter(config, "config");
        if (num != null) {
            config.setTitleColor(num.intValue());
        }
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(customType = "Color", name = ViewProps.BACKGROUND_COLOR)
    public void setBackgroundColor(ScreenStackHeaderConfig config, Integer num) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setBackgroundColor(num);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "hideShadow")
    public void setHideShadow(ScreenStackHeaderConfig config, boolean z) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setHideShadow(z);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "hideBackButton")
    public void setHideBackButton(ScreenStackHeaderConfig config, boolean z) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setHideBackButton(z);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "topInsetEnabled")
    public void setTopInsetEnabled(ScreenStackHeaderConfig config, boolean z) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setTopInsetEnabled(z);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(customType = "Color", name = ViewProps.COLOR)
    public void setColor(ScreenStackHeaderConfig config, Integer num) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setTintColor(num != null ? num.intValue() : 0);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = ViewProps.HIDDEN)
    public void setHidden(ScreenStackHeaderConfig config, boolean z) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setHidden(z);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "translucent")
    public void setTranslucent(ScreenStackHeaderConfig config, boolean z) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setTranslucent(z);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "backButtonInCustomView")
    public void setBackButtonInCustomView(ScreenStackHeaderConfig config, boolean z) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setBackButtonInCustomView(z);
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    @ReactProp(name = "direction")
    public void setDirection(ScreenStackHeaderConfig config, String str) {
        Intrinsics.checkNotNullParameter(config, "config");
        config.setDirection(str);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(HeaderAttachedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onAttached"), HeaderDetachedEvent.EVENT_NAME, MapBuilder.of("registrationName", "onDetached"));
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public ViewManagerDelegate<ScreenStackHeaderConfig> getDelegate() {
        return this.mDelegate;
    }

    /* compiled from: ScreenStackHeaderConfigViewManager.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/swmansion/rnscreens/ScreenStackHeaderConfigViewManager$Companion;", "", "()V", "REACT_CLASS", "", "react-native-screens_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void logNotAvailable(String str) {
        Log.w("RN SCREENS", str + " prop is not available on Android");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setBackTitle(ScreenStackHeaderConfig screenStackHeaderConfig, String str) {
        logNotAvailable("backTitle");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setBackTitleFontFamily(ScreenStackHeaderConfig screenStackHeaderConfig, String str) {
        logNotAvailable("backTitleFontFamily");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setBackTitleFontSize(ScreenStackHeaderConfig screenStackHeaderConfig, int i) {
        logNotAvailable("backTitleFontSize");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setBackTitleVisible(ScreenStackHeaderConfig screenStackHeaderConfig, boolean z) {
        logNotAvailable("backTitleVisible");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setLargeTitle(ScreenStackHeaderConfig screenStackHeaderConfig, boolean z) {
        logNotAvailable("largeTitle");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setLargeTitleFontFamily(ScreenStackHeaderConfig screenStackHeaderConfig, String str) {
        logNotAvailable("largeTitleFontFamily");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setLargeTitleFontSize(ScreenStackHeaderConfig screenStackHeaderConfig, int i) {
        logNotAvailable("largeTitleFontSize");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setLargeTitleFontWeight(ScreenStackHeaderConfig screenStackHeaderConfig, String str) {
        logNotAvailable("largeTitleFontWeight");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setLargeTitleBackgroundColor(ScreenStackHeaderConfig screenStackHeaderConfig, Integer num) {
        logNotAvailable("largeTitleBackgroundColor");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setLargeTitleHideShadow(ScreenStackHeaderConfig screenStackHeaderConfig, boolean z) {
        logNotAvailable("largeTitleHideShadow");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setLargeTitleColor(ScreenStackHeaderConfig screenStackHeaderConfig, Integer num) {
        logNotAvailable("largeTitleColor");
    }

    @Override // com.facebook.react.viewmanagers.RNSScreenStackHeaderConfigManagerInterface
    public void setDisableBackButtonMenu(ScreenStackHeaderConfig screenStackHeaderConfig, boolean z) {
        logNotAvailable("disableBackButtonMenu");
    }
}
