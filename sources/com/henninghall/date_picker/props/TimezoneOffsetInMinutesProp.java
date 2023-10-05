package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;

/* loaded from: classes.dex */
public class TimezoneOffsetInMinutesProp extends Prop<Integer> {
    public static final String name = "timezoneOffsetInMinutes";

    @Override // com.henninghall.date_picker.props.Prop
    public Integer toValue(Dynamic dynamic) {
        if (dynamic.isNull()) {
            return null;
        }
        return Integer.valueOf(dynamic.asInt());
    }
}
