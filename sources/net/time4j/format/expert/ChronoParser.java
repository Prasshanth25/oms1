package net.time4j.format.expert;

import net.time4j.engine.AttributeQuery;

/* loaded from: classes2.dex */
public interface ChronoParser<T> {
    T parse(CharSequence charSequence, ParseLog parseLog, AttributeQuery attributeQuery);
}
