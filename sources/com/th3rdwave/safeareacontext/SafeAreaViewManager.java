package com.th3rdwave.safeareacontext;

import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.StateWrapper;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManagerDelegate;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.viewmanagers.RNCSafeAreaViewManagerInterface;
import com.facebook.react.views.view.ReactViewGroup;
import com.facebook.react.views.view.ReactViewManager;
import com.henninghall.date_picker.props.ModeProp;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SafeAreaViewManager.kt */
@Metadata(d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u0000 \u001e2\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002:\u0001\u001eB\u0005¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016J\u0010\u0010\n\u001a\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000bH\u0014J\b\u0010\r\u001a\u00020\u000eH\u0016J\u000e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00060\u0010H\u0016J\u001a\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00032\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0017J\u001a\u0010\u0016\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00032\b\u0010\u0017\u001a\u0004\u0018\u00010\u000eH\u0017J&\u0010\u0018\u001a\u0004\u0018\u00010\u00192\u0006\u0010\u0013\u001a\u00020\f2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0016¨\u0006\u001f"}, d2 = {"Lcom/th3rdwave/safeareacontext/SafeAreaViewManager;", "Lcom/facebook/react/views/view/ReactViewManager;", "Lcom/facebook/react/viewmanagers/RNCSafeAreaViewManagerInterface;", "Lcom/th3rdwave/safeareacontext/SafeAreaView;", "()V", "createShadowNodeInstance", "Lcom/th3rdwave/safeareacontext/SafeAreaViewShadowNode;", "createViewInstance", "context", "Lcom/facebook/react/uimanager/ThemedReactContext;", "getDelegate", "Lcom/facebook/react/uimanager/ViewManagerDelegate;", "Lcom/facebook/react/views/view/ReactViewGroup;", "getName", "", "getShadowNodeClass", "Ljava/lang/Class;", "setEdges", "", "view", "propList", "Lcom/facebook/react/bridge/ReadableMap;", "setMode", ModeProp.name, "updateState", "", "props", "Lcom/facebook/react/uimanager/ReactStylesDiffMap;", "stateWrapper", "Lcom/facebook/react/uimanager/StateWrapper;", "Companion", "react-native-safe-area-context_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
@ReactModule(name = SafeAreaViewManager.REACT_CLASS)
/* loaded from: classes.dex */
public final class SafeAreaViewManager extends ReactViewManager implements RNCSafeAreaViewManagerInterface<SafeAreaView> {
    public static final Companion Companion = new Companion(null);
    public static final String REACT_CLASS = "RNCSafeAreaView";

    @Override // com.facebook.react.uimanager.ViewManager
    public ViewManagerDelegate<ReactViewGroup> getDelegate() {
        return null;
    }

    @Override // com.facebook.react.views.view.ReactViewManager, com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.ViewManager
    public Class<SafeAreaViewShadowNode> getShadowNodeClass() {
        return SafeAreaViewShadowNode.class;
    }

    @Override // com.facebook.react.views.view.ReactViewManager, com.facebook.react.uimanager.ViewManager
    public SafeAreaView createViewInstance(ThemedReactContext context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return new SafeAreaView(context);
    }

    @Override // com.facebook.react.uimanager.ViewGroupManager, com.facebook.react.uimanager.ViewManager
    public SafeAreaViewShadowNode createShadowNodeInstance() {
        return new SafeAreaViewShadowNode();
    }

    @Override // com.facebook.react.viewmanagers.RNCSafeAreaViewManagerInterface
    @ReactProp(name = ModeProp.name)
    public void setMode(SafeAreaView view, String str) {
        Intrinsics.checkNotNullParameter(view, "view");
        if (Intrinsics.areEqual(str, ViewProps.PADDING)) {
            view.setMode(SafeAreaViewMode.PADDING);
        } else if (Intrinsics.areEqual(str, ViewProps.MARGIN)) {
            view.setMode(SafeAreaViewMode.MARGIN);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:61:0x001e, code lost:
        if (r0 == null) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0037, code lost:
        if (r2 == null) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0050, code lost:
        if (r3 == null) goto L23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0069, code lost:
        if (r7 == null) goto L22;
     */
    @Override // com.facebook.react.viewmanagers.RNCSafeAreaViewManagerInterface
    @com.facebook.react.uimanager.annotations.ReactProp(name = "edges")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void setEdges(com.th3rdwave.safeareacontext.SafeAreaView r6, com.facebook.react.bridge.ReadableMap r7) {
        /*
            r5 = this;
            java.lang.String r0 = "view"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            if (r7 == 0) goto L75
            java.lang.String r0 = "top"
            java.lang.String r0 = r7.getString(r0)
            java.lang.String r1 = "this as java.lang.String).toUpperCase(Locale.ROOT)"
            if (r0 == 0) goto L20
            java.util.Locale r2 = java.util.Locale.ROOT
            java.lang.String r0 = r0.toUpperCase(r2)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r1)
            com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes r0 = com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes.valueOf(r0)
            if (r0 != 0) goto L22
        L20:
            com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes r0 = com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes.OFF
        L22:
            java.lang.String r2 = "right"
            java.lang.String r2 = r7.getString(r2)
            if (r2 == 0) goto L39
            java.util.Locale r3 = java.util.Locale.ROOT
            java.lang.String r2 = r2.toUpperCase(r3)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r1)
            com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes r2 = com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes.valueOf(r2)
            if (r2 != 0) goto L3b
        L39:
            com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes r2 = com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes.OFF
        L3b:
            java.lang.String r3 = "bottom"
            java.lang.String r3 = r7.getString(r3)
            if (r3 == 0) goto L52
            java.util.Locale r4 = java.util.Locale.ROOT
            java.lang.String r3 = r3.toUpperCase(r4)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, r1)
            com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes r3 = com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes.valueOf(r3)
            if (r3 != 0) goto L54
        L52:
            com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes r3 = com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes.OFF
        L54:
            java.lang.String r4 = "left"
            java.lang.String r7 = r7.getString(r4)
            if (r7 == 0) goto L6b
            java.util.Locale r4 = java.util.Locale.ROOT
            java.lang.String r7 = r7.toUpperCase(r4)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r7, r1)
            com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes r7 = com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes.valueOf(r7)
            if (r7 != 0) goto L6d
        L6b:
            com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes r7 = com.th3rdwave.safeareacontext.SafeAreaViewEdgeModes.OFF
        L6d:
            com.th3rdwave.safeareacontext.SafeAreaViewEdges r1 = new com.th3rdwave.safeareacontext.SafeAreaViewEdges
            r1.<init>(r0, r2, r3, r7)
            r6.setEdges(r1)
        L75:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.th3rdwave.safeareacontext.SafeAreaViewManager.setEdges(com.th3rdwave.safeareacontext.SafeAreaView, com.facebook.react.bridge.ReadableMap):void");
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Object updateState(ReactViewGroup view, ReactStylesDiffMap reactStylesDiffMap, StateWrapper stateWrapper) {
        Intrinsics.checkNotNullParameter(view, "view");
        ((SafeAreaView) view).getFabricViewStateManager().setStateWrapper(stateWrapper);
        return null;
    }

    /* compiled from: SafeAreaViewManager.kt */
    @Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lcom/th3rdwave/safeareacontext/SafeAreaViewManager$Companion;", "", "()V", "REACT_CLASS", "", "react-native-safe-area-context_release"}, k = 1, mv = {1, 6, 0}, xi = 48)
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
