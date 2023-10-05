package com.henninghall.date_picker.wheelFunctions;

import com.henninghall.date_picker.wheels.Wheel;

/* loaded from: classes.dex */
public class Refresh implements WheelFunction {
    @Override // com.henninghall.date_picker.wheelFunctions.WheelFunction
    public void apply(Wheel wheel) {
        wheel.refresh();
    }
}
