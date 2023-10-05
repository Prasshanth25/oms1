package com.henninghall.date_picker.props;

import com.facebook.react.bridge.Dynamic;
import com.henninghall.date_picker.models.Variant;

/* loaded from: classes.dex */
public class VariantProp extends Prop<Variant> {
    public static final String name = "androidVariant";

    @Override // com.henninghall.date_picker.props.Prop
    public Variant toValue(Dynamic dynamic) {
        return Variant.valueOf(dynamic.asString());
    }
}
