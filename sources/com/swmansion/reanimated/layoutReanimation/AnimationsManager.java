package com.swmansion.reanimated.layoutReanimation;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.facebook.react.bridge.JavaOnlyMap;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.IViewManagerWithChildren;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ReactStylesDiffMap;
import com.facebook.react.uimanager.RootView;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewManager;
import com.swmansion.reanimated.Scheduler;
import com.swmansion.rnscreens.ScreenStackViewManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class AnimationsManager implements ViewHierarchyObserver {
    private ReactContext mContext;
    private NativeMethodsHolder mNativeMethodsHolder;
    private ReanimatedNativeHierarchyManager mReanimatedNativeHierarchyManager;
    private WeakReference<Scheduler> mScheduler;
    private UIManagerModule mUIManager;
    private HashSet<Integer> mEnteringViews = new HashSet<>();
    private HashMap<Integer, Rect> mEnteringViewTargetValues = new HashMap<>();
    private HashMap<Integer, View> mExitingViews = new HashMap<>();
    private HashMap<Integer, Integer> mExitingSubviewCountMap = new HashMap<>();
    private HashSet<Integer> mAncestorsToRemove = new HashSet<>();
    private HashMap<Integer, Runnable> mCallbacks = new HashMap<>();
    private boolean isCatalystInstanceDestroyed = false;
    private SharedTransitionManager mSharedTransitionManager = new SharedTransitionManager(this);

    public void setReanimatedNativeHierarchyManager(ReanimatedNativeHierarchyManager reanimatedNativeHierarchyManager) {
        this.mReanimatedNativeHierarchyManager = reanimatedNativeHierarchyManager;
    }

    public ReanimatedNativeHierarchyManager getReanimatedNativeHierarchyManager() {
        return this.mReanimatedNativeHierarchyManager;
    }

    public void setScheduler(Scheduler scheduler) {
        this.mScheduler = new WeakReference<>(scheduler);
    }

    public AnimationsManager(ReactContext reactContext, UIManagerModule uIManagerModule) {
        this.mContext = reactContext;
        this.mUIManager = uIManagerModule;
    }

    public void onCatalystInstanceDestroy() {
        this.isCatalystInstanceDestroyed = true;
        this.mNativeMethodsHolder = null;
        this.mContext = null;
        this.mUIManager = null;
        this.mExitingViews = null;
        this.mExitingSubviewCountMap = null;
        this.mAncestorsToRemove = null;
        this.mCallbacks = null;
    }

    @Override // com.swmansion.reanimated.layoutReanimation.ViewHierarchyObserver
    public void onViewRemoval(View view, ViewGroup viewGroup, Runnable runnable) {
        if (this.isCatalystInstanceDestroyed) {
            return;
        }
        this.mCallbacks.put(Integer.valueOf(view.getId()), runnable);
        if (removeOrAnimateExitRecursive(view, true)) {
            return;
        }
        removeView(view, viewGroup);
    }

    @Override // com.swmansion.reanimated.layoutReanimation.ViewHierarchyObserver
    public void onViewCreate(View view, ViewGroup viewGroup, Snapshot snapshot) {
        if (this.isCatalystInstanceDestroyed) {
            return;
        }
        maybeRegisterSharedView(view);
        if (hasAnimationForTag(view.getId(), 1)) {
            Scheduler scheduler = this.mScheduler.get();
            if (scheduler != null) {
                scheduler.triggerUI();
            }
            int id = view.getId();
            HashMap<String, Object> targetMap = snapshot.toTargetMap();
            if (targetMap != null) {
                this.mNativeMethodsHolder.startAnimation(id, 1, prepareDataForAnimationWorklet(targetMap, true));
                this.mEnteringViews.add(Integer.valueOf(id));
            }
        }
    }

    @Override // com.swmansion.reanimated.layoutReanimation.ViewHierarchyObserver
    public void onViewUpdate(View view, Snapshot snapshot, Snapshot snapshot2) {
        if (this.isCatalystInstanceDestroyed) {
            return;
        }
        int id = view.getId();
        if (!hasAnimationForTag(id, 3)) {
            if (this.mEnteringViews.contains(Integer.valueOf(id))) {
                this.mEnteringViewTargetValues.put(Integer.valueOf(id), new Rect(snapshot2.originX, snapshot2.originY, snapshot2.originX + snapshot2.width, snapshot2.originY + snapshot2.height));
                view.layout(snapshot.originX, snapshot.originY, snapshot.originX + snapshot.width, snapshot.originY + snapshot.height);
                return;
            }
            return;
        }
        HashMap<String, Object> currentMap = snapshot.toCurrentMap();
        HashMap<String, Object> targetMap = snapshot2.toTargetMap();
        boolean z = true;
        for (int i = 0; i < Snapshot.targetKeysToTransform.size(); i++) {
            if (((Number) currentMap.get(Snapshot.currentKeysToTransform.get(i))).doubleValue() != ((Number) targetMap.get(Snapshot.targetKeysToTransform.get(i))).doubleValue()) {
                z = false;
            }
        }
        if (z) {
            return;
        }
        HashMap<String, Object> prepareDataForAnimationWorklet = prepareDataForAnimationWorklet(currentMap, false);
        HashMap<String, Object> hashMap = new HashMap<>(prepareDataForAnimationWorklet(targetMap, true));
        for (String str : prepareDataForAnimationWorklet.keySet()) {
            hashMap.put(str, prepareDataForAnimationWorklet.get(str));
        }
        this.mNativeMethodsHolder.startAnimation(id, 3, hashMap);
    }

    public void maybeRegisterSharedView(View view) {
        if (hasAnimationForTag(view.getId(), 4)) {
            this.mSharedTransitionManager.notifyAboutNewView(view);
        }
    }

    public void progressLayoutAnimation(int i, Map<String, Object> map, boolean z) {
        ViewGroup viewGroup;
        View resolveView = resolveView(i);
        if (resolveView == null || (viewGroup = (ViewGroup) resolveView.getParent()) == null) {
            return;
        }
        ViewManager resolveViewManager = resolveViewManager(i);
        ViewManager resolveViewManager2 = resolveViewManager(viewGroup.getId());
        if (resolveViewManager == null) {
            return;
        }
        setNewProps(map, resolveView, resolveViewManager, resolveViewManager2, Integer.valueOf(viewGroup.getId()), z);
    }

    public void endLayoutAnimation(int i, boolean z, boolean z2) {
        View resolveView = resolveView(i);
        if (resolveView == null) {
            return;
        }
        Rect rect = this.mEnteringViewTargetValues.get(Integer.valueOf(i));
        if (!z2 && this.mEnteringViews.contains(Integer.valueOf(i)) && rect != null) {
            resolveView.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
        this.mEnteringViews.remove(Integer.valueOf(i));
        this.mEnteringViewTargetValues.remove(Integer.valueOf(i));
        if (z2) {
            if ((resolveView instanceof ViewGroup) && this.mAncestorsToRemove.contains(Integer.valueOf(i))) {
                cancelAnimationsInSubviews((ViewGroup) resolveView);
            }
            this.mExitingViews.remove(Integer.valueOf(i));
            maybeDropAncestors(resolveView);
            removeView(resolveView, (ViewGroup) resolveView.getParent());
        }
        this.mSharedTransitionManager.finishSharedAnimation(i);
    }

    public void printSubTree(View view, int i) {
        if (i == 0) {
            Log.v("rea", "----------------------");
        }
        if (view == null) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            sb.append("+");
        }
        sb.append(" TAG:");
        sb.append(view.getId());
        sb.append(" CLASS:");
        sb.append(view.getClass().getSimpleName());
        Log.v("rea", sb.toString());
        if (!(view instanceof ViewGroup)) {
            return;
        }
        while (true) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (i2 >= viewGroup.getChildCount()) {
                return;
            }
            printSubTree(viewGroup.getChildAt(i2), i + 1);
            i2++;
        }
    }

    public HashMap<String, Object> prepareDataForAnimationWorklet(HashMap<String, Object> hashMap, boolean z) {
        return prepareDataForAnimationWorklet(hashMap, z, false);
    }

    public HashMap<String, Object> prepareDataForAnimationWorklet(HashMap<String, Object> hashMap, boolean z, boolean z2) {
        ArrayList<String> arrayList;
        HashMap<String, Object> hashMap2 = new HashMap<>();
        if (z) {
            arrayList = Snapshot.targetKeysToTransform;
        } else {
            arrayList = Snapshot.currentKeysToTransform;
        }
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            hashMap2.put(next, Float.valueOf(PixelUtil.toDIPFromPixel(((Integer) hashMap.get(next)).intValue())));
        }
        if (z2) {
            String str = z ? Snapshot.TARGET_TRANSFORM_MATRIX : Snapshot.CURRENT_TRANSFORM_MATRIX;
            hashMap2.put(str, hashMap.get(str));
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Activity currentActivity = this.mContext.getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int i = displayMetrics.heightPixels;
            hashMap2.put("windowWidth", Float.valueOf(PixelUtil.toDIPFromPixel(displayMetrics.widthPixels)));
            hashMap2.put("windowHeight", Float.valueOf(PixelUtil.toDIPFromPixel(i)));
        } else {
            hashMap2.put("windowWidth", Float.valueOf(PixelUtil.toDIPFromPixel(0.0f)));
            hashMap2.put("windowHeight", Float.valueOf(PixelUtil.toDIPFromPixel(0.0f)));
        }
        return hashMap2;
    }

    public void setNativeMethods(NativeMethodsHolder nativeMethodsHolder) {
        this.mNativeMethodsHolder = nativeMethodsHolder;
        this.mSharedTransitionManager.setNativeMethods(nativeMethodsHolder);
    }

    public void setNewProps(Map<String, Object> map, View view, ViewManager viewManager, ViewManager viewManager2, Integer num, boolean z) {
        float dIPFromPixel;
        float dIPFromPixel2;
        float dIPFromPixel3;
        float dIPFromPixel4;
        if (map.get(Snapshot.ORIGIN_X) != null) {
            dIPFromPixel = ((Double) map.get(Snapshot.ORIGIN_X)).floatValue();
        } else {
            dIPFromPixel = PixelUtil.toDIPFromPixel(view.getLeft());
        }
        float f = dIPFromPixel;
        if (map.get(Snapshot.ORIGIN_Y) != null) {
            dIPFromPixel2 = ((Double) map.get(Snapshot.ORIGIN_Y)).floatValue();
        } else {
            dIPFromPixel2 = PixelUtil.toDIPFromPixel(view.getTop());
        }
        float f2 = dIPFromPixel2;
        if (map.get("width") != null) {
            dIPFromPixel3 = ((Double) map.get("width")).floatValue();
        } else {
            dIPFromPixel3 = PixelUtil.toDIPFromPixel(view.getWidth());
        }
        float f3 = dIPFromPixel3;
        if (map.get("height") != null) {
            dIPFromPixel4 = ((Double) map.get("height")).floatValue();
        } else {
            dIPFromPixel4 = PixelUtil.toDIPFromPixel(view.getHeight());
        }
        float f4 = dIPFromPixel4;
        if (map.containsKey(Snapshot.TRANSFORM_MATRIX)) {
            float[] fArr = new float[9];
            if (map.get(Snapshot.TRANSFORM_MATRIX) instanceof ReadableNativeArray) {
                ReadableNativeArray readableNativeArray = (ReadableNativeArray) map.get(Snapshot.TRANSFORM_MATRIX);
                for (int i = 0; i < 9; i++) {
                    fArr[i] = Double.valueOf(readableNativeArray.getDouble(i)).floatValue();
                }
            } else {
                ArrayList arrayList = (ArrayList) map.get(Snapshot.TRANSFORM_MATRIX);
                for (int i2 = 0; i2 < 9; i2++) {
                    fArr[i2] = ((Float) arrayList.get(i2)).floatValue();
                }
            }
            view.setScaleX(fArr[0]);
            view.setScaleY(fArr[4]);
            map.remove(Snapshot.TRANSFORM_MATRIX);
        }
        updateLayout(view, viewManager2, num.intValue(), view.getId(), f, f2, f3, f4, z);
        map.remove(Snapshot.ORIGIN_X);
        map.remove(Snapshot.ORIGIN_Y);
        map.remove(Snapshot.GLOBAL_ORIGIN_X);
        map.remove(Snapshot.GLOBAL_ORIGIN_Y);
        map.remove("width");
        map.remove("height");
        if (map.size() == 0) {
            return;
        }
        JavaOnlyMap javaOnlyMap = new JavaOnlyMap();
        for (String str : map.keySet()) {
            addProp(javaOnlyMap, str, map.get(str));
        }
        viewManager.updateProperties(view, new ReactStylesDiffMap(javaOnlyMap));
    }

    private static void addProp(WritableMap writableMap, String str, Object obj) {
        if (obj == null) {
            writableMap.putNull(str);
        } else if (obj instanceof Double) {
            writableMap.putDouble(str, ((Double) obj).doubleValue());
        } else if (obj instanceof Integer) {
            writableMap.putInt(str, ((Integer) obj).intValue());
        } else if (obj instanceof Number) {
            writableMap.putDouble(str, ((Number) obj).doubleValue());
        } else if (obj instanceof Boolean) {
            writableMap.putBoolean(str, ((Boolean) obj).booleanValue());
        } else if (obj instanceof String) {
            writableMap.putString(str, (String) obj);
        } else if (obj instanceof ReadableArray) {
            writableMap.putArray(str, (ReadableArray) obj);
        } else if (obj instanceof ReadableMap) {
            writableMap.putMap(str, (ReadableMap) obj);
        } else {
            throw new IllegalStateException("Unknown type of animated value [Layout Animations]");
        }
    }

    public void updateLayout(View view, ViewManager viewManager, int i, int i2, float f, float f2, float f3, float f4, boolean z) {
        int round = Math.round(PixelUtil.toPixelFromDIP(f));
        int round2 = Math.round(PixelUtil.toPixelFromDIP(f2));
        int round3 = Math.round(PixelUtil.toPixelFromDIP(f3));
        int round4 = Math.round(PixelUtil.toPixelFromDIP(f4));
        view.measure(View.MeasureSpec.makeMeasureSpec(round3, 1073741824), View.MeasureSpec.makeMeasureSpec(round4, 1073741824));
        ViewParent parent = view.getParent();
        if (parent instanceof RootView) {
            parent.requestLayout();
        }
        if (i % 10 == 1 && viewManager != null) {
            if (!(viewManager instanceof IViewManagerWithChildren)) {
                throw new IllegalViewOperationException("Trying to use view with tag " + i + " as a parent, but its Manager doesn't implement IViewManagerWithChildren");
            } else if (((IViewManagerWithChildren) viewManager).needsCustomLayoutForChildren()) {
                return;
            } else {
                view.layout(round, round2, round3 + round, round4 + round2);
                return;
            }
        }
        if (z) {
            Point convertScreenLocationToViewCoordinates = convertScreenLocationToViewCoordinates(new Point(round, round2), (View) view.getParent());
            round = convertScreenLocationToViewCoordinates.x;
            round2 = convertScreenLocationToViewCoordinates.y;
        }
        view.layout(round, round2, round3 + round, round4 + round2);
    }

    public boolean hasAnimationForTag(int i, int i2) {
        return this.mNativeMethodsHolder.hasAnimation(i, i2);
    }

    public boolean isLayoutAnimationEnabled() {
        NativeMethodsHolder nativeMethodsHolder = this.mNativeMethodsHolder;
        return nativeMethodsHolder != null && nativeMethodsHolder.isLayoutAnimationEnabled();
    }

    private boolean removeOrAnimateExitRecursive(View view, boolean z) {
        boolean z2;
        int id = view.getId();
        ViewManager resolveViewManager = resolveViewManager(id);
        if (resolveViewManager != null && resolveViewManager.getName().equals(ScreenStackViewManager.REACT_CLASS)) {
            cancelAnimationsRecursive(view);
            return false;
        }
        boolean z3 = hasAnimationForTag(id, 2) || this.mExitingViews.containsKey(Integer.valueOf(id));
        boolean z4 = z && !z3;
        if (hasAnimationForTag(id, 4)) {
            this.mSharedTransitionManager.notifyAboutRemovedView(view);
            this.mSharedTransitionManager.makeSnapshot(view);
        }
        ArrayList arrayList = new ArrayList();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            z2 = false;
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                if (removeOrAnimateExitRecursive(childAt, z4)) {
                    z2 = true;
                } else if (z4) {
                    arrayList.add(childAt);
                }
            }
        } else {
            z2 = false;
        }
        boolean z5 = z3 || z2;
        if (z3) {
            HashMap<String, Object> prepareDataForAnimationWorklet = prepareDataForAnimationWorklet(new Snapshot(view, this.mReanimatedNativeHierarchyManager).toCurrentMap(), false);
            if (!this.mExitingViews.containsKey(Integer.valueOf(id))) {
                this.mExitingViews.put(Integer.valueOf(id), view);
                registerExitingAncestors(view);
                this.mNativeMethodsHolder.startAnimation(id, 2, prepareDataForAnimationWorklet);
            }
        }
        this.mNativeMethodsHolder.clearAnimationConfig(id);
        if (z5) {
            if (z2) {
                this.mAncestorsToRemove.add(Integer.valueOf(id));
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                removeView((View) it.next(), (ViewGroup) view);
            }
            return true;
        }
        return false;
    }

    public void clearAnimationConfigForTag(int i) {
        View resolveView = resolveView(i);
        if (resolveView != null) {
            clearAnimationConfigRecursive(resolveView);
        }
    }

    public void clearAnimationConfigRecursive(View view) {
        this.mNativeMethodsHolder.clearAnimationConfig(view.getId());
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                clearAnimationConfigRecursive(viewGroup.getChildAt(i));
            }
        }
    }

    public void cancelAnimationsInSubviews(View view) {
        cancelAnimationsRecursive(view);
        clearAnimationConfigRecursive(view);
    }

    private void registerExitingAncestors(View view) {
        for (View view2 = (View) view.getParent(); view2 != null && !(view2 instanceof RootView); view2 = (View) view2.getParent()) {
            int id = view2.getId();
            Integer num = this.mExitingSubviewCountMap.get(Integer.valueOf(id));
            int i = 1;
            if (num != null) {
                i = 1 + num.intValue();
            }
            this.mExitingSubviewCountMap.put(Integer.valueOf(id), Integer.valueOf(i));
        }
    }

    private void maybeDropAncestors(View view) {
        if (view.getParent() instanceof View) {
            View view2 = (View) view.getParent();
            while (view2 != null && !(view2 instanceof RootView)) {
                View view3 = (View) view2.getParent();
                int id = view2.getId();
                Integer num = this.mExitingSubviewCountMap.get(Integer.valueOf(id));
                Integer valueOf = Integer.valueOf(num != null ? num.intValue() - 1 : 0);
                if (valueOf.intValue() <= 0) {
                    if (this.mAncestorsToRemove.contains(Integer.valueOf(id))) {
                        this.mAncestorsToRemove.remove(Integer.valueOf(id));
                        if (!this.mExitingViews.containsKey(Integer.valueOf(id))) {
                            removeView(view2, (ViewGroup) view3);
                        }
                    }
                    this.mExitingSubviewCountMap.remove(Integer.valueOf(id));
                } else {
                    this.mExitingSubviewCountMap.put(Integer.valueOf(id), valueOf);
                }
                view2 = view3;
            }
        }
    }

    private void removeView(View view, @Nullable ViewGroup viewGroup) {
        int id = view.getId();
        if (this.mCallbacks.containsKey(Integer.valueOf(id))) {
            Runnable runnable = this.mCallbacks.get(Integer.valueOf(id));
            this.mCallbacks.remove(Integer.valueOf(id));
            if (runnable != null) {
                runnable.run();
            }
        } else {
            this.mReanimatedNativeHierarchyManager.publicDropView(view);
        }
        if (viewGroup != null) {
            viewGroup.removeView(view);
        }
    }

    public void cancelAnimationsRecursive(View view) {
        if (this.mExitingViews.containsKey(Integer.valueOf(view.getId()))) {
            endLayoutAnimation(view.getId(), true, true);
        } else if ((view instanceof ViewGroup) && this.mExitingSubviewCountMap.containsKey(Integer.valueOf(view.getId()))) {
            cancelAnimationsInSubviews((ViewGroup) view);
        }
    }

    private void cancelAnimationsInSubviews(ViewGroup viewGroup) {
        for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = viewGroup.getChildAt(childCount);
            if (childAt != null) {
                if (this.mExitingViews.containsKey(Integer.valueOf(childAt.getId()))) {
                    endLayoutAnimation(childAt.getId(), true, true);
                } else if ((childAt instanceof ViewGroup) && this.mExitingSubviewCountMap.containsKey(Integer.valueOf(childAt.getId()))) {
                    cancelAnimationsInSubviews((ViewGroup) childAt);
                }
            }
        }
    }

    private View resolveView(int i) {
        if (this.mExitingViews.containsKey(Integer.valueOf(i))) {
            return this.mExitingViews.get(Integer.valueOf(i));
        }
        View transitioningView = this.mSharedTransitionManager.getTransitioningView(i);
        if (transitioningView != null) {
            return transitioningView;
        }
        try {
            return this.mUIManager.resolveView(i);
        } catch (IllegalViewOperationException unused) {
            return null;
        }
    }

    private ViewManager resolveViewManager(int i) {
        try {
            return this.mReanimatedNativeHierarchyManager.resolveViewManager(i);
        } catch (Exception unused) {
            return null;
        }
    }

    private static Point convertScreenLocationToViewCoordinates(Point point, View view) {
        int[] iArr = {0, 0};
        if (view != null) {
            view.getLocationOnScreen(iArr);
        }
        return new Point(point.x - iArr[0], point.y - iArr[1]);
    }

    public void screenDidLayout() {
        this.mSharedTransitionManager.screenDidLayout();
    }

    public void viewDidLayout(View view) {
        this.mSharedTransitionManager.viewDidLayout(view);
    }

    public void notifyAboutViewsRemoval(int[] iArr) {
        this.mSharedTransitionManager.onViewsRemoval(iArr);
    }

    public void makeSnapshotOfTopScreenViews(ViewGroup viewGroup) {
        this.mSharedTransitionManager.doSnapshotForTopScreenViews(viewGroup);
    }

    public ReactContext getContext() {
        return this.mContext;
    }
}
