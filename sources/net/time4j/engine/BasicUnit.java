package net.time4j.engine;

/* loaded from: classes2.dex */
public abstract class BasicUnit implements ChronoUnit {
    public <T extends ChronoEntity<T>> UnitRule<T> derive(Chronology<T> chronology) {
        return null;
    }

    @Override // net.time4j.engine.ChronoUnit
    public boolean isCalendrical() {
        return Double.compare(getLength(), 86400.0d) >= 0;
    }

    public final <T extends ChronoEntity<T>> UnitRule<T> derive(T t) {
        return derive(t.getChronology());
    }
}
