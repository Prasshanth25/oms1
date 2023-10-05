package kotlin.time;

import com.facebook.react.uimanager.ViewProps;
import kotlin.Metadata;
import org.apache.commons.lang3.ClassUtils;

/* compiled from: TimeSources.kt */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002ø\u0001\u0000¢\u0006\u0004\b\t\u0010\nJ\u001b\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086\u0002ø\u0001\u0000¢\u0006\u0004\b\f\u0010\nJ\b\u0010\r\u001a\u00020\u0004H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000e"}, d2 = {"Lkotlin/time/TestTimeSource;", "Lkotlin/time/AbstractLongTimeSource;", "()V", "reading", "", ViewProps.OVERFLOW, "", "duration", "Lkotlin/time/Duration;", "overflow-LRDsOJo", "(J)V", "plusAssign", "plusAssign-LRDsOJo", "read", "kotlin-stdlib"}, k = 1, mv = {1, 7, 1}, xi = 48)
/* loaded from: classes2.dex */
public final class TestTimeSource extends AbstractLongTimeSource {
    private long reading;

    public TestTimeSource() {
        super(DurationUnit.NANOSECONDS);
    }

    @Override // kotlin.time.AbstractLongTimeSource
    protected long read() {
        return this.reading;
    }

    /* renamed from: plusAssign-LRDsOJo */
    public final void m1898plusAssignLRDsOJo(long j) {
        long j2;
        long m1810toLongimpl = Duration.m1810toLongimpl(j, getUnit());
        if (m1810toLongimpl != Long.MIN_VALUE && m1810toLongimpl != Long.MAX_VALUE) {
            long j3 = this.reading;
            j2 = j3 + m1810toLongimpl;
            if ((m1810toLongimpl ^ j3) >= 0 && (j3 ^ j2) < 0) {
                m1897overflowLRDsOJo(j);
            }
        } else {
            double m1807toDoubleimpl = this.reading + Duration.m1807toDoubleimpl(j, getUnit());
            if (m1807toDoubleimpl > 9.223372036854776E18d || m1807toDoubleimpl < -9.223372036854776E18d) {
                m1897overflowLRDsOJo(j);
            }
            j2 = (long) m1807toDoubleimpl;
        }
        this.reading = j2;
    }

    /* renamed from: overflow-LRDsOJo */
    private final void m1897overflowLRDsOJo(long j) {
        throw new IllegalStateException("TestTimeSource will overflow if its reading " + this.reading + "ns is advanced by " + ((Object) Duration.m1813toStringimpl(j)) + ClassUtils.PACKAGE_SEPARATOR_CHAR);
    }
}
