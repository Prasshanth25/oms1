package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;
import com.henninghall.date_picker.models.Mode;

/* loaded from: classes.dex */
public class ModeProp extends Prop<Mode> {
    public static final String name = "mode";

    @Override // com.henninghall.date_picker.props.Prop
    public Mode toValue(Dynamic dynamic) {
        return Mode.valueOf(dynamic.asString());
    }
}
