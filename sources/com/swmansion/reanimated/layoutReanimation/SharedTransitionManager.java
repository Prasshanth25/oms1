package com.swmansion.reanimated.layoutReanimation;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.uimanager.IllegalViewOperationException;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.views.view.ReactViewGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

/* loaded from: classes.dex */
public class SharedTransitionManager {
    private final AnimationsManager mAnimationsManager;
    private boolean mIsSharedTransitionActive;
    private NativeMethodsHolder mNativeMethodsHolder;
    private View mTransitionContainer;
    private final List<View> mAddedSharedViews = new ArrayList();
    private final Map<Integer, View> mSharedTransitionParent = new HashMap();
    private final Map<Integer, Integer> mSharedTransitionInParentIndex = new HashMap();
    private final Map<Integer, Snapshot> mSnapshotRegistry = new HashMap();
    private final Map<Integer, View> mCurrentSharedTransitionViews = new HashMap();
    private final List<View> mRemovedSharedViews = new ArrayList();
    private final Set<Integer> mViewTagsToHide = new HashSet();
    private final Map<Integer, Integer> mDisableCleaningForViewTag = new HashMap();
    private List<SharedElement> mSharedElements = new ArrayList();
    private final Map<Integer, View> mViewsWithCanceledAnimation = new HashMap();

    /* loaded from: classes.dex */
    public interface TreeVisitor {
        void run(View view);
    }

    public SharedTransitionManager(AnimationsManager animationsManager) {
        this.mAnimationsManager = animationsManager;
    }

    public void notifyAboutNewView(View view) {
        this.mAddedSharedViews.add(view);
    }

    public void notifyAboutRemovedView(View view) {
        this.mRemovedSharedViews.add(view);
    }

    @Nullable
    public View getTransitioningView(int i) {
        return this.mCurrentSharedTransitionViews.get(Integer.valueOf(i));
    }

    public void screenDidLayout() {
        tryStartSharedTransitionForViews(this.mAddedSharedViews, true);
        this.mAddedSharedViews.clear();
    }

    public void viewDidLayout(View view) {
        maybeRestartAnimationWithNewLayout(view);
    }

    public void onViewsRemoval(int[] iArr) {
        if (iArr == null) {
            return;
        }
        restoreVisibility();
        visitTreeForTags(iArr, new SnapshotTreeVisitor());
        if (this.mRemovedSharedViews.size() > 0) {
            if (!tryStartSharedTransitionForViews(this.mRemovedSharedViews, false)) {
                this.mRemovedSharedViews.clear();
                return;
            }
            ConfigCleanerTreeVisitor configCleanerTreeVisitor = new ConfigCleanerTreeVisitor();
            for (View view : this.mRemovedSharedViews) {
                visitTree(view, configCleanerTreeVisitor);
            }
            this.mRemovedSharedViews.clear();
            visitTreeForTags(iArr, configCleanerTreeVisitor);
        } else if (this.mCurrentSharedTransitionViews.size() > 0) {
            ArrayList<View> arrayList = new ArrayList();
            for (View view2 : this.mCurrentSharedTransitionViews.values()) {
                for (int i : iArr) {
                    if (isViewChildParentWithTag(view2, i)) {
                        arrayList.add(view2);
                    }
                }
            }
            tryStartSharedTransitionForViews(arrayList, false);
            for (View view3 : arrayList) {
                clearAllSharedConfigsForView(view3);
            }
        }
    }

    private void restoreVisibility() {
        ReanimatedNativeHierarchyManager reanimatedNativeHierarchyManager = this.mAnimationsManager.getReanimatedNativeHierarchyManager();
        for (Integer num : this.mViewTagsToHide) {
            View resolveView = reanimatedNativeHierarchyManager.resolveView(num.intValue());
            if (resolveView != null) {
                resolveView.setVisibility(0);
            }
        }
        this.mViewTagsToHide.clear();
    }

    private boolean isViewChildParentWithTag(View view, int i) {
        Object obj = this.mSharedTransitionParent.get(Integer.valueOf(view.getId()));
        while (true) {
            View view2 = (View) obj;
            if (view2 != null) {
                if (view2.getId() != i) {
                    if (view2.getClass().getSimpleName().equals("Screen") || !(view2 instanceof View)) {
                        break;
                    }
                    obj = view2.getParent();
                } else {
                    return true;
                }
            } else {
                break;
            }
        }
        return false;
    }

