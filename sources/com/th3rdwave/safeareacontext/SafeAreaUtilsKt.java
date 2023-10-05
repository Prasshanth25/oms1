package com.th3rdwave.safeareacontext;

import android.graphics.Insets;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SafeAreaUtils.kt */
@Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u0018\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005\u001a\u0012\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0002\u001a\u00020\u0005H\u0002\u001a\u0012\u0010\b\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0002\u001a\u00020\u0005H\u0002\u001a\u0012\u0010\t\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0002\u001a\u00020\u0005H\u0003\u001a\u0012\u0010\n\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0002\u001a\u00020\u0005H\u0003\u001a\u0010\u0010\u000b\u001a\u0004\u0018\u00010\u00072\u0006\u0010\u0004\u001a\u00020\u0005¨\u0006\f"}, d2 = {"getFrame", "Lcom/th3rdwave/safeareacontext/Rect;", "rootView", "Landroid/view/ViewGroup;", "view", "Landroid/view/View;", "getRootWindowInsetsCompat", "Lcom/th3rdwave/safeareacontext/EdgeInsets;", "getRootWindowInsetsCompatBase", "getRootWindowInsetsCompatM", "getRootWindowInsetsCompatR", "getSafeAreaInsets", "react-native-safe-area-context_release"}, k = 2, mv = {1, 6, 0}, xi = 48)
/* loaded from: classes.dex */
public final class SafeAreaUtilsKt {
    private static final EdgeInsets getRootWindowInsetsCompatR(View view) {
        Insets insets;
        WindowInsets rootWindowInsets = view.getRootWindowInsets();
        if (rootWindowInsets == null || (insets = rootWindowInsets.getInsets(WindowInsets.Type.statusBars() | WindowInsets.Type.displayCutout() | WindowInsets.Type.navigationBars())) == null) {
            return null;
        }
        return new EdgeInsets(insets.top, insets.right, insets.bottom, insets.left);
    }

    private static final EdgeInsets getRootWindowInsetsCompatM(View view) {
        WindowInsets rootWindowInsets = view.getRootWindowInsets();
        if (rootWindowInsets == null) {
            return null;
        }
        return new EdgeInsets(rootWindowInsets.getSystemWindowInsetTop(), rootWindowInsets.getSystemWindowInsetRight(), Math.min(rootWindowInsets.getSystemWindowInsetBottom(), rootWindowInsets.getStableInsetBottom()), rootWindowInsets.getSystemWindowInsetLeft());
    }

    private static final EdgeInsets getRootWindowInsetsCompatBase(View view) {
        android.graphics.Rect rect = new android.graphics.Rect();
        view.getWindowVisibleDisplayFrame(rect);
        return new EdgeInsets(rect.top, view.getWidth() - rect.right, view.getHeight() - rect.bottom, rect.left);
    }

    private static final EdgeInsets getRootWindowInsetsCompat(View view) {
        return Build.VERSION.SDK_INT >= 30 ? getRootWindowInsetsCompatR(view) : Build.VERSION.SDK_INT >= 23 ? getRootWindowInsetsCompatM(view) : getRootWindowInsetsCompatBase(view);
    }

    public static final EdgeInsets getSafeAreaInsets(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        if (view.getHeight() == 0) {
            return null;
        }
        View rootView = view.getRootView();
        Intrinsics.checkNotNullExpressionValue(rootView, "rootView");
        EdgeInsets rootWindowInsetsCompat = getRootWindowInsetsCompat(rootView);
        if (rootWindowInsetsCompat == null) {
            return null;
        }
        android.graphics.Rect rect = new android.graphics.Rect();
        view.getGlobalVisibleRect(rect);
        return new EdgeInsets(Math.max(rootWindowInsetsCompat.getTop() - rect.top, 0.0f), Math.max(Math.min((rect.left + view.getWidth()) - rootView.getWidth(), 0.0f) + rootWindowInsetsCompat.getRight(), 0.0f), Math.max(Math.min((rect.top + view.getHeight()) - rootView.getHeight(), 0.0f) + rootWindowInsetsCompat.getBottom(), 0.0f), Math.max(rootWindowInsetsCompat.getLeft() - rect.left, 0.0f));
    }

    public static final Rect getFrame(ViewGroup rootView, View view) {
        Intrinsics.checkNotNullParameter(rootView, "rootView");
        Intrinsics.checkNotNullParameter(view, "view");
        if (view.getParent() == null) {
            return null;
        }
        android.graphics.Rect rect = new android.graphics.Rect();
        view.getDrawingRect(rect);
        try {
            rootView.offsetDescendantRectToMyCoords(view, rect);
            return new Rect(rect.left, rect.top, view.getWidth(), view.getHeight());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
