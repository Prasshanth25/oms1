package net.time4j;

import net.time4j.engine.ChronoEntity;
import net.time4j.engine.ChronoOperator;

/* loaded from: classes2.dex */
public final class FractionOperator<T extends ChronoEntity<T>> implements ChronoOperator<T> {
    private static final int KILO = 1000;
    private static final int MIO = 1000000;
    private final char fraction;
    private final boolean up;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // net.time4j.engine.ChronoOperator
    public /* bridge */ /* synthetic */ Object apply(Object obj) {
        return apply((FractionOperator<T>) ((ChronoEntity) obj));
    }

    public FractionOperator(char c, boolean z) {
        this.fraction = c;
        this.up = z;
    }

    public T apply(T t) {
        if (this.fraction == '9') {
            return t;
        }
        int intValue = ((Integer) t.get(PlainTime.NANO_OF_SECOND)).intValue();
        int intValue2 = ((Integer) t.getMaximum(PlainTime.NANO_OF_SECOND)).intValue();
        char c = this.fraction;
        if (c == '3') {
            return (T) t.with(PlainTime.NANO_OF_SECOND, Math.min(intValue2, ((intValue / 1000000) * 1000000) + (this.up ? 999999 : 0)));
        } else if (c == '6') {
            return (T) t.with(PlainTime.NANO_OF_SECOND, Math.min(intValue2, ((intValue / 1000) * 1000) + (this.up ? 999 : 0)));
        } else {
            throw new UnsupportedOperationException("Unknown: " + this.fraction);
        }
    }
}