    public void doSnapshotForTopScreenViews(ViewGroup viewGroup) {
        if (viewGroup.getChildCount() > 0) {
            View childAt = viewGroup.getChildAt(0);
            if (childAt instanceof ViewGroup) {
                visitNativeTreeAndMakeSnapshot(((ViewGroup) childAt).getChildAt(0));
            } else {
                Log.e("[Reanimated]", "Unable to recognize screen on stack.");
            }
        }
    }

    public void setNativeMethods(NativeMethodsHolder nativeMethodsHolder) {
        this.mNativeMethodsHolder = nativeMethodsHolder;
    }

    private void maybeRestartAnimationWithNewLayout(View view) {
        View view2 = this.mCurrentSharedTransitionViews.get(Integer.valueOf(view.getId()));
        if (view2 == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (SharedElement sharedElement : this.mSharedElements) {
            if (sharedElement.targetView == view2) {
                arrayList.add(sharedElement);
                View view3 = sharedElement.sourceView;
                View view4 = sharedElement.targetView;
                Snapshot snapshot = new Snapshot(view3);
                Snapshot snapshot2 = this.mSnapshotRegistry.get(Integer.valueOf(view4.getId()));
                Snapshot snapshot3 = new Snapshot(view4);
                int i = (snapshot2.originX - snapshot2.originXByParent) + snapshot3.originX;
                int i2 = (snapshot2.originY - snapshot2.originYByParent) + snapshot3.originY;
                snapshot2.originX = i;
                snapshot2.originY = i2;
                snapshot2.globalOriginX = i;
                snapshot2.globalOriginY = i2;
                snapshot2.originXByParent = snapshot3.originXByParent;
                snapshot2.originYByParent = snapshot3.originYByParent;
                snapshot2.height = snapshot3.height;
                snapshot2.width = snapshot3.width;
                sharedElement.sourceViewSnapshot = snapshot;
                sharedElement.targetViewSnapshot = snapshot2;
                disableCleaningForViewTag(view3.getId());
                disableCleaningForViewTag(view4.getId());
            }
        }
        startSharedTransition(arrayList);
    }

    private boolean tryStartSharedTransitionForViews(List<View> list, boolean z) {
        if (list.isEmpty()) {
            return false;
        }
        sortViewsByTags(list);
        List<SharedElement> sharedElementsForCurrentTransition = getSharedElementsForCurrentTransition(list, z);
        if (sharedElementsForCurrentTransition.isEmpty()) {
            return false;
        }
        setupTransitionContainer();
        reparentSharedViewsForCurrentTransition(sharedElementsForCurrentTransition);
        startSharedTransition(sharedElementsForCurrentTransition);
        return true;
    }

    private void sortViewsByTags(List<View> list) {
        Collections.sort(list, new Comparator() { // from class: com.swmansion.reanimated.layoutReanimation.SharedTransitionManager$$ExternalSyntheticLambda0
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                int compare;
                compare = Integer.compare(((View) obj2).getId(), ((View) obj).getId());
                return compare;
            }
        });
    }

