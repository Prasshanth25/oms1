package com.henninghall.date_picker;

import android.widget.LinearLayout;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.henninghall.date_picker.props.DateProp;
import com.henninghall.date_picker.props.DividerHeightProp;
import com.henninghall.date_picker.props.FadeToColorProp;
import com.henninghall.date_picker.props.Is24hourSourceProp;
import com.henninghall.date_picker.props.MaximumDateProp;
import com.henninghall.date_picker.props.MinimumDateProp;
import com.henninghall.date_picker.props.MinuteIntervalProp;
import com.henninghall.date_picker.props.ModeProp;
import com.henninghall.date_picker.props.TextColorProp;
import com.henninghall.date_picker.props.TimezoneOffsetInMinutesProp;
import com.henninghall.date_picker.props.VariantProp;
import java.lang.reflect.Method;
import java.util.Map;

/* loaded from: classes.dex */
public class DatePickerManager extends SimpleViewManager<PickerView> {
    private static final String REACT_CLASS = "DatePickerManager";
    private static final int SCROLL = 1;

    @Override // com.facebook.react.uimanager.ViewManager, com.facebook.react.bridge.NativeModule
    public String getName() {
        return REACT_CLASS;
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public PickerView createViewInstance(ThemedReactContext themedReactContext) {
        return new PickerView(new LinearLayout.LayoutParams(-1, -1));
    }

    @ReactPropGroup(names = {DateProp.name, ModeProp.name, "locale", MaximumDateProp.name, MinimumDateProp.name, FadeToColorProp.name, TextColorProp.name, TimezoneOffsetInMinutesProp.name, MinuteIntervalProp.name, VariantProp.name, DividerHeightProp.name, Is24hourSourceProp.name})
    public void setProps(PickerView pickerView, int i, Dynamic dynamic) {
        updateProp("setProps", pickerView, i, dynamic);
    }

    @ReactPropGroup(customType = "Style", names = {"height"})
    public void setStyle(PickerView pickerView, int i, Dynamic dynamic) {
        updateProp("setStyle", pickerView, i, dynamic);
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(ViewProps.SCROLL, 1);
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public void onAfterUpdateTransaction(PickerView pickerView) {
        super.onAfterUpdateTransaction((DatePickerManager) pickerView);
        try {
            pickerView.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override // com.facebook.react.uimanager.ViewManager
    public void receiveCommand(PickerView pickerView, int i, ReadableArray readableArray) {
        if (i == 1) {
            pickerView.scroll(readableArray.getInt(0), readableArray.getInt(1));
        }
    }

    @Override // com.facebook.react.uimanager.BaseViewManager, com.facebook.react.uimanager.ViewManager
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder().put("dateChange", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onChange"))).build();
    }

    private void updateProp(String str, PickerView pickerView, int i, Dynamic dynamic) {
        pickerView.updateProp(getMethodAnnotation(str).names()[i], dynamic);
    }

    private ReactPropGroup getMethodAnnotation(String str) {
        Method[] methods;
        Method method = null;
        for (Method method2 : getClass().getMethods()) {
            if (method2.getName().equals(str)) {
                method = method2;
            }
        }
        return (ReactPropGroup) method.getAnnotation(ReactPropGroup.class);
    }
}
