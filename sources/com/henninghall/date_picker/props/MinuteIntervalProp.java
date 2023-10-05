package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;

/* loaded from: classes.dex */
public class MinuteIntervalProp extends Prop<Integer> {
    public static final String name = "minuteInterval";

    public MinuteIntervalProp() {
        super(1);
    }

    @Override // com.henninghall.date_picker.props.Prop
    public Integer toValue(Dynamic dynamic) {
        return Integer.valueOf(dynamic.asInt());
    }
}