    private List<SharedElement> getSharedElementsForCurrentTransition(List<View> list, boolean z) {
        View resolveView;
        ViewGroup viewGroup;
        ViewGroupManager viewGroupManager;
        int childCount;
        ArrayList<View> arrayList = new ArrayList();
        HashSet hashSet = new HashSet();
        if (!z) {
            for (View view : list) {
                hashSet.add(Integer.valueOf(view.getId()));
            }
        }
        ArrayList arrayList2 = new ArrayList();
        ReanimatedNativeHierarchyManager reanimatedNativeHierarchyManager = this.mAnimationsManager.getReanimatedNativeHierarchyManager();
        for (View view2 : list) {
            int findPrecedingViewTagForTransition = this.mNativeMethodsHolder.findPrecedingViewTagForTransition(view2.getId());
            boolean z2 = !z && hashSet.contains(Integer.valueOf(findPrecedingViewTagForTransition));
            if (findPrecedingViewTagForTransition >= 0) {
                if (z) {
                    View resolveView2 = reanimatedNativeHierarchyManager.resolveView(findPrecedingViewTagForTransition);
                    resolveView = view2;
                    view2 = resolveView2;
                } else {
                    resolveView = reanimatedNativeHierarchyManager.resolveView(findPrecedingViewTagForTransition);
                }
                if (z2) {
                    clearAllSharedConfigsForView(view2);
                    clearAllSharedConfigsForView(resolveView);
                } else {
                    boolean containsKey = this.mCurrentSharedTransitionViews.containsKey(Integer.valueOf(view2.getId()));
                    boolean containsKey2 = this.mCurrentSharedTransitionViews.containsKey(Integer.valueOf(resolveView.getId()));
                    if (!containsKey && !containsKey2) {
                        View findScreen = findScreen(view2);
                        View findScreen2 = findScreen(resolveView);
                        if (findScreen != null && findScreen2 != null && (viewGroup = (ViewGroup) findStack(findScreen)) != null && (childCount = (viewGroupManager = (ViewGroupManager) reanimatedNativeHierarchyManager.resolveViewManager(viewGroup.getId())).getChildCount((ViewGroupManager) viewGroup)) >= 2) {
                            View childAt = viewGroupManager.getChildAt((ViewGroupManager) viewGroup, childCount - 1);
                            View childAt2 = viewGroupManager.getChildAt((ViewGroupManager) viewGroup, childCount - 2);
                            if (!(!z ? !(childAt.getId() == findScreen.getId() && childAt2.getId() == findScreen2.getId()) : !(childAt2.getId() == findScreen.getId() && childAt.getId() == findScreen2.getId()))) {
                            }
                        }
                    }
                    Snapshot snapshot = null;
                    if (z) {
                        this.mViewTagsToHide.add(Integer.valueOf(view2.getId()));
                        if (containsKey) {
                            snapshot = new Snapshot(view2);
                        } else {
                            makeSnapshot(view2);
                        }
                        makeSnapshot(resolveView);
                    } else if (containsKey) {
                        makeSnapshot(view2);
                    }
                    if (snapshot == null) {
                        snapshot = this.mSnapshotRegistry.get(Integer.valueOf(view2.getId()));
                    }
                    arrayList.add(view2);
                    arrayList.add(resolveView);
                    arrayList2.add(new SharedElement(view2, snapshot, resolveView, this.mSnapshotRegistry.get(Integer.valueOf(resolveView.getId()))));
                }
            }
        }
        if (!arrayList.isEmpty()) {
            for (View view3 : this.mCurrentSharedTransitionViews.values()) {
                if (arrayList.contains(view3)) {
                    disableCleaningForViewTag(view3.getId());
                } else {
                    this.mViewsWithCanceledAnimation.put(Integer.valueOf(view3.getId()), view3);
                }
            }
            this.mCurrentSharedTransitionViews.clear();
            for (View view4 : arrayList) {
                this.mCurrentSharedTransitionViews.put(Integer.valueOf(view4.getId()), view4);
            }
            for (View view5 : new ArrayList(this.mViewsWithCanceledAnimation.values())) {
                cancelAnimation(view5);
                finishSharedAnimation(view5.getId());
            }
        }
        this.mSharedElements = arrayList2;
        return arrayList2;
    }

