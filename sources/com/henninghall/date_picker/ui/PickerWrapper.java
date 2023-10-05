package com.henninghall.date_picker.ui;

import android.view.View;
import android.widget.LinearLayout;
import com.henninghall.date_picker.R;

/* loaded from: classes.dex */
public class PickerWrapper {
    private final LinearLayout view;

    public PickerWrapper(View view) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.pickerWrapper);
        this.view = linearLayout;
        linearLayout.setWillNotDraw(false);
    }

    public void addPicker(View view) {
        this.view.addView(view);
    }

    void addPicker(View view, int i) {
        this.view.addView(view, i);
    }

    public void removeAll() {
        this.view.removeAllViews();
    }
}
