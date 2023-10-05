package com.henninghall.date_picker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Dynamic;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import net.time4j.android.ApplicationStarter;

/* loaded from: classes.dex */
public class DatePickerModule extends ReactContextBaseJavaModule {
    private AlertDialog dialog;

    @ReactMethod
    public void addListener(String str) {
    }

    @Override // com.facebook.react.bridge.NativeModule
    public String getName() {
        return "RNDatePicker";
    }

    @ReactMethod
    public void removeListeners(Integer num) {
    }

    public DatePickerModule(ReactApplicationContext reactApplicationContext) {
        super(reactApplicationContext);
        ApplicationStarter.initialize((Context) reactApplicationContext, false);
    }

    @ReactMethod
    public void openPicker(ReadableMap readableMap, Callback callback, Callback callback2) {
        AlertDialog createDialog = createDialog(readableMap, createPicker(readableMap), callback, callback2);
        this.dialog = createDialog;
        createDialog.show();
    }

    @ReactMethod
    public void closePicker() {
        this.dialog.dismiss();
    }

    private AlertDialog createDialog(ReadableMap readableMap, final PickerView pickerView, final Callback callback, final Callback callback2) {
        String string = readableMap.getString("title");
        String string2 = readableMap.getString("confirmText");
        return new AlertDialog.Builder(DatePickerPackage.context.getCurrentActivity(), getTheme(readableMap)).setTitle(string).setCancelable(true).setView(withTopMargin(pickerView)).setPositiveButton(string2, new DialogInterface.OnClickListener() { // from class: com.henninghall.date_picker.DatePickerModule.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                callback.invoke(pickerView.getDate());
                dialogInterface.dismiss();
            }
        }).setNegativeButton(readableMap.getString("cancelText"), new DialogInterface.OnClickListener() { // from class: com.henninghall.date_picker.DatePickerModule.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                callback2.invoke(new Object[0]);
                dialogInterface.dismiss();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.henninghall.date_picker.DatePickerModule.1
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                callback2.invoke(new Object[0]);
            }
        }).create();
    }

    private int getTheme(ReadableMap readableMap) {
        String string = readableMap.getString("theme");
        if (string == null) {
            return 0;
        }
        string.hashCode();
        if (string.equals("dark")) {
            return 4;
        }
        return !string.equals("light") ? 0 : 5;
    }

    private PickerView createPicker(ReadableMap readableMap) {
        PickerView pickerView = new PickerView(new LinearLayout.LayoutParams(-1, Utils.toDp(RotationOptions.ROTATE_180)));
        ReadableMapKeySetIterator keySetIterator = readableMap.keySetIterator();
        while (keySetIterator.hasNextKey()) {
            String nextKey = keySetIterator.nextKey();
            Dynamic dynamic = readableMap.getDynamic(nextKey);
            if (!nextKey.equals("style")) {
                try {
                    pickerView.updateProp(nextKey, dynamic);
                } catch (Exception unused) {
                }
            }
        }
        pickerView.update();
        return pickerView;
    }

    private View withTopMargin(PickerView pickerView) {
        LinearLayout linearLayout = new LinearLayout(DatePickerPackage.context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
        linearLayout.addView(pickerView);
        linearLayout.setPadding(0, Utils.toDp(20), 0, 0);
        return linearLayout;
    }
}