    private void setupTransitionContainer() {
        if (this.mIsSharedTransitionActive) {
            return;
        }
        this.mIsSharedTransitionActive = true;
        ReactContext context = this.mAnimationsManager.getContext();
        Activity currentActivity = context.getCurrentActivity();
        if (currentActivity == null) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) currentActivity.getWindow().getDecorView().getRootView();
        if (this.mTransitionContainer == null) {
            this.mTransitionContainer = new ReactViewGroup(context);
        }
        viewGroup.addView(this.mTransitionContainer);
        this.mTransitionContainer.bringToFront();
    }

    private void reparentSharedViewsForCurrentTransition(List<SharedElement> list) {
        for (SharedElement sharedElement : list) {
            View view = sharedElement.sourceView;
            View view2 = sharedElement.targetView;
            if (!this.mSharedTransitionParent.containsKey(Integer.valueOf(view.getId()))) {
                this.mSharedTransitionParent.put(Integer.valueOf(view.getId()), (View) view.getParent());
                this.mSharedTransitionInParentIndex.put(Integer.valueOf(view.getId()), Integer.valueOf(((ViewGroup) view.getParent()).indexOfChild(view)));
                ((ViewGroup) view.getParent()).removeView(view);
                ((ViewGroup) this.mTransitionContainer).addView(view);
            }
            if (!this.mSharedTransitionParent.containsKey(Integer.valueOf(view2.getId()))) {
                this.mSharedTransitionParent.put(Integer.valueOf(view2.getId()), (View) view2.getParent());
                this.mSharedTransitionInParentIndex.put(Integer.valueOf(view2.getId()), Integer.valueOf(((ViewGroup) view2.getParent()).indexOfChild(view2)));
                ((ViewGroup) view2.getParent()).removeView(view2);
                ((ViewGroup) this.mTransitionContainer).addView(view2);
            }
        }
    }

    private void startSharedTransition(List<SharedElement> list) {
        for (SharedElement sharedElement : list) {
            startSharedAnimationForView(sharedElement.sourceView, sharedElement.sourceViewSnapshot, sharedElement.targetViewSnapshot);
            startSharedAnimationForView(sharedElement.targetView, sharedElement.sourceViewSnapshot, sharedElement.targetViewSnapshot);
        }
    }

    private void startSharedAnimationForView(View view, Snapshot snapshot, Snapshot snapshot2) {
        HashMap<String, Object> targetMap = snapshot2.toTargetMap();
        HashMap<String, Object> prepareDataForAnimationWorklet = this.mAnimationsManager.prepareDataForAnimationWorklet(snapshot.toCurrentMap(), false, true);
        HashMap<String, Object> hashMap = new HashMap<>(this.mAnimationsManager.prepareDataForAnimationWorklet(targetMap, true, true));
        hashMap.putAll(prepareDataForAnimationWorklet);
        this.mNativeMethodsHolder.startAnimation(view.getId(), 4, hashMap);
    }

    public void finishSharedAnimation(int i) {
        ViewParent parent;
        if (this.mDisableCleaningForViewTag.containsKey(Integer.valueOf(i))) {
            enableCleaningForViewTag(i);
            return;
        }
        View view = this.mCurrentSharedTransitionViews.get(Integer.valueOf(i));
        if (view == null && (view = this.mViewsWithCanceledAnimation.get(Integer.valueOf(i))) != null) {
            this.mViewsWithCanceledAnimation.remove(Integer.valueOf(view.getId()));
        }
        if (view != null) {
            int id = view.getId();
            ((ViewGroup) this.mTransitionContainer).removeView(view);
            int intValue = this.mSharedTransitionInParentIndex.get(Integer.valueOf(id)).intValue();
            ViewGroup viewGroup = (ViewGroup) this.mSharedTransitionParent.get(Integer.valueOf(id));
            if (intValue <= viewGroup.getChildCount()) {
                viewGroup.addView(view, intValue);
            } else {
                viewGroup.addView(view);
            }
            Snapshot snapshot = this.mSnapshotRegistry.get(Integer.valueOf(id));
            if (snapshot != null) {
                int i2 = snapshot.originY;
                if (findStack(view) == null) {
                    snapshot.originY = snapshot.originYByParent;
                }
                HashMap<String, Object> basicMap = snapshot.toBasicMap();
                HashMap hashMap = new HashMap();
                for (String str : basicMap.keySet()) {
                    Object obj = basicMap.get(str);
                    if (str.equals(Snapshot.TRANSFORM_MATRIX)) {
                        hashMap.put(str, obj);
                    } else {
                        hashMap.put(str, Double.valueOf(PixelUtil.toDIPFromPixel(((Integer) obj).intValue())));
                    }
                }
                this.mAnimationsManager.progressLayoutAnimation(id, hashMap, true);
                snapshot.originY = i2;
            }
            if (this.mViewTagsToHide.contains(Integer.valueOf(i))) {
                view.setVisibility(4);
            }
            this.mCurrentSharedTransitionViews.remove(Integer.valueOf(id));
            this.mSharedTransitionParent.remove(Integer.valueOf(id));
            this.mSharedTransitionInParentIndex.remove(Integer.valueOf(id));
        }
        if (this.mCurrentSharedTransitionViews.isEmpty()) {
            View view2 = this.mTransitionContainer;
            if (view2 != null && (parent = view2.getParent()) != null) {
                ((ViewGroup) parent).removeView(this.mTransitionContainer);
            }
            this.mSharedElements.clear();
            this.mIsSharedTransitionActive = false;
        }
    }

    @Nullable
    private View findScreen(View view) {
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent.getClass().getSimpleName().equals("Screen")) {
                return (View) parent;
            }
        }
        return null;
    }

    @Nullable
    private View findStack(View view) {
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent.getClass().getSimpleName().equals("ScreenStack")) {
                return (View) parent;
            }
        }
        return null;
    }

    public void makeSnapshot(View view) {
        this.mSnapshotRegistry.put(Integer.valueOf(view.getId()), new Snapshot(view));
    }

    /* loaded from: classes.dex */
    public class SnapshotTreeVisitor implements TreeVisitor {
        SnapshotTreeVisitor() {
            SharedTransitionManager.this = r1;
        }

        @Override // com.swmansion.reanimated.layoutReanimation.SharedTransitionManager.TreeVisitor
        public void run(View view) {
            if (SharedTransitionManager.this.mAnimationsManager.hasAnimationForTag(view.getId(), 4)) {
                SharedTransitionManager.this.mRemovedSharedViews.add(view);
                SharedTransitionManager.this.makeSnapshot(view);
            }
        }
    }

    /* loaded from: classes.dex */
    public class ConfigCleanerTreeVisitor implements TreeVisitor {
        ConfigCleanerTreeVisitor() {
            SharedTransitionManager.this = r1;
        }

        @Override // com.swmansion.reanimated.layoutReanimation.SharedTransitionManager.TreeVisitor
        public void run(View view) {
            SharedTransitionManager.this.mNativeMethodsHolder.clearAnimationConfig(view.getId());
        }
    }

    protected void visitTreeForTags(int[] iArr, TreeVisitor treeVisitor) {
        if (iArr == null) {
            return;
        }
        ReanimatedNativeHierarchyManager reanimatedNativeHierarchyManager = this.mAnimationsManager.getReanimatedNativeHierarchyManager();
        for (int i : iArr) {
            visitTree(reanimatedNativeHierarchyManager.resolveView(i), treeVisitor);
        }
    }

    private void visitTree(View view, TreeVisitor treeVisitor) {
        int id = view.getId();
        if (id == -1) {
            return;
        }
        ReanimatedNativeHierarchyManager reanimatedNativeHierarchyManager = this.mAnimationsManager.getReanimatedNativeHierarchyManager();
        try {
            treeVisitor.run(view);
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                ViewManager resolveViewManager = reanimatedNativeHierarchyManager.resolveViewManager(id);
                ViewGroupManager viewGroupManager = resolveViewManager instanceof ViewGroupManager ? (ViewGroupManager) resolveViewManager : null;
                if (viewGroupManager == null) {
                    return;
                }
                for (int i = 0; i < viewGroupManager.getChildCount((ViewGroupManager) viewGroup); i++) {
                    visitTree(viewGroupManager.getChildAt((ViewGroupManager) viewGroup, i), treeVisitor);
                }
            }
        } catch (IllegalViewOperationException unused) {
        }
    }

    void visitNativeTreeAndMakeSnapshot(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (this.mAnimationsManager.hasAnimationForTag(view.getId(), 4)) {
                makeSnapshot(view);
            }
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                visitNativeTreeAndMakeSnapshot(viewGroup.getChildAt(i));
            }
        }
    }

    private void clearAllSharedConfigsForView(View view) {
        int id = view.getId();
        this.mSnapshotRegistry.remove(Integer.valueOf(id));
        this.mNativeMethodsHolder.clearAnimationConfig(id);
    }

    private void cancelAnimation(View view) {
        this.mNativeMethodsHolder.cancelAnimation(view.getId(), 4, true, true);
    }

    private void disableCleaningForViewTag(int i) {
        Integer num = this.mDisableCleaningForViewTag.get(Integer.valueOf(i));
        if (num != null) {
            this.mDisableCleaningForViewTag.put(Integer.valueOf(i), Integer.valueOf(num.intValue() + 1));
        } else {
            this.mDisableCleaningForViewTag.put(Integer.valueOf(i), 1);
        }
    }

    private void enableCleaningForViewTag(int i) {
        Integer num = this.mDisableCleaningForViewTag.get(Integer.valueOf(i));
        if (num == null) {
            return;
        }
        if (num.intValue() == 1) {
            this.mDisableCleaningForViewTag.remove(Integer.valueOf(i));
        } else {
            this.mDisableCleaningForViewTag.put(Integer.valueOf(i), Integer.valueOf(num.intValue() - 1));
        }
    }
}
