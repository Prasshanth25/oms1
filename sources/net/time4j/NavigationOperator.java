package net.time4j;

import java.lang.Enum;
import net.time4j.engine.ChronoElement;
import net.time4j.engine.ChronoEntity;
import net.time4j.engine.ChronoException;
import net.time4j.engine.ChronoOperator;

/* loaded from: classes2.dex */
public final class NavigationOperator<V extends Enum<V>> extends ElementOperator<PlainDate> {
    private final int len;
    private final ChronoOperator<PlainTimestamp> navTS;
    private final V value;

    public NavigationOperator(ChronoElement<V> chronoElement, int i, V v) {
        super(chronoElement, i);
        if (v == null) {
            throw new NullPointerException("Missing value.");
        }
        this.value = v;
        this.len = chronoElement.getType().getEnumConstants().length;
        this.navTS = new ChronoOperator<PlainTimestamp>() { // from class: net.time4j.NavigationOperator.1
            @Override // net.time4j.engine.ChronoOperator
            public PlainTimestamp apply(PlainTimestamp plainTimestamp) {
                return (PlainTimestamp) NavigationOperator.this.doApply(plainTimestamp);
            }
        };
    }

    @Override // net.time4j.engine.ChronoOperator
    public PlainDate apply(PlainDate plainDate) {
        return (PlainDate) doApply(plainDate);
    }

    @Override // net.time4j.ElementOperator
    public ChronoOperator<PlainTimestamp> onTimestamp() {
        return this.navTS;
    }

    public <T extends ChronoEntity<T>> T doApply(T t) {
        String str;
        if (t.contains(PlainDate.CALENDAR_DATE)) {
            PlainDate plainDate = (PlainDate) t.get(PlainDate.CALENDAR_DATE);
            int ordinal = ((Enum) Enum.class.cast(plainDate.get(getElement()))).ordinal();
            int delta = delta(ordinal);
            return delta == ordinal ? t : (T) t.with(PlainDate.CALENDAR_DATE, plainDate.plus(delta - ordinal, plainDate.getChronology().getBaseUnit(getElement())));
        }
        switch (getType()) {
            case 9:
                str = "setToNext";
                break;
            case 10:
                str = "setToPrevious";
                break;
            case 11:
                str = "setToNextOrSame";
                break;
            case 12:
                str = "setToPreviousOrSame";
                break;
            default:
                throw new AssertionError("Unknown: " + getType());
        }
        throw new ChronoException(str + "()-operation not supported on: " + getElement().name());
    }

    private int delta(int i) {
        int i2;
        int i3;
        int ordinal = this.value.ordinal();
        switch (getType()) {
            case 9:
                if (ordinal <= i) {
                    i2 = this.len;
                    return ordinal + i2;
                }
                return ordinal;
            case 10:
                if (ordinal >= i) {
                    i3 = this.len;
                    return ordinal - i3;
                }
                return ordinal;
            case 11:
                if (ordinal < i) {
                    i2 = this.len;
                    return ordinal + i2;
                }
                return ordinal;
            case 12:
                if (ordinal > i) {
                    i3 = this.len;
                    return ordinal - i3;
                }
                return ordinal;
            default:
                throw new AssertionError("Unknown: " + getType());
        }
    }
}
