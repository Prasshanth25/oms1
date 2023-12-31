package com.facebook.react;

import com.facebook.react.bridge.ModuleHolder;
import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactMarker;
import com.facebook.react.bridge.ReactMarkerConstants;
import com.facebook.react.module.model.ReactModuleInfo;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.systrace.SystraceMessage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class LazyReactPackage implements ReactPackage {
    public abstract List<ModuleSpec> getNativeModules(ReactApplicationContext reactApplicationContext);

    public abstract ReactModuleInfoProvider getReactModuleInfoProvider();

    @Deprecated
    public static ReactModuleInfoProvider getReactModuleInfoProviderViaReflection(LazyReactPackage lazyReactPackage) {
        return new ReactModuleInfoProvider() { // from class: com.facebook.react.LazyReactPackage.1
            @Override // com.facebook.react.module.model.ReactModuleInfoProvider
            public Map<String, ReactModuleInfo> getReactModuleInfos() {
                return Collections.emptyMap();
            }
        };
    }

    public Iterable<ModuleHolder> getNativeModuleIterator(ReactApplicationContext reactApplicationContext) {
        final Map<String, ReactModuleInfo> reactModuleInfos = getReactModuleInfoProvider().getReactModuleInfos();
        final List<ModuleSpec> nativeModules = getNativeModules(reactApplicationContext);
        return new Iterable<ModuleHolder>() { // from class: com.facebook.react.LazyReactPackage.2
            @Override // java.lang.Iterable
            public Iterator<ModuleHolder> iterator() {
                return new Iterator<ModuleHolder>() { // from class: com.facebook.react.LazyReactPackage.2.1
                    int position = 0;

                    @Override // java.util.Iterator
                    public ModuleHolder next() {
                        List list = nativeModules;
                        int i = this.position;
                        this.position = i + 1;
                        ModuleSpec moduleSpec = (ModuleSpec) list.get(i);
                        String name = moduleSpec.getName();
                        ReactModuleInfo reactModuleInfo = (ReactModuleInfo) reactModuleInfos.get(name);
                        if (reactModuleInfo == null) {
                            ReactMarker.logMarker(ReactMarkerConstants.CREATE_MODULE_START, name);
                            try {
                                NativeModule nativeModule = moduleSpec.getProvider().get();
                                ReactMarker.logMarker(ReactMarkerConstants.CREATE_MODULE_END);
                                return new ModuleHolder(nativeModule);
                            } catch (Throwable th) {
                                ReactMarker.logMarker(ReactMarkerConstants.CREATE_MODULE_END);
                                throw th;
                            }
                        }
                        return new ModuleHolder(reactModuleInfo, moduleSpec.getProvider());
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.position < nativeModules.size();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException("Cannot remove native modules from the list");
                    }
                };
            }
        };
    }

    @Override // com.facebook.react.ReactPackage
    public final List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        ArrayList arrayList = new ArrayList();
        for (ModuleSpec moduleSpec : getNativeModules(reactApplicationContext)) {
            SystraceMessage.beginSection(0L, "createNativeModule").flush();
            ReactMarker.logMarker(ReactMarkerConstants.CREATE_MODULE_START, moduleSpec.getName());
            try {
                NativeModule nativeModule = moduleSpec.getProvider().get();
                ReactMarker.logMarker(ReactMarkerConstants.CREATE_MODULE_END);
                SystraceMessage.endSection(0L).flush();
                arrayList.add(nativeModule);
            } catch (Throwable th) {
                ReactMarker.logMarker(ReactMarkerConstants.CREATE_MODULE_END);
                SystraceMessage.endSection(0L).flush();
                throw th;
            }
        }
        return arrayList;
    }

    public List<ModuleSpec> getViewManagers(ReactApplicationContext reactApplicationContext) {
        return Collections.emptyList();
    }

    @Override // com.facebook.react.ReactPackage
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        List<ModuleSpec> viewManagers = getViewManagers(reactApplicationContext);
        if (viewManagers == null || viewManagers.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (ModuleSpec moduleSpec : viewManagers) {
            arrayList.add((ViewManager) moduleSpec.getProvider().get());
        }
        return arrayList;
    }
}
