package com.henninghall.date_picker;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class DatePickerPackage implements ReactPackage {
    public static ReactApplicationContext context;

    @Override // com.facebook.react.ReactPackage
    public List<NativeModule> createNativeModules(ReactApplicationContext reactApplicationContext) {
        context = reactApplicationContext;
        return Arrays.asList(new DatePickerModule(reactApplicationContext));
    }

    @Override // com.facebook.react.ReactPackage
    public List<ViewManager> createViewManagers(ReactApplicationContext reactApplicationContext) {
        context = reactApplicationContext;
        return Arrays.asList(new DatePickerManager());
    }
}
